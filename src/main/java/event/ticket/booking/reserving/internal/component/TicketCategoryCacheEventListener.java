package event.ticket.booking.reserving.internal.component;

import event.ticket.booking.inventory.TicketCategoryContract;
import event.ticket.booking.reserving.internal.entity.TicketCategoryCache;
import event.ticket.booking.reserving.internal.repository.TicketCategoryCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketCategoryCacheEventListener {
    private final TicketCategoryCacheRepository cacheRepository;

    @ApplicationModuleListener
    void onTicketCategoryCreated(TicketCategoryContract.CreatedEvent dto) {
        TicketCategoryCache cache = new TicketCategoryCache();
        cache.setId(dto.id());
        cache.setConcertTitle(dto.concertTitle());
        cache.setName(dto.name());
        cache.setPrice(dto.price());
        cacheRepository.save(cache);
    }

    @ApplicationModuleListener
    void onTicketCategoryUpdated(TicketCategoryContract.UpdatedEvent dto) {
        TicketCategoryCache cache = cacheRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("TicketCategory not found"));
        cache.setPrice(dto.price());
    }
}
