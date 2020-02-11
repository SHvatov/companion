package com.ncec.companion.service.crud.ptask;

import com.ncec.companion.model.dto.ProfessorTaskDto;
import com.ncec.companion.service.crud.CrudService;

import java.util.Date;
import java.util.Set;

public interface ProfessorTaskCrudService extends CrudService<ProfessorTaskDto> {
    Set<ProfessorTaskDto> findAllTasksBetween(Date start, Date end);

    Set<ProfessorTaskDto> findAllUpcomingTasks(int daysBefore);

    Set<ProfessorTaskDto> findAllByProfessor(Integer professorId);
}
