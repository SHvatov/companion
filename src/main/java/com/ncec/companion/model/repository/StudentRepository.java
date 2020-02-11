package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    Optional<StudentEntity> findByUserEmail(String email);

    Optional<StudentEntity> findByUserId(Integer userId);

    boolean existsByUserEmail(String email);
}
