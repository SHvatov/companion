package com.ncec.companion.service.crud.event;

import com.ncec.companion.model.dto.EventDto;
import com.ncec.companion.model.enums.ParticipantType;
import com.ncec.companion.service.crud.CrudService;

import java.util.Date;
import java.util.Set;

public interface EventCrudService extends CrudService<EventDto> {
    Set<String> getAllVisitorsEmails(Integer id);

    Set<EventDto> findAllEventsBetween(Date start, Date end);

    Set<EventDto> findAllUpcomingEvents(int daysBefore);

    Set<EventDto> findAllSuitable(ParticipantType type);

    Set<EventDto> findAllByVisitor(Integer visitorId);
}
