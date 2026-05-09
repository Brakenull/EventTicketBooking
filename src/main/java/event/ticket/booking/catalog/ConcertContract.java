package event.ticket.booking.catalog;

import event.ticket.booking.shared.consts.ConcertStatus;

import java.time.LocalDateTime;

public interface ConcertContract {
    record Res(
            Long id,
            String title,
            String description,
            String location,
            LocalDateTime startDate,
            ConcertStatus status
    ) {}

    record CreateReq(
            String title,
            String description,
            String location,
            LocalDateTime startDate
    ) {}

    record UpdateReq(
            String title,
            String description,
            String location,
            LocalDateTime startDate,
            ConcertStatus status
    ) {}

    record CreatedEvent(
            Long id,
            String title,
            String location,
            LocalDateTime startDate
    ) {}
}
