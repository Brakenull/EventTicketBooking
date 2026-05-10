package event.ticket.booking.reserving.internal.component;

import event.ticket.booking.promotion.VoucherContract;
import event.ticket.booking.reserving.internal.entity.VoucherCache;
import event.ticket.booking.reserving.internal.repository.VoucherCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoucherCacheEventListener {
    private final VoucherCacheRepository cacheRepository;

    @ApplicationModuleListener
    void onVoucherCreated(VoucherContract.CreatedEvent dto) {
        VoucherCache cache = new VoucherCache();
        cache.setId(dto.id());
        cache.setCode(dto.code());
        cache.setDiscountValue(dto.discountValue());
        cache.setStartDate(dto.startDate());
        cache.setExpirationDate(dto.expirationDate());
        cacheRepository.save(cache);
    }

    @ApplicationModuleListener
    void onVoucherUpdated(VoucherContract.UpdatedEvent dto) {
        VoucherCache cache = cacheRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        cache.setStartDate(dto.startDate());
        cache.setExpirationDate(dto.expirationDate());
    }
}
