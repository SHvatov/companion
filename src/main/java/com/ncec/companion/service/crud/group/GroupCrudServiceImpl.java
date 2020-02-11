package com.ncec.companion.service.crud.group;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.entity.GroupEntity;
import com.ncec.companion.model.entity.LessonEntity;
import com.ncec.companion.model.enums.WeekDay;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.GroupRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupCrudServiceImpl
        extends AbstractCrudService<GroupEntity, GroupDto, GroupRepository>
        implements GroupCrudService {
    @Autowired
    public GroupCrudServiceImpl(GroupRepository repository,
                                Mapper<GroupEntity, GroupDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public GroupDto create(GroupDto dto) {
        if (repository.existsByNumber(dto.getNumber())) {
            throw new EntityAlreadyExistsException(GroupEntity.class, dto.getNumber());
        }

        GroupEntity groupEntity = new GroupEntity();
        mapper.map(dto, groupEntity);
        return mapper.map(repository.save(groupEntity));
    }

    @Override
    public GroupDto update(GroupDto dto) {
        GroupEntity groupEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(GroupEntity.class, dto.getId()));

        if (!groupEntity.getNumber().equals(dto.getNumber())
                && repository.existsByNumber(dto.getNumber())) {
            throw new EntityAlreadyExistsException(GroupEntity.class, dto.getNumber());
        }

        mapper.map(dto, groupEntity);
        return mapper.map(repository.save(groupEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(groupEntity -> {
            groupEntity.getLessons().forEach(lessonEntity ->
                    lessonEntity.getGroups().remove(groupEntity)
            );
            groupEntity.getLessons().clear();
            repository.delete(groupEntity);
        });
    }


    @Override
    public GroupDto findByNumber(String number) {
        return mapper.map(repository.findByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException(GroupEntity.class, number)));
    }

    @Override
    public Set<GroupDto> findLeisureGroups(WeekDay weekDay, Integer start, Integer duration) {
        List<GroupEntity> groups = repository.findAll();
        Set<GroupEntity> availableGroups = new HashSet<>();
        for (GroupEntity groupEntity : groups) {
            Set<LessonEntity> groupLessons = groupEntity.getLessons()
                    .stream()
                    .filter(lessonEntity -> lessonEntity.getDay() == weekDay)
                    .collect(Collectors.toSet());

            boolean overlap = false;
            for (LessonEntity lesson : groupLessons) {
                if (TimeUtils.lessonsOverlap(start, duration, lesson.getStartTime(), lesson.getDuration())) {
                    overlap = true;
                    break;
                }
            }

            if (!overlap) {
                availableGroups.add(groupEntity);
            }
        }

        return availableGroups.stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }
}
