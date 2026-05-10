package event.ticket.booking.reserving.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_category_cache")
@Data
public class TicketCategoryCache {
    @Id
    private Long id;
    private String concertTitle;
    private String name;
    private BigDecimal price;
}
