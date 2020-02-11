package com.ncec.companion.service.crud.student;

import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.service.crud.CrudService;

public interface StudentCrudService extends CrudService<StudentDto> {
    StudentDto findByUserId(Integer userId);
}
