package event.ticket.booking.catalog;

import event.ticket.booking.catalog.internal.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/concerts")
@RequiredArgsConstructor
public class ConcertUserController {
    private final ConcertService concertService;

    @GetMapping
    public ResponseEntity<List<ConcertContract.Res>> getAll() {
        return ResponseEntity.ok(concertService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertContract.Res> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getById(id));
    }
}
