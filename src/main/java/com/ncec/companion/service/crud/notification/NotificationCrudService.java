package com.ncec.companion.service.crud.notification;

import com.ncec.companion.model.dto.NotificationDto;
import com.ncec.companion.service.crud.CrudService;

import java.util.Set;

public interface NotificationCrudService extends CrudService<NotificationDto> {
    Set<NotificationDto> findAllByReceiver(Integer receiverId);

    Set<NotificationDto> findAllByStudentTaskAndReceiver(Integer taskId, Integer receiverId);

    void deleteAllByStudentTask(Integer taskId);

    void deleteAllByStudentTaskAndReceiver(Integer taskId, Integer receiverId);
}
