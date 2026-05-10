package event.ticket.booking.reserving.internal.service;

import event.ticket.booking.inventory.TicketCategoryApi;
import event.ticket.booking.inventory.internal.service.TicketCategoryService;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public void createBooking(BookingContract.CreateReq dto) {
        Optional<Booking> existingBooking = bookingRepository.findByIdempotencyKey(dto.idempotencyKey());
        if (existingBooking.isPresent()) {
            throw new RuntimeException("Booking already exists");
        }

        try {
            BigDecimal subTotal = BigDecimal.ZERO;
            List<BookingDetail> bookingDetails = new ArrayList<>();

            for (BookingContract.DetailCreateReq detail : dto.details()) {
                TicketCategoryCache ticketCategoryCache = ticketCategoryCacheRepository.findById(detail.categoryId())
                        .orElseThrow(() -> new IllegalArgumentException("TicketCategory not found"));
                ticketCategoryApi.decreaseTicketQuantity(detail);

                BigDecimal itemTotal = ticketCategoryCache.getPrice().multiply(BigDecimal.valueOf(detail.quantity()));
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
        } catch (ObjectOptimisticLockingFailureException e) {
            // Bắt lỗi Optimistic Locking khi có 2 request cùng mua vé cuối cùng
            throw new IllegalStateException("Please try again later.");
        } catch (DataIntegrityViolationException e) {
            // Dự phòng trường hợp 2 request lưu cùng 1 idempotencyKey cùng 1 mili-giây
            throw new IllegalStateException("Booking already exists");
        }
    }
}
