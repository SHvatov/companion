package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Set<UserEntity> findAllByRole(UserRole role);
}
