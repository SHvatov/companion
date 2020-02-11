package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.StudentTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StudentTaskRepository extends JpaRepository<StudentTaskEntity, Integer> {
    boolean existsByStudent_User_IdAndProfessorTask_Id(Integer student_user_id, Integer professorTask_id);

    Set<StudentTaskEntity> findAllByStudent_Id(Integer student_id);
}
