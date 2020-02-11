package com.ncec.companion.service.business.task.impl;

import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.model.dto.NotificationDto;
import com.ncec.companion.model.dto.ProfessorTaskDto;
import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.enums.NotificationType;
import com.ncec.companion.model.enums.TaskAssessment;
import com.ncec.companion.model.enums.TaskStatus;
import com.ncec.companion.service.business.attachment.AttachmentService;
import com.ncec.companion.service.business.task.StudentTaskService;
import com.ncec.companion.service.crud.notification.NotificationCrudService;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.ptask.ProfessorTaskCrudService;
import com.ncec.companion.service.crud.stask.StudentTaskCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StudentTaskServiceImpl implements StudentTaskService {
    private final ProfessorTaskCrudService professorTaskCrudService;
    private final NotificationCrudService notificationCrudService;
    private final StudentTaskCrudService studentTaskCrudService;
    private final ProfessorCrudService professorCrudService;
    private final StudentCrudService studentCrudService;
    private final AttachmentService attachmentService;
    private final UserCrudService userCrudService;

    @Override
    public StudentTaskDto addAttachment(Integer taskId, MultipartFile file) {
        AttachmentDto attachmentDto = attachmentService.upload(file);

        StudentTaskDto current = studentTaskCrudService.findById(taskId);
        current.getAttachments().add(attachmentDto.getId());

        return studentTaskCrudService.update(current);
    }

    @Override
    public StudentTaskDto removeAttachment(Integer taskId, Integer attachmentId) {
        StudentTaskDto current = studentTaskCrudService.findById(taskId);
        current.getAttachments().remove(attachmentId);

        StudentTaskDto updated = studentTaskCrudService.update(current);
        attachmentService.delete(attachmentId);

        return updated;
    }

    @Override
    public StudentTaskDto updateStatus(Integer taskId, TaskStatus status) {
        StudentTaskDto current = studentTaskCrudService.findById(taskId);
        current.setStatus(status);

        StudentTaskDto updated = studentTaskCrudService.update(current);
        generateNotification(updated, NotificationType.TASK_STATUS_UPDATED, true);

        return updated;
    }

    @Override
    public StudentTaskDto updateAssessment(Integer taskId, TaskAssessment assessment) {
        StudentTaskDto current = studentTaskCrudService.findById(taskId);
        current.setAssessment(assessment);

        StudentTaskDto updated = studentTaskCrudService.update(current);
        generateNotification(updated, NotificationType.TASK_ASSESSMENT_UPDATED, false);

        return updated;
    }

    private void generateNotification(StudentTaskDto studentTaskDto, NotificationType type, boolean isStudentCreator) {
        ProfessorTaskDto professorTaskDto = professorTaskCrudService.findById(studentTaskDto.getProfessorTask());
        ProfessorDto professorDto = professorCrudService.findById(professorTaskDto.getProfessor());
        UserDto professorUserDto = userCrudService.findById(professorDto.getUser());

        StudentDto studentDto = studentCrudService.findById(studentTaskDto.getStudent());
        UserDto studentUserDto = userCrudService.findById(studentDto.getUser());

        NotificationDto notificationDto;
        if (isStudentCreator) {
            notificationDto = new NotificationDto(
                    studentUserDto.getId(),
                    professorUserDto.getId(),
                    studentTaskDto.getId(),
                    type
            );
        } else {
            notificationDto = new NotificationDto(
                    professorUserDto.getId(),
                    studentUserDto.getId(),
                    studentTaskDto.getId(),
                    type
            );
        }
        notificationCrudService.create(notificationDto);
    }
}
