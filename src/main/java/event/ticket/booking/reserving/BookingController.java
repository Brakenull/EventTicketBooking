package event.ticket.booking.reserving;

import event.ticket.booking.reserving.internal.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/customer/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody BookingContract.CreateReq dto) {
        bookingService.createBooking(dto);
        return ResponseEntity.ok(Map.of("message", "Booking created successfully"));
    }
}
