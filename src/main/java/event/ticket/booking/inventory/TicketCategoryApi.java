package event.ticket.booking.inventory;

import event.ticket.booking.reserving.BookingContract;

public interface TicketCategoryApi {
    void decreaseTicketQuantity(BookingContract.DetailCreateReq dto);
    void publishConcert(Long concertId);
}
