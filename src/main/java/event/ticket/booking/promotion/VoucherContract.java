package event.ticket.booking.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface VoucherContract {
    record Res(
            Long id,
            String code,
            BigDecimal discountValue,
            Integer maxUsage,
            Integer currentUsage,
            LocalDateTime startDate,
            LocalDateTime expirationDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    record CreateReq(
            String code,
            BigDecimal discountValue,
            Integer maxUsage,
            LocalDateTime startDate,
            LocalDateTime expirationDate
    ) {}

    record UpdateReq(
            Integer maxUsage,
            LocalDateTime startDate,
            LocalDateTime expirationDate
    ) {}

    record CreatedEvent(
            Long id,
            String code,
            BigDecimal discountValue,
            LocalDateTime startDate,
            LocalDateTime expirationDate
    ) {}

    record UpdatedEvent(
            Long id,
            LocalDateTime startDate,
            LocalDateTime expirationDate
    ) {}
}
