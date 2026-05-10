package event.ticket.booking.reserving.internal.repository;

import event.ticket.booking.reserving.internal.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdempotencyKey(String s);
}
