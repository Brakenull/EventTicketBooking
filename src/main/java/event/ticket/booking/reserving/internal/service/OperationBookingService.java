package event.ticket.booking.reserving.internal.service;

import event.ticket.booking.reserving.internal.entity.Booking;
import event.ticket.booking.reserving.internal.repository.BookingRepository;
import event.ticket.booking.shared.consts.BookingStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationBookingService {
    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true) // readOnly = true giúp tối ưu hiệu suất cho câu lệnh SELECT
    public Page<Booking> monitorBookings(
            BookingStatus status,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        // Gọi Specification để tự động sinh câu query SQL tương ứng
        Specification<Booking> spec = filterBookings(status, userId, startDate, endDate);

        // Trả về dữ liệu đã được phân trang
        return bookingRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Booking getBookingDetail(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng: " + bookingId));
    }

    @Transactional
    public void updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
    }

    private static Specification<Booking> filterBookings(
            BookingStatus status,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Thêm điều kiện WHERE status = ?
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Thêm điều kiện WHERE user_id = ?
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }

            // Thêm điều kiện WHERE created_at BETWEEN ? AND ?
            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), startDate, endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
