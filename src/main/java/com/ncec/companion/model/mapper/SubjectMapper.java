package com.ncec.companion.model.mapper;

import com.ncec.companion.model.dto.SubjectDto;
import com.ncec.companion.model.entity.ProfessorEntity;
import com.ncec.companion.model.entity.SubjectEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
public class SubjectMapper extends AbstractMapper<SubjectEntity, SubjectDto> {
    @Autowired
    public SubjectMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(SubjectEntity::setId);
            m.skip(SubjectEntity::setProfessors);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> m.skip(SubjectDto::setProfessors))
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(SubjectEntity source, SubjectDto destination) {
        destination.setProfessors(source.getProfessors()
                .stream()
                .map(ProfessorEntity::getId)
                .collect(Collectors.toSet()));
    }
}
