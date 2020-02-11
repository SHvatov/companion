package com.ncec.companion.service.crud.stask;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.model.entity.StudentTaskEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.StudentTaskRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentTaskCrudServiceImpl
        extends AbstractCrudService<StudentTaskEntity, StudentTaskDto, StudentTaskRepository>
        implements StudentTaskCrudService {
    @Autowired
    public StudentTaskCrudServiceImpl(StudentTaskRepository repository,
                                      Mapper<StudentTaskEntity, StudentTaskDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public StudentTaskDto create(StudentTaskDto dto) {
        if (repository.existsByStudent_User_IdAndProfessorTask_Id(dto.getStudent(), dto.getProfessorTask())) {
            throw new EntityAlreadyExistsException(
                    StudentTaskEntity.class,
                    new Pair<>(dto.getStudent(), dto.getProfessorTask())
            );
        }

        StudentTaskEntity studentTaskEntity = new StudentTaskEntity();
        mapper.map(dto, studentTaskEntity);
        return mapper.map(repository.save(studentTaskEntity));
    }

    @Override
    public StudentTaskDto update(StudentTaskDto dto) {
        StudentTaskEntity studentTaskEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(StudentTaskEntity.class, dto.getId()));
        mapper.map(dto, studentTaskEntity);
        return mapper.map(repository.save(studentTaskEntity));
    }

    @Override
    public Set<StudentTaskDto> findAllByStudent(Integer studentId) {
        return repository.findAllByStudent_Id(studentId)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
