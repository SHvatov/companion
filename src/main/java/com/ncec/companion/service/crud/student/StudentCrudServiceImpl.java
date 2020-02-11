package com.ncec.companion.service.crud.student;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.entity.StudentEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.StudentRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentCrudServiceImpl
        extends AbstractCrudService<StudentEntity, StudentDto, StudentRepository>
        implements StudentCrudService {
    private UserCrudService userCrudService;

    @Autowired
    public StudentCrudServiceImpl(StudentRepository repository,
                                  Mapper<StudentEntity, StudentDto> mapper,
                                  UserCrudService userCrudService) {
        super(repository, mapper);
        this.userCrudService = userCrudService;
    }

    @Override
    public StudentDto create(StudentDto dto) {
        UserDto userDto = userCrudService.findById(dto.getUser());
        if (repository.existsByUserEmail(userDto.getEmail())) {
            throw new EntityAlreadyExistsException(StudentEntity.class, userDto.getEmail());
        }

        StudentEntity studentEntity = new StudentEntity();
        mapper.map(dto, studentEntity);
        return mapper.map(repository.save(studentEntity));
    }

    @Override
    public StudentDto update(StudentDto dto) {
        StudentEntity studentEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(StudentEntity.class, dto.getId()));
        mapper.map(dto, studentEntity);
        return mapper.map(repository.save(studentEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public StudentDto findByUserId(Integer userId) {
        return mapper.map(
                repository.findByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException(StudentEntity.class, userId))
        );
    }
}
