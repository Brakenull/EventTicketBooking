package event.ticket.booking.inventory.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_categories")
@Data
public class TicketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertId;
    private String name; // VIP, Standard...
    private BigDecimal price;

    private Integer totalQuantity;
    private Integer availableQuantity;

    // Sử dụng Optimistic Locking để ngăn chặn Overselling khi có traffic cao
    @Version
    private Integer version;
}
