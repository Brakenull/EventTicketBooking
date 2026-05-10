package event.ticket.booking.reserving.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_details")
@Data
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ticketCategoryId;
    private Integer quantity;
    private BigDecimal priceAtBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
