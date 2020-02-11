package com.ncec.companion.service.crud.event;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.EventDto;
import com.ncec.companion.model.entity.EventEntity;
import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.enums.ParticipantType;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.EventRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventCrudServiceImpl
        extends AbstractCrudService<EventEntity, EventDto, EventRepository>
        implements EventCrudService {
    @Autowired
    public EventCrudServiceImpl(EventRepository repository,
                                Mapper<EventEntity, EventDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public EventDto create(EventDto dto) {
        EventEntity eventEntity = new EventEntity();
        mapper.map(dto, eventEntity);
        return mapper.map(repository.save(eventEntity));
    }

    @Override
    public EventDto update(EventDto dto) {
        EventEntity eventEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(EventEntity.class, dto.getId()));
        mapper.map(dto, eventEntity);
        return mapper.map(repository.save(eventEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Set<String> getAllVisitorsEmails(Integer id) {
        EventEntity eventEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EventEntity.class, id));

        return eventEntity.getVisitors()
                .stream()
                .map(UserEntity::getEmail)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EventDto> findAllEventsBetween(Date start, Date end) {
        return repository.findAllEventsBetween(start, end)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EventDto> findAllUpcomingEvents(int daysBefore) {
        Date periodBegin = new Date();
        Date periodEnd = new Date(periodBegin.getTime() + daysBefore * TimeUtils.MILLISECONDS_IN_DAY);
        return findAllEventsBetween(periodBegin, periodEnd);
    }

    @Override
    public Set<EventDto> findAllSuitable(ParticipantType type) {
        return repository.findAllByParticipantType(type)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EventDto> findAllByVisitor(Integer visitorId) {
        return repository.findAll()
                .stream()
                .map(mapper::map)
                .filter(eventDto -> eventDto.getVisitors().contains(visitorId))
                .collect(Collectors.toSet());
    }
}
