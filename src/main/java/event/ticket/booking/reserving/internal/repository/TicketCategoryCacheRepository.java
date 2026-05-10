package event.ticket.booking.reserving.internal.repository;

import event.ticket.booking.reserving.internal.entity.TicketCategoryCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketCategoryCacheRepository extends JpaRepository<TicketCategoryCache, Long> {
}
