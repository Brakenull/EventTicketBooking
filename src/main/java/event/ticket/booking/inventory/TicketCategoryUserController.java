package event.ticket.booking.inventory;

import event.ticket.booking.inventory.internal.service.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/ticket/categories")
@RequiredArgsConstructor
public class TicketCategoryUserController {
    private final TicketCategoryService ticketCategoryService;

    @GetMapping
    public ResponseEntity<List<TicketCategoryContract.UserRes>> getAll() {
        return ResponseEntity.ok(ticketCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCategoryContract.UserRes> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketCategoryService.getById(id));
    }
}
