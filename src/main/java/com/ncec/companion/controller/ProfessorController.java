package com.ncec.companion.controller;

import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.dto.ProfessorTaskDto;
import com.ncec.companion.model.dto.SubjectDto;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.service.business.user.ProfessorManagementService;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.ptask.ProfessorTaskCrudService;
import com.ncec.companion.service.crud.subject.SubjectCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorCrudService professorCrudService;
    private final ProfessorManagementService professorManagementService;
    private final LessonCrudService lessonCrudService;
    private final SubjectCrudService subjectCrudService;
    private final ProfessorTaskCrudService professorTaskCrudService;
    private final UserCrudService userCrudService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<ProfessorDto> create(@RequestBody @Valid ProfessorDto professorDto) {
        return ResponseEntity.ok(professorManagementService.save(professorDto));
    }

    @GetMapping("/{professorId}/get")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<ProfessorDto> getById(@PathVariable Integer professorId) {
        return ResponseEntity.ok(professorCrudService.findById(professorId));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<ProfessorDto>> getAll() {
        return ResponseEntity.ok(professorCrudService.findAll());
    }

    @PutMapping("/{professorId}/update")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<ProfessorDto> update(@PathVariable Integer professorId,
                                               @RequestBody @Valid ProfessorDto professorDto) {
        professorDto.setUser(professorCrudService.findById(professorId).getUser());
        return ResponseEntity.ok(professorManagementService.update(professorDto));
    }

    @PutMapping("/{professorId}/updatePassword")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void updatePassword(@PathVariable Integer professorId) {
        professorManagementService.updatePassword(professorCrudService.findById(professorId).getUser());
    }

    @DeleteMapping("/{professorId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void delete(@PathVariable Integer professorId) {
        professorManagementService.delete(professorId);
    }

    @GetMapping("/{professorId}/subjects")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<SubjectDto>> getSubjects(@PathVariable Integer professorId) {
        return ResponseEntity.ok(
                professorCrudService.findById(professorId)
                        .getSubjects()
                        .stream()
                        .map(subjectCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{professorId}/lessons")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<LessonDto>> getLessons(@PathVariable Integer professorId) {
        return ResponseEntity.ok(
                professorCrudService.findById(professorId)
                        .getLessons()
                        .stream()
                        .map(lessonCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{professorId}/tasks")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<ProfessorTaskDto>> getTasks(@PathVariable Integer professorId) {
        return ResponseEntity.ok(
                professorCrudService.findById(professorId)
                        .getProfessorTasks()
                        .stream()
                        .map(professorTaskCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{professorId}/user")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_PROFESSOR', 'ROLE_STUDENT')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable Integer professorId) {
        return ResponseEntity.ok(
                userCrudService.findById(
                        professorCrudService.findById(professorId).getUser()
                )
        );
    }
}
