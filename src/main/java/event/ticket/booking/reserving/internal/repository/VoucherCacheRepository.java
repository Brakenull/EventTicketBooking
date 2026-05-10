package event.ticket.booking.reserving.internal.repository;

import event.ticket.booking.reserving.internal.entity.VoucherCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherCacheRepository extends JpaRepository<VoucherCache, Long> {
}
