package com.ncec.companion.controller;

import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.dto.SubjectDto;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.service.crud.group.GroupCrudService;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.subject.SubjectCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {
	private final ProfessorCrudService professorCrudService;
	private final SubjectCrudService subjectCrudService;
	private final LessonCrudService lessonCrudService;
	private final GroupCrudService groupCrudService;

	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_DEANERY')")
	public ResponseEntity<SubjectDto> create(@RequestBody @Valid SubjectDto subjectDto) {
		return ResponseEntity.ok(subjectCrudService.create(subjectDto));
	}

	@GetMapping("/{subjectId}/get")
	@PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_PROFESSOR', 'ROLE_STUDENT')")
	public ResponseEntity<SubjectDto> getById(@PathVariable Integer subjectId) {
		return ResponseEntity.ok(subjectCrudService.findById(subjectId));
	}

	@PutMapping("/{subjectId}/update")
	@PreAuthorize("hasRole('ROLE_DEANERY')")
	public ResponseEntity<SubjectDto> update(@PathVariable Integer subjectId,
	                                         @RequestBody @Valid SubjectDto subjectDto) {
		return ResponseEntity.ok(subjectCrudService.update(subjectDto));
	}

	@DeleteMapping("/{subjectId}/delete")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ROLE_DEANERY')")
	public void delete(@PathVariable Integer subjectId) {
		lessonCrudService.deleteAllBySubject(subjectId);
		subjectCrudService.delete(subjectId);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_DEANERY')")
	public ResponseEntity<Set<SubjectDto>> getAll() {
		return ResponseEntity.ok(subjectCrudService.findAll());
	}

	@GetMapping("/{subjectId}/groups")
	@PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
	public ResponseEntity<Set<GroupDto>> getGroups(@PathVariable Integer subjectId) {
		Set<Integer> groups = new HashSet<>();
		lessonCrudService.findLessonsBySubject(subjectId)
				.forEach(lessonDto -> groups.addAll(lessonDto.getGroups()));
		return ResponseEntity.ok(
				groups.stream()
						.map(groupCrudService::findById)
						.collect(Collectors.toSet())
		);
	}

	@GetMapping("/{subjectId}/professors")
	@PreAuthorize("hasAnyRole('ROLE_DEANERY')")
	public ResponseEntity<Set<ProfessorDto>> getProfessors(@PathVariable Integer subjectId) {
		return ResponseEntity.ok(
				subjectCrudService.findById(subjectId)
						.getProfessors()
						.stream()
						.map(professorCrudService::findById)
						.collect(Collectors.toSet())
		);
	}
}
