package com.ncec.companion.service.crud.subject;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.SubjectDto;
import com.ncec.companion.model.entity.SubjectEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.SubjectRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubjectCrudServiceImpl
        extends AbstractCrudService<SubjectEntity, SubjectDto, SubjectRepository>
        implements SubjectCrudService {
    @Autowired
    public SubjectCrudServiceImpl(SubjectRepository repository,
                                  Mapper<SubjectEntity, SubjectDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public SubjectDto create(SubjectDto dto) {
        if (repository.existsByNameAndType(dto.getName(), dto.getType())) {
            throw new EntityAlreadyExistsException(SubjectEntity.class, new Pair<>(dto.getName(), dto.getType()));
        }

        SubjectEntity subjectEntity = new SubjectEntity();
        mapper.map(dto, subjectEntity);
        return mapper.map(repository.save(subjectEntity));
    }

    @Override
    public SubjectDto update(SubjectDto dto) {
        SubjectEntity subjectEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(SubjectEntity.class, dto.getId()));

        if ((!subjectEntity.getName().equals(dto.getName()) || subjectEntity.getType() != dto.getType())
                && repository.existsByNameAndType(dto.getName(), dto.getType())) {
            throw new EntityAlreadyExistsException(SubjectEntity.class, new Pair<>(dto.getName(), dto.getType()));
        }

        mapper.map(dto, subjectEntity);
        return mapper.map(repository.save(subjectEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(subjectEntity -> {
            subjectEntity.getProfessors().forEach(
                    professorEntity -> professorEntity.getSubjects().remove(subjectEntity)
            );
            subjectEntity.getProfessors().clear();
            repository.delete(subjectEntity);
        });
    }
}
