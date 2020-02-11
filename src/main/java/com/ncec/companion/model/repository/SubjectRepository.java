package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.SubjectEntity;
import com.ncec.companion.model.enums.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    Optional<SubjectEntity> findByNameAndType(String name, SubjectType type);

    boolean existsByNameAndType(String name, SubjectType type);
}