package event.ticket.booking.inventory.internal.component;

import event.ticket.booking.inventory.internal.entity.TicketCategory;
import event.ticket.booking.inventory.internal.repository.TicketCategoryRepository;
import event.ticket.booking.reserving.BookingContract;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DecreaseTicketQuantityEventListener {
    private final TicketCategoryRepository ticketCategoryRepository;

    @ApplicationModuleListener
    void onDecreaseTicketQuantity(BookingContract.DetailCreateReq dto) {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("TicketCategory not found"));
        ticketCategory.setAvailableQuantity(ticketCategory.getAvailableQuantity() - dto.quantity());
        ticketCategoryRepository.save(ticketCategory);
    }
}
