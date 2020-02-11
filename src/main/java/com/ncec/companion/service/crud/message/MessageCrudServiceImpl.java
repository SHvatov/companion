package com.ncec.companion.service.crud.message;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.MessageDto;
import com.ncec.companion.model.entity.MessageEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.MessageRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageCrudServiceImpl
        extends AbstractCrudService<MessageEntity, MessageDto, MessageRepository>
        implements MessageCrudService {
    @Autowired
    public MessageCrudServiceImpl(MessageRepository repository,
                                  Mapper<MessageEntity, MessageDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public MessageDto create(MessageDto dto) {
        MessageEntity messageEntity = new MessageEntity();
        mapper.map(dto, messageEntity);
        return mapper.map(repository.save(messageEntity));
    }

    @Override
    public MessageDto update(MessageDto dto) {
        MessageEntity messageEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(MessageEntity.class, dto.getId()));
        mapper.map(dto, messageEntity);
        return mapper.map(repository.save(messageEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
