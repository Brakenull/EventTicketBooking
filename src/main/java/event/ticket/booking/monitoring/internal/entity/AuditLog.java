package event.ticket.booking.monitoring.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private String action; // VD: MANUAL_UPDATE, VALIDATE_AVAILABILITY
    private Long performedBy; // User ID của Operator
    private String reason;
    private LocalDateTime timestamp;
}
