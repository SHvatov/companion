package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {
    boolean existsByNumber(String number);

    Optional<GroupEntity> findByNumber(String number);
}
