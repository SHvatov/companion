package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.entity.GroupEntity;
import com.ncec.companion.model.entity.LessonEntity;
import com.ncec.companion.model.entity.ProfessorEntity;
import com.ncec.companion.model.entity.SubjectEntity;
import com.ncec.companion.model.repository.GroupRepository;
import com.ncec.companion.model.repository.ProfessorRepository;
import com.ncec.companion.model.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LessonMapper extends AbstractMapper<LessonEntity, LessonDto> {
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public LessonMapper(ModelMapper modelMapper,
                        ProfessorRepository professorRepository,
                        SubjectRepository subjectRepository,
                        GroupRepository groupRepository) {
        super(modelMapper);
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(LessonEntity::setId);
            m.skip(LessonEntity::setProfessor);
            m.skip(LessonEntity::setSubject);
            m.skip(LessonEntity::setGroups);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
            m.skip(LessonDto::setProfessor);
            m.skip(LessonDto::setSubject);
            m.skip(LessonDto::setGroups);
        }).setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(LessonEntity source, LessonDto destination) {
        destination.setProfessor(source.getProfessor().getId());
        destination.setSubject(source.getSubject().getId());
        destination.setGroups(source.getGroups()
                .stream()
                .map(GroupEntity::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    protected void mapSpecificFields(LessonDto source, LessonEntity destination) {
        ProfessorEntity professorEntity = professorRepository.findById(source.getProfessor())
                .orElseThrow(() -> new EntityNotFoundException(ProfessorEntity.class, source.getProfessor()));
        Optional.ofNullable(destination.getProfessor())
                .ifPresent(current -> current.getLessons().remove(destination));
        destination.setProfessor(professorEntity);
        professorEntity.getLessons().add(destination);

        SubjectEntity subjectEntity = subjectRepository.findById(source.getSubject())
                .orElseThrow(() -> new EntityNotFoundException(SubjectEntity.class, source.getSubject()));
        destination.setSubject(subjectEntity);

        destination.getGroups().forEach(groupEntity -> groupEntity.getLessons().remove(destination));
        destination.getGroups().clear();
        source.getGroups().forEach(id -> {
            GroupEntity groupEntity = groupRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(GroupEntity.class, id));
            groupEntity.getLessons().add(destination);
            destination.getGroups().add(groupEntity);
        });
    }
}
