package event.ticket.booking.catalog.internal.service;

import event.ticket.booking.catalog.ConcertContract;
import event.ticket.booking.catalog.internal.entity.Concert;
import event.ticket.booking.catalog.internal.repository.ConcertRepository;
import event.ticket.booking.shared.consts.ConcertStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<ConcertContract.Res> getAll() {
        return concertRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ConcertContract.Res getById(Long id) {
        return mapToDTO(concertRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Concert not found")));
    }

    public void create(ConcertContract.CreateReq dto) {
        Concert concert = new Concert();
        concert.setTitle(dto.title());
        concert.setDescription(dto.description());
        concert.setLocation(dto.location());
        concert.setStartDate(dto.startDate());
        concert.setStatus(ConcertStatus.DRAFT);
        Concert saved = concertRepository.save(concert);

        ConcertContract.CreatedEvent event = new ConcertContract.CreatedEvent(
                saved.getId(),
                saved.getTitle(),
                saved.getLocation(),
                saved.getStartDate()
        );
        eventPublisher.publishEvent(event);
    }

    public void update(Long id, ConcertContract.UpdateReq dto) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));
        concert.setTitle(dto.title());
        concert.setDescription(dto.description());
        concert.setLocation(dto.location());
        concert.setStartDate(dto.startDate());
        concert.setStatus(dto.status());
    }

    private ConcertContract.Res mapToDTO(Concert entity) {
        return new ConcertContract.Res(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getStartDate(),
                entity.getStatus()
        );
    }
}
