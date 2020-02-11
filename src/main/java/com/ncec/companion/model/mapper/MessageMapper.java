package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.MessageDto;
import com.ncec.companion.model.entity.MessageEntity;
import com.ncec.companion.model.entity.StudentTaskEntity;
import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.repository.StudentTaskRepository;
import com.ncec.companion.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class MessageMapper extends AbstractMapper<MessageEntity, MessageDto> {
    private final UserRepository userRepository;
    private final StudentTaskRepository studentTaskRepository;

    @Autowired
    public MessageMapper(ModelMapper modelMapper,
                         UserRepository userRepository,
                         StudentTaskRepository studentTaskRepository) {
        super(modelMapper);
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(MessageEntity::setId);
            m.skip(MessageEntity::setReceiver);
            m.skip(MessageEntity::setSender);
            m.skip(MessageEntity::setStudentTask);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
            m.skip(MessageDto::setReceiver);
            m.skip(MessageDto::setSender);
            m.skip(MessageDto::setStudentTask);
        }).setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(MessageEntity source, MessageDto destination) {
        destination.setReceiver(source.getReceiver().getId());
        destination.setSender(source.getSender().getId());
        destination.setStudentTask(source.getStudentTask().getId());
    }

    @Override
    protected void mapSpecificFields(MessageDto source, MessageEntity destination) {
        UserEntity receiver = userRepository.findById(source.getReceiver())
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, source.getReceiver()));
        destination.setReceiver(receiver);

        UserEntity sender = userRepository.findById(source.getSender())
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, source.getReceiver()));
        destination.setSender(sender);

        StudentTaskEntity studentTaskEntity = studentTaskRepository.findById(source.getStudentTask())
                .orElseThrow(() -> new EntityNotFoundException(StudentTaskEntity.class, source.getStudentTask()));
        Optional.ofNullable(destination.getStudentTask())
                .ifPresent(current -> current.getMessages().remove(destination));
        destination.setStudentTask(studentTaskEntity);
        studentTaskEntity.getMessages().add(destination);
    }
}
