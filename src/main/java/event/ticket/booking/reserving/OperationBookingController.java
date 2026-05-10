package event.ticket.booking.reserving;

import event.ticket.booking.reserving.internal.entity.Booking;
import event.ticket.booking.reserving.internal.service.OperationBookingService;
import event.ticket.booking.shared.consts.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/booking")
@RequiredArgsConstructor
public class OperationBookingController {

    private final OperationBookingService operationBookingService;

    @GetMapping
    public ResponseEntity<Page<Booking>> getBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Mặc định sắp xếp đơn hàng mới nhất lên đầu
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Booking> result = operationBookingService.monitorBookings(status, userId, startDate, endDate, pageRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingDetail(@PathVariable Long id) {
        return ResponseEntity.ok(operationBookingService.getBookingDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBookingStatus(@PathVariable Long id, @RequestParam BookingStatus status) {
        operationBookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(Map.of("message", "Update booking status successfully"));
    }
}
