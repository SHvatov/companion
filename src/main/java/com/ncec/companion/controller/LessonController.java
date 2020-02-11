package com.ncec.companion.controller;

import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.dto.SubjectDto;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.service.business.timetable.TimetableValidationService;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonCrudService lessonCrudService;
    private final GroupCrudService groupCrudService;
    private final SubjectCrudService subjectCrudService;
    private final ProfessorCrudService professorCrudService;
    private final TimetableValidationService validationService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<LessonDto> create(@RequestBody @Valid LessonDto lessonDto) {
        validationService.validate(lessonDto);
        return ResponseEntity.ok(lessonCrudService.create(lessonDto));
    }

    @GetMapping("/{lessonId}/get")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<LessonDto> getById(@PathVariable Integer lessonId) {
        return ResponseEntity.ok(lessonCrudService.findById(lessonId));
    }

    @PutMapping("/{lessonId}/update")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<LessonDto> update(@PathVariable Integer lessonId,
                                            @RequestBody @Valid LessonDto lessonDto) {
        validationService.validate(lessonDto);
        return ResponseEntity.ok(lessonCrudService.update(lessonDto));
    }

    @DeleteMapping("/{lessonId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void delete(@PathVariable Integer lessonId) {
        lessonCrudService.delete(lessonId);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<Set<LessonDto>> getAll() {
        return ResponseEntity.ok(lessonCrudService.findAll());
    }

    @GetMapping("{lessonId}/groups")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<GroupDto>> getGroups(@PathVariable Integer lessonId) {
        return ResponseEntity.ok(
                lessonCrudService.findById(lessonId)
                        .getGroups()
                        .stream()
                        .map(groupCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("{lessonId}/professor")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<ProfessorDto> getProfessor(@PathVariable Integer lessonId) {
        return ResponseEntity.ok(
                professorCrudService.findById(
                        lessonCrudService.findById(lessonId).getProfessor()
                )
        );
    }

    @GetMapping("{lessonId}/subject")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<SubjectDto> getSubject(@PathVariable Integer lessonId) {
        return ResponseEntity.ok(
                subjectCrudService.findById(
                        lessonCrudService.findById(lessonId).getSubject()
                )
        );
    }
}
