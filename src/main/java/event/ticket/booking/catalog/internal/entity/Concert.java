package event.ticket.booking.catalog.internal.entity;

import event.ticket.booking.shared.consts.ConcertStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "concerts")
@Data
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    private ConcertStatus status; // DRAFT, PUBLISHED, ENDED [cite: 35]
}
