package event.ticket.booking.reserving.internal.service;

import event.ticket.booking.inventory.TicketCategoryApi;
import event.ticket.booking.promotion.VoucherApi;
import event.ticket.booking.reserving.BookingContract;
import event.ticket.booking.reserving.internal.entity.Booking;
import event.ticket.booking.reserving.internal.entity.BookingDetail;
import event.ticket.booking.reserving.internal.entity.TicketCategoryCache;
import event.ticket.booking.reserving.internal.entity.VoucherCache;
import event.ticket.booking.reserving.internal.repository.BookingRepository;
import event.ticket.booking.reserving.internal.repository.TicketCategoryCacheRepository;
import event.ticket.booking.reserving.internal.repository.VoucherCacheRepository;
import event.ticket.booking.shared.consts.BookingStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TicketCategoryCacheRepository ticketCategoryCacheRepository;
    private final VoucherCacheRepository voucherCacheRepository;
    private final TicketCategoryApi ticketCategoryApi;
    private final VoucherApi voucherApi;
    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    private static final String RESERVE_TICKET_SCRIPT =
            "local current_tickets = tonumber(redis.call('GET', KEYS[1])) " +
                    "local requested_tickets = tonumber(ARGV[1]) " +
                    "if current_tickets == nil then return -1 end " + // Chưa setup Redis
                    "if current_tickets >= requested_tickets then " +
                    "    redis.call('DECRBY', KEYS[1], requested_tickets) " +
                    "    return 1 " + // Thành công
                    "else " +
                    "    return 0 " + // Hết vé
                    "end";

    public void createBooking(BookingContract.CreateReq dto) {
        Optional<Booking> existingBooking = bookingRepository.findByIdempotencyKey(dto.idempotencyKey());
        if (existingBooking.isPresent()) {
            throw new RuntimeException("Booking already exists");
        }

        BigDecimal subTotal = BigDecimal.ZERO;
        List<BookingDetail> bookingDetails = new ArrayList<>();
        // Dùng để lưu lại những vé đã trừ trên Redis, phòng trường hợp lỗi phải Rollback
        List<BookingContract.DetailCreateReq> reservedItemsInRedis = new ArrayList<>();
        try {
            for (BookingContract.DetailCreateReq detail : dto.details()) {
                TicketCategoryCache ticketCategoryCache = ticketCategoryCacheRepository.findById(detail.categoryId())
                        .orElseThrow(() -> new IllegalArgumentException("TicketCategory not found"));
//                ticketCategoryApi.decreaseTicketQuantity(detail);
                String redisKey = "ticket:available:" + detail.categoryId();
                String priceKey = "ticket:price:" + detail.categoryId();
                String priceStr = redisTemplate.opsForValue().get(priceKey);
                BigDecimal ticketPrice;

                // Chạy Lua Script trên Redis
                DefaultRedisScript<Long> script = new DefaultRedisScript<>(RESERVE_TICKET_SCRIPT, Long.class);
                Long result = redisTemplate.execute(script, Collections.singletonList(redisKey), String.valueOf(detail.quantity()));

                if (result == null || result == -1L) {
                    throw new IllegalStateException("Systen error, please try again later.");
                }
                if (result == 0L) {
                    throw new IllegalStateException("Ticket not available");
                }

                // Đánh dấu là đã giữ vé trên Redis thành công
                reservedItemsInRedis.add(detail);

                if (priceStr != null) {
                    // Trường hợp lý tưởng: Lấy giá từ RAM (Cực nhanh)
                    ticketPrice = new BigDecimal(priceStr);
                } else {
                    // FALLBACK (Cache Miss): Nếu Redis vô tình mất dữ liệu giá, mới gọi xuống DB
                    ticketPrice = ticketCategoryCache.getPrice();

                    // Ghi ngược lại lên Redis để các request sau không bị Miss nữa
                    redisTemplate.opsForValue().set(priceKey, ticketPrice.toString());
                }

                BigDecimal itemTotal = ticketPrice.multiply(BigDecimal.valueOf(detail.quantity()));
                subTotal = subTotal.add(itemTotal);

                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setTicketCategoryId(detail.categoryId());
                bookingDetail.setQuantity(detail.quantity());
                bookingDetail.setPriceAtBooking(ticketCategoryCache.getPrice());
                bookingDetails.add(bookingDetail);
            }
            BigDecimal totalAmount = subTotal;
            Long voucherId = null;

            if (dto.voucherCode() != null) {
                VoucherCache voucherCache = voucherCacheRepository.findByCode(dto.voucherCode())
                        .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
                voucherApi.increaseVoucherUsage(dto.voucherCode());
                totalAmount = totalAmount.subtract(voucherCache.getDiscountValue());
                if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                    totalAmount = BigDecimal.ZERO;
                }
                voucherId = voucherCache.getId();
            }
            Booking booking = new Booking();
            booking.setUserId(1L);
            booking.setVoucherId(voucherId);
            booking.setTotalAmount(totalAmount);
            booking.setStatus(BookingStatus.WAITING);
            booking.setIdempotencyKey(dto.idempotencyKey());
            for (BookingDetail detail : bookingDetails) {
                detail.setBooking(booking);
            }
            booking.setDetails(bookingDetails);
            bookingRepository.save(booking);

            for (BookingContract.DetailCreateReq detail : dto.details()) {
                eventPublisher.publishEvent(detail);
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            // Bắt lỗi Optimistic Locking khi có 2 request cùng mua vé cuối cùng
            throw new IllegalStateException("Please try again later.");
        } catch (DataIntegrityViolationException e) {
            // Dự phòng trường hợp 2 request lưu cùng 1 idempotencyKey cùng 1 mili-giây
            throw new IllegalStateException("Booking already exists");
        } catch (Exception e) {
            rollbackRedisReservation(reservedItemsInRedis);
            throw e;
        }
    }

    private void rollbackRedisReservation(List<BookingContract.DetailCreateReq> reservedItems) {
        for (BookingContract.DetailCreateReq item : reservedItems) {
            String redisKey = "ticket:available:" + item.categoryId();
            // Cộng lại số lượng vé đã lỡ trừ
            redisTemplate.opsForValue().increment(redisKey, item.quantity());
        }
    }
}
