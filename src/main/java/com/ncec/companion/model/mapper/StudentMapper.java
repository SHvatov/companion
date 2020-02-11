package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.entity.GroupEntity;
import com.ncec.companion.model.entity.StudentEntity;
import com.ncec.companion.model.entity.StudentTaskEntity;
import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.repository.GroupRepository;
import com.ncec.companion.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StudentMapper extends AbstractMapper<StudentEntity, StudentDto> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentMapper(ModelMapper modelMapper,
                         UserRepository userRepository,
                         GroupRepository groupRepository) {
        super(modelMapper);
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(StudentEntity::setId);
            m.skip(StudentEntity::setStudentTasks);
            m.skip(StudentEntity::setUser);
            m.skip(StudentEntity::setGroup);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
            m.skip(StudentDto::setStudentTasks);
            m.skip(StudentDto::setUser);
            m.skip(StudentDto::setGroup);
        }).setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(StudentEntity source, StudentDto destination) {
        destination.setUser(source.getUser().getId());
        destination.setGroup(source.getGroup().getId());

        destination.setStudentTasks(source.getStudentTasks()
                .stream()
                .map(StudentTaskEntity::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(StudentDto source, StudentEntity destination) {
        UserEntity userEntity = userRepository.findById(source.getUser())
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, source.getUser()));
        destination.setUser(userEntity);

        GroupEntity groupEntity = groupRepository.findById(source.getGroup())
                .orElseThrow(() -> new EntityNotFoundException(GroupEntity.class, source.getGroup()));
        Optional.ofNullable(destination.getGroup())
                .ifPresent(current -> current.getStudents().remove(destination));
        destination.setGroup(groupEntity);
        groupEntity.getStudents().add(destination);
    }
}
