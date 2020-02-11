package com.ncec.companion.service.crud.professor;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.entity.ProfessorEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.ProfessorRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfessorCrudServiceImpl
        extends AbstractCrudService<ProfessorEntity, ProfessorDto, ProfessorRepository>
        implements ProfessorCrudService {
    private UserCrudService userCrudService;

    @Autowired
    public ProfessorCrudServiceImpl(ProfessorRepository repository,
                                    Mapper<ProfessorEntity, ProfessorDto> mapper,
                                    UserCrudService userCrudService) {
        super(repository, mapper);
        this.userCrudService = userCrudService;
    }

    @Override
    public ProfessorDto create(ProfessorDto dto) {
        UserDto userDto = userCrudService.findById(dto.getUser());
        if (repository.existsByUserEmail(userDto.getEmail())) {
            throw new EntityAlreadyExistsException(ProfessorEntity.class, userDto.getEmail());
        }

        ProfessorEntity professorEntity = new ProfessorEntity();
        mapper.map(dto, professorEntity);
        return mapper.map(repository.save(professorEntity));
    }

    @Override
    public ProfessorDto update(ProfessorDto dto) {
        ProfessorEntity professorEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(ProfessorEntity.class, dto.getId()));
        mapper.map(dto, professorEntity);
        return mapper.map(repository.save(professorEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public ProfessorDto findByUserId(Integer userId) {
        return mapper.map(
                repository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException(ProfessorEntity.class, userId))
        );
    }
}
