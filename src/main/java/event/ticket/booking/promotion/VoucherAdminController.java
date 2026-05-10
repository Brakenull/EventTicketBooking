package event.ticket.booking.promotion;

import event.ticket.booking.promotion.internal.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/vouchers")
@RequiredArgsConstructor
public class VoucherAdminController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<List<VoucherContract.Res>> getAll() {
        return ResponseEntity.ok(voucherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherContract.Res> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody VoucherContract.CreateReq dto) {
        voucherService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Voucher created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody VoucherContract.UpdateReq dto) {
        voucherService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Voucher updated successfully"));
    }
}
