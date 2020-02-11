package com.ncec.companion.service.crud.professor;

import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.service.crud.CrudService;

public interface ProfessorCrudService extends CrudService<ProfessorDto> {
    ProfessorDto findByUserId(Integer userId);
}
