package com.ncec.companion.service.business.event;

import com.ncec.companion.model.dto.EventDto;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {
    EventDto save(EventDto dto);

    EventDto update(EventDto dto);

    EventDto addParticipant(Integer eventId, Integer participantId);

    EventDto removeParticipant(Integer eventId, Integer participantId);

    EventDto addAttachment(Integer eventId, MultipartFile file);

    EventDto removeAttachment(Integer eventId, Integer attachmentId);

    void delete(Integer id);
}
