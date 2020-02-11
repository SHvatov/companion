package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.ProfessorTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface ProfessorTaskRepository extends JpaRepository<ProfessorTaskEntity, Integer> {
    @Query("select e from ProfessorTaskEntity e where e.dueDate <= :endDate and e.dueDate >= :startDate")
    Set<ProfessorTaskEntity> findAllTasksBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Set<ProfessorTaskEntity> findAllByProfessor_Id(Integer professor_id);
}
