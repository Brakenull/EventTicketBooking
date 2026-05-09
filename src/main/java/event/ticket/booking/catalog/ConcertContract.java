package event.ticket.booking.catalog;

import event.ticket.booking.shared.consts.ConcertStatus;

import java.time.LocalDateTime;

public interface ConcertContract {
    record ConcertRes(
            Long id,
            String title,
            String description,
            String location,
            LocalDateTime startDate,
            ConcertStatus status
    ) {}

    record ConcertCreateReq(
            String title,
            String description,
            String location,
            LocalDateTime startDate
    ) {}

    record ConcertUpdateReq(
            String title,
            String description,
            String location,
            LocalDateTime startDate,
            ConcertStatus status
    ) {}
}
