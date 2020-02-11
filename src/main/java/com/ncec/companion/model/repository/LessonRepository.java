package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.LessonEntity;
import com.ncec.companion.model.enums.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Integer> {
    Set<LessonEntity> findAllByProfessor_Id(Integer professor_id);

    Set<LessonEntity> findAllBySubject_Id(Integer subject_id);

    Set<LessonEntity> findAllByDay(WeekDay day);

    void deleteAllBySubject_Id(Integer subject_id);
}
