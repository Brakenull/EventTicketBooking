package event.ticket.booking.reserving;

import java.util.List;

public interface BookingContract {
    record CreateReq(
            Long userId,
            String idempotencyKey,
            String voucherCode,
            List<DetailCreateReq> details
    ) {}

    record DetailCreateReq(
            Long categoryId,
            Integer quantity
    ) {}
}
