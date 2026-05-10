package event.ticket.booking.promotion.internal.impl;

import event.ticket.booking.promotion.VoucherApi;
import event.ticket.booking.promotion.internal.entity.Voucher;
import event.ticket.booking.promotion.internal.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherApiImpl implements VoucherApi {
    private final VoucherRepository voucherRepository;

    @Override
    public void increaseVoucherUsage(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
        if (voucher.getCurrentUsage() >= voucher.getMaxUsage()) {
            throw new IllegalStateException("Voucher usage limit reached");
        }
        voucher.setCurrentUsage(voucher.getCurrentUsage() + 1);
        voucherRepository.save(voucher);
    }
}
