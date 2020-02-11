package com.ncec.companion.controller;

import com.ncec.companion.model.dto.*;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.service.business.task.ProfessorTaskService;
import com.ncec.companion.service.crud.attachment.AttachmentCrudService;
import com.ncec.companion.service.crud.notification.NotificationCrudService;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.ptask.ProfessorTaskCrudService;
import com.ncec.companion.service.crud.stask.StudentTaskCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
import com.ncec.companion.service.crud.subject.SubjectCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ptask")
@RequiredArgsConstructor
public class ProfessorTaskController {
    private final ProfessorTaskCrudService professorTaskCrudService;
    private final StudentTaskCrudService studentTaskCrudService;
    private final AttachmentCrudService attachmentCrudService;
    private final ProfessorCrudService professorCrudService;
    private final ProfessorTaskService professorTaskService;
    private final StudentCrudService studentCrudService;
    private final SubjectCrudService subjectCrudService;
    private final NotificationCrudService notificationCrudService;

    @GetMapping("/{taskId}/get")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY', 'ROLE_STUDENT')")
    public ResponseEntity<ProfessorTaskDto> getById(@PathVariable Integer taskId) {
        return ResponseEntity.ok(professorTaskCrudService.findById(taskId));
    }

    @GetMapping("/{taskId}/assignees")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<StudentDto>> getAssignees(@PathVariable Integer taskId) {
        return ResponseEntity.ok(
                professorTaskCrudService.findById(taskId)
                        .getStudentsTasks()
                        .stream()
                        .map(studentTaskCrudService::findById)
                        .map(StudentTaskDto::getStudent)
                        .map(studentCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<ProfessorTaskDto> create(@RequestBody @Valid ProfessorTaskDto professorTaskDto) {
        return ResponseEntity.ok(professorTaskService.save(professorTaskDto));
    }

    @PutMapping("/{taskId}/update")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<ProfessorTaskDto> update(@PathVariable Integer taskId,
                                                   @RequestBody @Valid ProfessorTaskDto professorTaskDto) {
        return ResponseEntity.ok(professorTaskService.update(professorTaskDto));
    }

    @DeleteMapping("/{taskId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public void delete(@PathVariable Integer taskId) {
        professorTaskCrudService.delete(taskId);
    }


    @PostMapping("/{taskId}/attachment/add")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<ProfessorTaskDto> addAttachment(@PathVariable Integer taskId,
                                                          @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(professorTaskService.addAttachment(taskId, file));
    }

    @DeleteMapping("/{taskId}/attachment/{attachmentId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<ProfessorTaskDto> removeAttachment(@PathVariable Integer taskId,
                                                             @PathVariable Integer attachmentId) {
        return ResponseEntity.ok(professorTaskService.removeAttachment(taskId, attachmentId));
    }

    @GetMapping("{taskId}/attachments")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<AttachmentDto>> getAttachmentsByTask(@PathVariable Integer taskId) {
        ProfessorTaskDto professorTaskDto = professorTaskCrudService.findById(taskId);
        return ResponseEntity.ok(
                professorTaskDto.getAttachments()
                        .stream()
                        .map(attachmentCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{taskId}/notifications/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
    public ResponseEntity<Set<NotificationDto>> getNotificationsByTask(@PathVariable Integer taskId,
                                                                       @PathVariable Integer userId) {
        Set<NotificationDto> notifications = new HashSet<>();
        professorTaskCrudService.findById(taskId)
                .getStudentsTasks()
                .forEach(id -> {
                    StudentTaskDto studentTaskDto = studentTaskCrudService.findById(id);
                    notifications.addAll(notificationCrudService.findAllByStudentTaskAndReceiver(studentTaskDto.getId(), userId));
                });
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("{taskId}/tasks")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<StudentTaskDto>> getStudentsTasksByTask(@PathVariable Integer taskId) {
        return ResponseEntity.ok(
                professorTaskCrudService.findById(taskId).getStudentsTasks()
                        .stream()
                        .map(studentTaskCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("{taskId}/subject")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<SubjectDto> getSubjectByTask(@PathVariable Integer taskId) {
        return ResponseEntity.ok(
                subjectCrudService.findById(
                        professorTaskCrudService.findById(taskId).getSubject()
                )
        );
    }

    @GetMapping("{taskId}/professor")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<ProfessorDto> getProfessorByTask(@PathVariable Integer taskId) {
        return ResponseEntity.ok(
                professorCrudService.findById(
                        professorTaskCrudService.findById(taskId).getProfessor()
                )
        );
    }
}
