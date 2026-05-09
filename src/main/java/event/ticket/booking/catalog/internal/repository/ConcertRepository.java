package event.ticket.booking.catalog.internal.repository;

import event.ticket.booking.catalog.internal.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
