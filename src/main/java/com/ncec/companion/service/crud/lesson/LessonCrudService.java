package com.ncec.companion.service.crud.lesson;

import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.enums.WeekDay;
import com.ncec.companion.service.crud.CrudService;

import java.util.Set;

public interface LessonCrudService extends CrudService<LessonDto> {
    Set<LessonDto> findLessonsByGroup(Integer groupId);

    Set<LessonDto> findLessonsByProfessor(Integer professorId);

    Set<LessonDto> findLessonsBySubject(Integer subjectId);

    Set<LessonDto> findLessonsByWeekDay(WeekDay weekDay);

    void deleteAllLessonsWithoutGroups();

    void deleteAllBySubject(Integer subjectId);
}
