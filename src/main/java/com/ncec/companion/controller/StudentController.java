package com.ncec.companion.controller;

import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.service.business.user.StudentManagementService;
import com.ncec.companion.service.crud.group.GroupCrudService;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.stask.StudentTaskCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
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
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentCrudService studentCrudService;
    private final GroupCrudService groupCrudService;
    private final StudentTaskCrudService studentTaskCrudService;
    private final StudentManagementService studentManagementService;
    private final UserCrudService userCrudService;
    private final LessonCrudService lessonCrudService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<StudentDto> create(@RequestBody @Valid StudentDto professorDto) {
        return ResponseEntity.ok(studentManagementService.save(professorDto));
    }

    @GetMapping("/{studentId}/get")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<StudentDto> getById(@PathVariable Integer studentId) {
        StudentDto professorDto = studentCrudService.findById(studentId);
        return ResponseEntity.ok(professorDto);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<Set<StudentDto>> getAll() {
        return ResponseEntity.ok(studentCrudService.findAll());
    }

    @PutMapping("/{studentId}/update")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<StudentDto> update(@PathVariable Integer studentId,
                                             @RequestBody @Valid StudentDto studentDto) {
        studentDto.setUser(studentCrudService.findById(studentId).getUser());
        return ResponseEntity.ok(studentManagementService.update(studentDto));
    }

    @PutMapping("/{studentId}/updatePassword")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void updatePassword(@PathVariable Integer studentId) {
        studentManagementService.updatePassword(studentCrudService.findById(studentId).getUser());
    }

    @DeleteMapping("/{studentId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void delete(@PathVariable Integer studentId) {
        studentManagementService.delete(studentId);
    }

    @GetMapping("/{studentId}/tasks")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<StudentTaskDto>> getTasks(@PathVariable Integer studentId) {
        return ResponseEntity.ok(
                studentCrudService.findById(studentId)
                        .getStudentTasks()
                        .stream()
                        .map(studentTaskCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("/{studentId}/lessons")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<Set<LessonDto>> getLessons(@PathVariable Integer studentId) {
        return ResponseEntity.ok(
                lessonCrudService.findLessonsByGroup(
                        studentCrudService.findById(studentId).getGroup()
                )
        );
    }

    @GetMapping("/{studentId}/group")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Integer studentId) {
        return ResponseEntity.ok(
                groupCrudService.findById(
                        studentCrudService.findById(studentId).getGroup()
                )
        );
    }

    @GetMapping("/{studentId}/user")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable Integer studentId) {
        return ResponseEntity.ok(
                userCrudService.findById(
                        studentCrudService.findById(studentId).getUser()
                )
        );
    }
}
