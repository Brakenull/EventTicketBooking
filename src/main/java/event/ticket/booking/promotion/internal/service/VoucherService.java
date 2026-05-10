package event.ticket.booking.promotion.internal.service;

import event.ticket.booking.promotion.VoucherContract;
import event.ticket.booking.promotion.internal.entity.Voucher;
import event.ticket.booking.promotion.internal.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public List<VoucherContract.Res> getAll() {
        return voucherRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public VoucherContract.Res getById(Long id) {
        return voucherRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
    }

    public void create(VoucherContract.CreateReq dto) {
        Voucher voucher = new Voucher();
        voucher.setCode(dto.code());
        voucher.setDiscountValue(dto.discountValue());
        voucher.setMaxUsage(dto.maxUsage());
        voucher.setStartDate(dto.startDate());
        voucher.setExpirationDate(dto.expirationDate());
        voucherRepository.save(voucher);
    }

    public void update(Long id, VoucherContract.UpdateReq dto) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucher.setMaxUsage(dto.maxUsage());
        voucher.setStartDate(dto.startDate());
        voucher.setExpirationDate(dto.expirationDate());
    }

    private VoucherContract.Res mapToDTO(Voucher voucher) {
        return new VoucherContract.Res(
                voucher.getId(),
                voucher.getCode(),
                voucher.getDiscountValue(),
                voucher.getMaxUsage(),
                voucher.getCurrentUsage(),
                voucher.getStartDate(),
                voucher.getExpirationDate(),
                voucher.getCreatedAt(),
                voucher.getUpdatedAt()
        );
    }
}
