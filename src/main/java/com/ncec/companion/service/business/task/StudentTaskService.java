package com.ncec.companion.service.business.task;

import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.model.enums.TaskAssessment;
import com.ncec.companion.model.enums.TaskStatus;
import org.springframework.web.multipart.MultipartFile;

public interface StudentTaskService {
    StudentTaskDto addAttachment(Integer taskId, MultipartFile file);

    StudentTaskDto removeAttachment(Integer taskId, Integer attachmentId);

    StudentTaskDto updateStatus(Integer taskId, TaskStatus status);

    StudentTaskDto updateAssessment(Integer taskId, TaskAssessment assessment);
}
