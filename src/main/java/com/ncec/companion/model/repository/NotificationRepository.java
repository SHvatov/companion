package com.ncec.companion.model.repository;

import com.ncec.companion.model.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    Set<NotificationEntity> findAllByReceiver_Id(Integer receiver_id);

    Set<NotificationEntity> findAllByStudentTask_IdAndReceiver_Id(Integer student_task_id, Integer receiver_id);

    void deleteAllByStudentTask_Id(Integer studentTask_id);

    void deleteAllByStudentTask_IdAndReceiver_Id(Integer studentTask_id, Integer receiver_id);
}
