package event.ticket.booking.inventory.internal.impl;

import event.ticket.booking.inventory.TicketCategoryApi;
import event.ticket.booking.inventory.internal.entity.TicketCategory;
import event.ticket.booking.inventory.internal.repository.TicketCategoryRepository;
import event.ticket.booking.inventory.internal.service.TicketCategoryService;
import event.ticket.booking.reserving.BookingContract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketCategoryApiImpl implements TicketCategoryApi {
    private final TicketCategoryRepository ticketCategoryRepository;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void decreaseTicketQuantity(BookingContract.DetailCreateReq dto) {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("TicketCategory not found"));
        if (ticketCategory.getTotalQuantity() < dto.quantity()) {
            throw new IllegalStateException("Insufficient quantity");
        }
        ticketCategory.setAvailableQuantity(ticketCategory.getAvailableQuantity() - dto.quantity());
        ticketCategoryRepository.save(ticketCategory);
    }

    @Override
    public void publishConcert(Long concertId) {
        List<TicketCategory> ticketCategories = ticketCategoryRepository.findByConcertId(concertId);
        for (TicketCategory ticketCategory : ticketCategories) {
            // Cache số lượng (để Lua Script chạy)
            redisTemplate.opsForValue().set("ticket:available:" + ticketCategory.getId(), ticketCategory.getAvailableQuantity().toString());

            // Cache giá tiền
            redisTemplate.opsForValue().set("ticket:price:" + ticketCategory.getId(), ticketCategory.getPrice().toString());
        }
    }
}
