package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Integer> {
    boolean existsByFileKeyAndFileBucket(String fileKey, String fileBucket);

    Optional<AttachmentEntity> findByFileKeyAndFileBucket(String fileKey, String fileBucket);

    void deleteByFileKeyAndFileBucket(String fileKey, String fileBucket);
}