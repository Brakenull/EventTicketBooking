package event.ticket.booking.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TicketCategoryContract {
    record UserRes(
            Long id,
            String concertTitle,
            String concertLocation,
            LocalDateTime concertStartDate,
            String name,
            BigDecimal price,
            Integer totalQuantity,
            Integer availableQuantity
    ) {}

    record CreateReq(
            Long concertId,
            String name,
            BigDecimal price,
            Integer totalQuantity
    ) {}

    record UpdateReq(
            BigDecimal price,
            Integer totalQuantity
    ) {}
}
