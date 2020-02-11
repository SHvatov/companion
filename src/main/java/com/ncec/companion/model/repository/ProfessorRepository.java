package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Integer> {
	Optional<ProfessorEntity> findByUserEmail(String email);

	Optional<ProfessorEntity> findByUserId(Integer userId);

	boolean existsByUserEmail(String email);
}
