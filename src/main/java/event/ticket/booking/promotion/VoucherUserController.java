package event.ticket.booking.promotion;

import event.ticket.booking.promotion.internal.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/vouchers")
@RequiredArgsConstructor
public class VoucherUserController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<List<VoucherContract.Res>> getAll() {
        return ResponseEntity.ok(voucherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherContract.Res> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getById(id));
    }
}
