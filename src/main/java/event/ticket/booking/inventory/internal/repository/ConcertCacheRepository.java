package event.ticket.booking.inventory.internal.repository;

import event.ticket.booking.inventory.internal.entity.ConcertCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertCacheRepository extends JpaRepository<ConcertCache, Long> {
}
