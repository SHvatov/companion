package com.ncec.companion.service.crud.attachment;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.model.entity.AttachmentEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.AttachmentRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttachmentCrudServiceImpl
        extends AbstractCrudService<AttachmentEntity, AttachmentDto, AttachmentRepository>
        implements AttachmentCrudService {
    @Autowired
    public AttachmentCrudServiceImpl(AttachmentRepository repository,
                                     Mapper<AttachmentEntity, AttachmentDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public AttachmentDto create(AttachmentDto dto) {
        if (repository.existsByFileKeyAndFileBucket(dto.getFileKey(), dto.getFileBucket())) {
            throw new EntityAlreadyExistsException(
                    AttachmentEntity.class,
                    new Pair<>(dto.getFileKey(), dto.getFileBucket())
            );
        }

        AttachmentEntity attachmentEntity = new AttachmentEntity();
        mapper.map(dto, attachmentEntity);
        return mapper.map(repository.save(attachmentEntity));
    }

    @Override
    public AttachmentDto update(AttachmentDto dto) {
        AttachmentEntity attachmentEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(AttachmentEntity.class, dto.getId()));
        mapper.map(dto, attachmentEntity);
        return mapper.map(repository.save(attachmentEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
