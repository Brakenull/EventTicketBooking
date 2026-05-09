package event.ticket.booking.catalog.internal.service;

import event.ticket.booking.catalog.ConcertContract;
import event.ticket.booking.catalog.internal.entity.Concert;
import event.ticket.booking.catalog.internal.repository.ConcertRepository;
import event.ticket.booking.shared.consts.ConcertStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConcertService {
    private final ConcertRepository concertRepository;

    public List<ConcertContract.ConcertRes> getAll() {
        return concertRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ConcertContract.ConcertRes getById(Long id) {
        return mapToDTO(concertRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Concert not found")));
    }

    public void create(ConcertContract.ConcertCreateReq dto) {
        Concert concert = new Concert();
        concert.setTitle(dto.title());
        concert.setDescription(dto.description());
        concert.setLocation(dto.location());
        concert.setStartDate(dto.startDate());
        concert.setStatus(ConcertStatus.DRAFT);
        concertRepository.save(concert);
    }

    public void update(Long id, ConcertContract.ConcertUpdateReq dto) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));
        concert.setTitle(dto.title());
        concert.setDescription(dto.description());
        concert.setLocation(dto.location());
        concert.setStartDate(dto.startDate());
        concert.setStatus(dto.status());
    }

    private ConcertContract.ConcertRes mapToDTO(Concert entity) {
        return new ConcertContract.ConcertRes(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getStartDate(),
                entity.getStatus()
        );
    }
}
