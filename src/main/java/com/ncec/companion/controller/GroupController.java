package com.ncec.companion.controller;

import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.dto.SubjectDto;
import com.ncec.companion.model.dto.common.LessonPeriodDto;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.service.crud.group.GroupCrudService;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
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
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupCrudService groupCrudService;
    private final LessonCrudService lessonCrudService;
    private final StudentCrudService studentCrudService;
    private final SubjectCrudService subjectCrudService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<GroupDto> create(@RequestBody @Valid GroupDto groupDto) {
        return ResponseEntity.ok(groupCrudService.create(groupDto));
    }

    @GetMapping("/{groupId}/get")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<GroupDto> getById(@PathVariable Integer groupId) {
        return ResponseEntity.ok(groupCrudService.findById(groupId));
    }

    @PutMapping("/{groupId}/update")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<GroupDto> update(@PathVariable Integer groupId,
                                           @RequestBody @Valid GroupDto groupDto) {
        return ResponseEntity.ok(groupCrudService.update(groupDto));
    }

    @DeleteMapping("/{groupId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void delete(@PathVariable Integer groupId) {
        groupCrudService.delete(groupId);
        lessonCrudService.deleteAllLessonsWithoutGroups();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<Set<GroupDto>> getAll() {
        return ResponseEntity.ok(groupCrudService.findAll());
    }

    @GetMapping("/list/by_subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<GroupDto>> getAllBySubject(@PathVariable Integer subjectId) {
        Set<Integer> groups = new HashSet<>();
        lessonCrudService.findLessonsBySubject(subjectId)
                .stream()
                .map(LessonDto::getGroups)
                .forEach(groups::addAll);
        return ResponseEntity.ok(
                groups.stream()
                        .map(groupCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/list/free")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<Set<GroupDto>> getLeisureGroups(@RequestBody @Valid LessonPeriodDto lessonPeriodDto) {
        return ResponseEntity.ok(groupCrudService.findLeisureGroups(
                lessonPeriodDto.getWeekDay(),
                lessonPeriodDto.getStart(),
                lessonPeriodDto.getDuration()
        ));
    }

    @GetMapping("/{groupId}/students")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<StudentDto>> getStudents(@PathVariable Integer groupId) {
        GroupDto groupDto = groupCrudService.findById(groupId);
        return ResponseEntity.ok(
                groupDto.getStudents()
                        .stream()
                        .map(studentCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{groupId}/lessons")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY', 'ROLE_STUDENT')")
    public ResponseEntity<Set<LessonDto>> getLessons(@PathVariable Integer groupId) {
        GroupDto groupDto = groupCrudService.findById(groupId);
        return ResponseEntity.ok(
                groupDto.getLessons()
                        .stream()
                        .map(lessonCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{groupId}/subjects")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_DEANERY', 'ROLE_STUDENT')")
    public ResponseEntity<Set<SubjectDto>> getSubjects(@PathVariable Integer groupId) {
        return ResponseEntity.ok(
                lessonCrudService.findLessonsByGroup(groupId)
                        .stream()
                        .map(LessonDto::getSubject)
                        .map(subjectCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }
}
