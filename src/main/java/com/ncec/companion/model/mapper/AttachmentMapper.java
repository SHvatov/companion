package com.ncec.companion.model.mapper;

import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.model.entity.AttachmentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AttachmentMapper extends AbstractMapper<AttachmentEntity, AttachmentDto> {

    @Autowired
    public AttachmentMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> m.skip(AttachmentEntity::setId))
                .setPostConverter(convertToEntity());
    }
}
