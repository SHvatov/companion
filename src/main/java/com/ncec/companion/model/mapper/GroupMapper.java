package com.ncec.companion.model.mapper;

import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.entity.GroupEntity;
import com.ncec.companion.model.entity.LessonEntity;
import com.ncec.companion.model.entity.StudentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
public class GroupMapper extends AbstractMapper<GroupEntity, GroupDto> {
    @Autowired
    public GroupMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
            m.skip(GroupEntity::setId);
            m.skip(GroupEntity::setLessons);
            m.skip(GroupEntity::setStudents);
        }).setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
            m.skip(GroupDto::setLessons);
            m.skip(GroupDto::setStudents);
        }).setPostConverter(convertToDto());
    }


    @Override
    protected void mapSpecificFields(GroupEntity source, GroupDto destination) {
        destination.setLessons(source.getLessons()
                .stream()
                .map(LessonEntity::getId)
                .collect(Collectors.toSet()));

        destination.setStudents(source.getStudents()
                .stream()
                .map(StudentEntity::getId)
                .collect(Collectors.toSet()));
    }
}
