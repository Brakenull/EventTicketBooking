package event.ticket.booking.inventory.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_cache")
@Data
public class ConcertCache {
    @Id
    private Long id;
    private String title;
    private String location;
    private LocalDateTime startDate;
}
