package event.ticket.booking.inventory.internal.service;

import event.ticket.booking.inventory.TicketCategoryContract;
import event.ticket.booking.inventory.internal.entity.ConcertCache;
import event.ticket.booking.inventory.internal.entity.TicketCategory;
import event.ticket.booking.inventory.internal.repository.ConcertCacheRepository;
import event.ticket.booking.inventory.internal.repository.TicketCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketCategoryService {
    private final TicketCategoryRepository ticketCategoryRepository;
    private final ConcertCacheRepository concertCacheRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StringRedisTemplate redisTemplate;

    public List<TicketCategoryContract.UserRes> getAll() {
        return ticketCategoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TicketCategoryContract.UserRes getById(Long id) {
        return ticketCategoryRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("TicketCategory not found"));
    }

    public void create(TicketCategoryContract.CreateReq dto) {
        TicketCategory ticketCategory = new TicketCategory();
        ticketCategory.setConcertId(dto.concertId());
        ticketCategory.setName(dto.name());
        ticketCategory.setPrice(dto.price());
        ticketCategory.setTotalQuantity(dto.totalQuantity());
        ticketCategory.setAvailableQuantity(dto.totalQuantity());
        TicketCategory saved = ticketCategoryRepository.save(ticketCategory);

        ConcertCache concertCache = concertCacheRepository.findById(saved.getConcertId())
                .orElseThrow(() -> new RuntimeException("concert not found"));
        TicketCategoryContract.CreatedEvent event = new TicketCategoryContract.CreatedEvent(
                saved.getId(),
                concertCache.getTitle(),
                saved.getName(),
                saved.getPrice()
        );
        eventPublisher.publishEvent(event);
    }

    public void update(Long id, TicketCategoryContract.UpdateReq dto) {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketCategory not found"));
        ticketCategory.setPrice(dto.price());
        ticketCategory.setTotalQuantity(dto.totalQuantity());

        redisTemplate.opsForValue().set("ticket:price:" + id, dto.price().toString());
        TicketCategoryContract.UpdatedEvent event = new TicketCategoryContract.UpdatedEvent(
                ticketCategory.getId(),
                ticketCategory.getPrice()
        );
        eventPublisher.publishEvent(event);
    }

    private TicketCategoryContract.UserRes mapToDTO(TicketCategory ticketCategory) {
        ConcertCache concertCache = concertCacheRepository.findById(ticketCategory.getConcertId())
                .orElseThrow(() -> new RuntimeException("concert not found"));

        return new TicketCategoryContract.UserRes(
                ticketCategory.getId(),
                concertCache.getTitle(),
                concertCache.getLocation(),
                concertCache.getStartDate(),
                ticketCategory.getName(),
                ticketCategory.getPrice(),
                ticketCategory.getTotalQuantity(),
                ticketCategory.getAvailableQuantity()
        );
    }
}
