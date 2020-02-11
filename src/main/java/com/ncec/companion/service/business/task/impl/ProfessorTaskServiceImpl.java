package com.ncec.companion.service.business.task.impl;

import com.ncec.companion.exception.BusinessLogicException;
import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.dto.ProfessorTaskDto;
import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.enums.TaskAssessment;
import com.ncec.companion.model.enums.TaskStatus;
import com.ncec.companion.service.business.attachment.AttachmentService;
import com.ncec.companion.service.business.task.ProfessorTaskService;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.ptask.ProfessorTaskCrudService;
import com.ncec.companion.service.crud.stask.StudentTaskCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorTaskServiceImpl implements ProfessorTaskService {
	private final ProfessorTaskCrudService professorTaskCrudService;
	private final StudentTaskCrudService studentTaskCrudService;
	private final ProfessorCrudService professorCrudService;
	private final StudentCrudService studentCrudService;
	private final LessonCrudService lessonCrudService;
	private final AttachmentService attachmentService;

	@Override
	public ProfessorTaskDto save(ProfessorTaskDto dto) {
		validate(dto);
		Integer professorTaskId = professorTaskCrudService.create(dto).getId();

		for (Integer id : dto.getAssignees()) {
			StudentTaskDto studentTaskDto = formStudentTask(id, professorTaskId);

			try {
				studentTaskCrudService.create(studentTaskDto);
			} catch (EntityNotFoundException | EntityAlreadyExistsException exception) {
				professorTaskCrudService.delete(professorTaskId);
				throw exception;
			}
		}
		return professorTaskCrudService.findById(professorTaskId);
	}

	@Override
	public ProfessorTaskDto update(ProfessorTaskDto dto) {
		ProfessorTaskDto current = professorTaskCrudService.findById(dto.getId());

		current.setTitle(dto.getTitle());
		current.setDescription(dto.getDescription());
		current.setDueDate(dto.getDueDate());
		current.setPriority(dto.getPriority());

		validate(current);
		return professorTaskCrudService.update(current);
	}

	@Override
	public ProfessorTaskDto addAttachment(Integer taskId, MultipartFile file) {
		AttachmentDto attachmentDto = attachmentService.upload(file);

		ProfessorTaskDto current = professorTaskCrudService.findById(taskId);
		current.getAttachments().add(attachmentDto.getId());

		return professorTaskCrudService.update(current);
	}

	@Override
	public ProfessorTaskDto removeAttachment(Integer taskId, Integer attachmentId) {
		ProfessorTaskDto current = professorTaskCrudService.findById(taskId);
		current.getAttachments().remove(attachmentId);

		ProfessorTaskDto updated = professorTaskCrudService.update(current);
		attachmentService.delete(attachmentId);
		return updated;
	}

	private void validate(ProfessorTaskDto dto) {
		validateAssignees(dto.getAssignees(), dto.getSubject());
		validateProfessor(dto.getProfessor(), dto.getSubject());
		if (dto.getDueDate().before(new Date())) {
			throw new BusinessLogicException("Due date cannot be before current moment of the time");
		}
	}

	private void validateAssignees(Set<Integer> assignees, Integer subjectId) {
		if (assignees.isEmpty()) {
			throw new BusinessLogicException("List of the assignees must not be empty!");
		}

		Set<Integer> groups = assignees.stream()
				.map(studentCrudService::findById)
				.map(StudentDto::getGroup)
				.collect(Collectors.toSet());
		for (Integer group : groups) {
			Set<Integer> subjects = lessonCrudService.findLessonsByGroup(group)
					.stream()
					.map(LessonDto::getSubject)
					.collect(Collectors.toSet());

			if (!subjects.contains(subjectId)) {
				throw new BusinessLogicException("This subject is not taught to all of the students, that are presented in the list");
			}
		}
	}

	private void validateProfessor(Integer professorId, Integer subjectId) {
		ProfessorDto professorDto = professorCrudService.findById(professorId);
		if (!professorDto.getSubjects().contains(subjectId)) {
			throw new BusinessLogicException("This subject is not taught by the requested professor");
		}
	}

	private StudentTaskDto formStudentTask(Integer studentId, Integer professorTaskId) {
		StudentTaskDto studentTaskDto = new StudentTaskDto();
		studentTaskDto.setStudent(studentId);
		studentTaskDto.setProfessorTask(professorTaskId);
		studentTaskDto.setStatus(TaskStatus.NOT_COMPLETED);
		studentTaskDto.setAssessment(TaskAssessment.NOT_DONE);
		return studentTaskDto;
	}
}
