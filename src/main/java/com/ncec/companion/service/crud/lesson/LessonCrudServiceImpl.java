package com.ncec.companion.service.crud.lesson;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.entity.AbstractDataBaseEntity;
import com.ncec.companion.model.entity.LessonEntity;
import com.ncec.companion.model.enums.WeekDay;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.LessonRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonCrudServiceImpl
        extends AbstractCrudService<LessonEntity, LessonDto, LessonRepository>
        implements LessonCrudService {
    @Autowired
    public LessonCrudServiceImpl(LessonRepository repository,
                                 Mapper<LessonEntity, LessonDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public LessonDto create(LessonDto dto) {
        LessonEntity lessonEntity = new LessonEntity();
        mapper.map(dto, lessonEntity);
        return mapper.map(repository.save(lessonEntity));
    }

    @Override
    public LessonDto update(LessonDto dto) {
        LessonEntity lessonEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(LessonEntity.class, dto.getId()));
        mapper.map(dto, lessonEntity);
        return mapper.map(repository.save(lessonEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Set<LessonDto> findLessonsByGroup(Integer groupId) {
        return repository.findAll()
                .stream()
                .filter(lessonEntity -> {
                    Set<Integer> groupsIds = lessonEntity.getGroups()
                            .stream()
                            .map(AbstractDataBaseEntity::getId)
                            .collect(Collectors.toSet());
                    return groupsIds.contains(groupId);
                })
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LessonDto> findLessonsByProfessor(Integer professorId) {
        return repository.findAllByProfessor_Id(professorId)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LessonDto> findLessonsBySubject(Integer subjectId) {
        return repository.findAllBySubject_Id(subjectId)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LessonDto> findLessonsByWeekDay(WeekDay weekDay) {
        return repository.findAllByDay(weekDay)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAllLessonsWithoutGroups() {
        repository.findAll()
                .stream()
                .filter(lessonEntity -> lessonEntity.getGroups().isEmpty())
                .forEach(repository::delete);
    }

    @Override
    public void deleteAllBySubject(Integer subjectId) {
        repository.deleteAllBySubject_Id(subjectId);
    }
}
