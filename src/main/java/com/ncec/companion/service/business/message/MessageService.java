package com.ncec.companion.service.business.message;

import com.ncec.companion.model.dto.MessageDto;

import java.util.Set;

public interface MessageService {
    MessageDto save(MessageDto dto);

    Set<MessageDto> getAllByTask(Integer taskId);
}
