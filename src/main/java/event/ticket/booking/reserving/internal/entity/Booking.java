package event.ticket.booking.reserving.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import event.ticket.booking.shared.consts.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @Column(updatable = false)
    private Long userId;

    private Long voucherId;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; // RECEIVED, WAITING_FOR_PAYMENT, COMPLETED, FAILED

    // Ngăn chặn duplicate bookings do người dùng retry khi mạng lag
    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "booking-details")
    private List<BookingDetail> details;
}
