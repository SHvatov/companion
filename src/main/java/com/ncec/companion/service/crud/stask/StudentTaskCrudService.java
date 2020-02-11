package com.ncec.companion.service.crud.stask;

import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.service.crud.CrudService;

import java.util.Set;

public interface StudentTaskCrudService extends CrudService<StudentTaskDto> {
    Set<StudentTaskDto> findAllByStudent(Integer studentId);
}
