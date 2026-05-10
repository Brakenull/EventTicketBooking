package event.ticket.booking.reserving.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher_cache")
@Data
public class VoucherCache {
    @Id
    private Long id;
    private String code;
    private BigDecimal discountValue;
    LocalDateTime startDate;
    LocalDateTime expirationDate;
}
