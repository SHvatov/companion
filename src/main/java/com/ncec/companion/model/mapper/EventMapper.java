package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.EventDto;
import com.ncec.companion.model.entity.AttachmentEntity;
import com.ncec.companion.model.entity.EventEntity;
import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.repository.AttachmentRepository;
import com.ncec.companion.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
public class EventMapper extends AbstractMapper<EventEntity, EventDto> {
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventMapper(ModelMapper modelMapper,
                       AttachmentRepository attachmentRepository,
                       UserRepository userRepository) {
        super(modelMapper);
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(EventEntity::setId);
            m.skip(EventEntity::setVisitors);
            m.skip(EventEntity::setAttachments);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
            m.skip(EventDto::setVisitors);
            m.skip(EventDto::setAttachments);
        }).setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(EventEntity source, EventDto destination) {
        destination.setAttachments(source.getAttachments()
                .stream()
                .map(AttachmentEntity::getId)
                .collect(Collectors.toSet()));

        destination.setVisitors(source.getVisitors()
                .stream()
                .map(UserEntity::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(EventDto source, EventEntity destination) {
        destination.getAttachments().clear();
        source.getAttachments().forEach(id -> {
            AttachmentEntity attachmentEntity = attachmentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(AttachmentEntity.class, id));
            destination.getAttachments().add(attachmentEntity);
        });

        destination.getVisitors().clear();
        source.getVisitors().forEach(id -> {
            UserEntity userEntity = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, id));
            destination.getVisitors().add(userEntity);
        });
    }
}
