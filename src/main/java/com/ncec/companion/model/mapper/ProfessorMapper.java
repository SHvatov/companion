package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.entity.*;
import com.ncec.companion.model.repository.SubjectRepository;
import com.ncec.companion.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
public class ProfessorMapper extends AbstractMapper<ProfessorEntity, ProfessorDto> {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfessorMapper(ModelMapper modelMapper,
                           SubjectRepository subjectRepository,
                           UserRepository userRepository) {
        super(modelMapper);
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(ProfessorEntity::setId);
            m.skip(ProfessorEntity::setUser);
            m.skip(ProfessorEntity::setLessons);
            m.skip(ProfessorEntity::setSubjects);
            m.skip(ProfessorEntity::setProfessorTasks);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
            m.skip(ProfessorDto::setUser);
            m.skip(ProfessorDto::setLessons);
            m.skip(ProfessorDto::setSubjects);
            m.skip(ProfessorDto::setProfessorTasks);
        }).setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(ProfessorEntity source, ProfessorDto destination) {
        destination.setUser(source.getUser().getId());

        destination.setLessons(source.getLessons()
                .stream()
                .map(LessonEntity::getId)
                .collect(Collectors.toSet()));

        destination.setSubjects(source.getSubjects()
                .stream()
                .map(SubjectEntity::getId)
                .collect(Collectors.toSet()));

        destination.setProfessorTasks(source.getProfessorTasks()
                .stream()
                .map(ProfessorTaskEntity::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(ProfessorDto source, ProfessorEntity destination) {
        UserEntity userEntity = userRepository.findById(source.getUser())
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, source.getUser()));
        destination.setUser(userEntity);

        destination.getSubjects().forEach(subjectEntity -> subjectEntity.getProfessors().remove(destination));
        destination.getSubjects().clear();
        source.getSubjects().forEach(id -> {
            SubjectEntity subjectEntity = subjectRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(SubjectEntity.class, id));

            subjectEntity.getProfessors().add(destination);
            destination.getSubjects().add(subjectEntity);
        });
    }
}
