package event.ticket.booking.inventory.internal.component;

import event.ticket.booking.catalog.ConcertContract;
import event.ticket.booking.inventory.internal.entity.ConcertCache;
import event.ticket.booking.inventory.internal.repository.ConcertCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertCacheEventListener {
    private final ConcertCacheRepository cacheRepository;

    @ApplicationModuleListener
    void onConcertCreated(ConcertContract.CreatedEvent dto) {
        ConcertCache cache = new ConcertCache();
        cache.setId(dto.id());
        cache.setTitle(dto.title());
        cache.setLocation(dto.location());
        cache.setStartDate(dto.startDate());
        cacheRepository.save(cache);
    }
}
