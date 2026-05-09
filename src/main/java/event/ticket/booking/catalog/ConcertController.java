package event.ticket.booking.catalog;

import event.ticket.booking.catalog.internal.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertService concertService;

    @GetMapping
    public ResponseEntity<List<ConcertContract.ConcertRes>> getAll() {
        return ResponseEntity.ok(concertService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertContract.ConcertRes> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ConcertContract.ConcertCreateReq dto) {
        concertService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Concert created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ConcertContract.ConcertUpdateReq dto) {
        concertService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Concert updated successfully"));
    }
}
