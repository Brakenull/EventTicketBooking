package event.ticket.booking.promotion.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private BigDecimal discountValue;
    private Integer maxUsage;
    private Integer currentUsage;
    private LocalDateTime expirationDate;
}
