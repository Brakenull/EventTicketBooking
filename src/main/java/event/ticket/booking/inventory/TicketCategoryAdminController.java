package event.ticket.booking.inventory;

import event.ticket.booking.inventory.internal.service.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ticket/categories")
@RequiredArgsConstructor
public class TicketCategoryAdminController {
    private final TicketCategoryService ticketCategoryService;

    @GetMapping
    public ResponseEntity<List<TicketCategoryContract.UserRes>> getAll() {
        return ResponseEntity.ok(ticketCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCategoryContract.UserRes> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketCategoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TicketCategoryContract.CreateReq dto) {
        ticketCategoryService.create(dto);
        return ResponseEntity.ok(Map.of("message", "TicketCategory created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody TicketCategoryContract.UpdateReq dto) {
        ticketCategoryService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "TicketCategory updated successfully"));
    }
}
