package com.ncec.companion.controller;

import com.ncec.companion.model.dto.*;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.enums.TaskAssessment;
import com.ncec.companion.model.enums.TaskStatus;
import com.ncec.companion.service.business.message.MessageService;
import com.ncec.companion.service.business.task.StudentTaskService;
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
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stask")
@RequiredArgsConstructor
public class StudentTaskController {
	private final ProfessorTaskCrudService professorTaskCrudService;
	private final NotificationCrudService notificationCrudService;
	private final StudentTaskCrudService studentTaskCrudService;
	private final AttachmentCrudService attachmentCrudService;
	private final ProfessorCrudService professorCrudService;
	private final StudentCrudService studentCrudService;
	private final SubjectCrudService subjectCrudService;
	private final StudentTaskService studentTaskService;
	private final MessageService messageService;

	@GetMapping("/{taskId}/get")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
	public ResponseEntity<StudentTaskDto> getById(@PathVariable Integer taskId) {
		return ResponseEntity.ok(studentTaskCrudService.findById(taskId));
	}

	@PutMapping("/{taskId}/assessment/update")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<StudentTaskDto> updateAssessment(@PathVariable Integer taskId,
	                                                       @Valid @NotNull @RequestParam(name = "assessment") TaskAssessment assessment) {
		return ResponseEntity.ok(studentTaskService.updateAssessment(taskId, assessment));
	}

	@PutMapping("/{taskId}/status/update")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_DEANERY')")
	public ResponseEntity<StudentTaskDto> updateStatus(@PathVariable Integer taskId,
	                                                   @RequestParam(name = "status") TaskStatus status) {
		return ResponseEntity.ok(studentTaskService.updateStatus(taskId, status));
	}

	@GetMapping("/{taskId}/messages")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
	public ResponseEntity<Set<MessageDto>> getMessagesByTask(@PathVariable Integer taskId) {
		return ResponseEntity.ok(messageService.getAllByTask(taskId));
	}

	@GetMapping("/{taskId}/notifications/{userId}")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
	public ResponseEntity<Set<NotificationDto>> getNotificationsByTask(@PathVariable Integer taskId,
	                                                                   @PathVariable Integer userId) {
		return ResponseEntity.ok(notificationCrudService.findAllByStudentTaskAndReceiver(taskId, userId));
	}

	@GetMapping("/{taskId}/notifications/{userId}/discard")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
	public void discardNotifications(@PathVariable Integer taskId, @PathVariable Integer userId) {
		notificationCrudService.deleteAllByStudentTaskAndReceiver(taskId, userId);
	}

	@PostMapping("/{taskId}/message/add")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<MessageDto> addMessage(@PathVariable Integer taskId,
	                                             @RequestBody @Valid MessageDto messageDto) {
		return ResponseEntity.ok(messageService.save(messageDto));
	}

	@GetMapping("{taskId}/attachments")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<Set<AttachmentDto>> getAttachmentsByTask(@PathVariable Integer taskId) {
		StudentTaskDto studentTaskDto = studentTaskCrudService.findById(taskId);
		return ResponseEntity.ok(
				studentTaskDto.getAttachments()
						.stream()
						.map(attachmentCrudService::findById)
						.collect(Collectors.toSet())
		);
	}

	@PostMapping("/{taskId}/attachment/add")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
	public ResponseEntity<StudentTaskDto> addAttachment(@PathVariable Integer taskId,
	                                                    @RequestPart("file") MultipartFile file) {
		return ResponseEntity.ok(studentTaskService.addAttachment(taskId, file));
	}

	@DeleteMapping("/{taskId}/attachment/{attachmentId}/delete")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_STUDENT', 'ROLE_DEANERY')")
	public ResponseEntity<StudentTaskDto> removeAttachment(@PathVariable Integer taskId,
	                                                       @PathVariable Integer attachmentId) {
		return ResponseEntity.ok(studentTaskService.removeAttachment(taskId, attachmentId));
	}

	@GetMapping("{taskId}/professorTask")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<ProfessorTaskDto> getProfessorTasksByTask(@PathVariable Integer taskId) {
		return ResponseEntity.ok(
				professorTaskCrudService.findById(
						studentTaskCrudService.findById(taskId).getProfessorTask()
				)
		);
	}

	@GetMapping("{taskId}/student")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<StudentDto> getStudentByTask(@PathVariable Integer taskId) {
		return ResponseEntity.ok(
				studentCrudService.findById(
						studentTaskCrudService.findById(taskId).getStudent()
				)
		);
	}

	@GetMapping("{taskId}/professor")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<ProfessorDto> getProfessorByTask(@PathVariable Integer taskId) {
		return ResponseEntity.ok(
				professorCrudService.findById(
						professorTaskCrudService.findById(
								studentTaskCrudService.findById(taskId).getProfessorTask()
						).getProfessor()
				)
		);
	}

	@GetMapping("{taskId}/subject")
	@PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<SubjectDto> getSubjectByTask(@PathVariable Integer taskId) {
		return ResponseEntity.ok(
				subjectCrudService.findById(
						professorTaskCrudService.findById(
								studentTaskCrudService.findById(taskId).getProfessorTask()
						).getSubject()
				)
		);
	}
}
