package com.ncec.companion.service.crud.ptask;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.ProfessorTaskDto;
import com.ncec.companion.model.entity.ProfessorTaskEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.ProfessorTaskRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import com.ncec.companion.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessorTaskCrudServiceImpl
		extends AbstractCrudService<ProfessorTaskEntity, ProfessorTaskDto, ProfessorTaskRepository>
		implements ProfessorTaskCrudService {
	@Autowired
	public ProfessorTaskCrudServiceImpl(ProfessorTaskRepository repository,
	                                    Mapper<ProfessorTaskEntity, ProfessorTaskDto> mapper) {
		super(repository, mapper);
	}

	@Override
	public ProfessorTaskDto create(ProfessorTaskDto dto) {
		ProfessorTaskEntity professorTaskEntity = new ProfessorTaskEntity();
		mapper.map(dto, professorTaskEntity);
		return mapper.map(repository.save(professorTaskEntity));
	}

	@Override
	public ProfessorTaskDto update(ProfessorTaskDto dto) {
		ProfessorTaskEntity professorTaskEntity = repository.findById(dto.getId())
				.orElseThrow(() -> new EntityNotFoundException(ProfessorTaskEntity.class, dto.getId()));
		mapper.map(dto, professorTaskEntity);
		return mapper.map(repository.save(professorTaskEntity));
	}

	@Override
	public Set<ProfessorTaskDto> findAllTasksBetween(Date start, Date end) {
		return repository.findAllTasksBetween(start, end)
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<ProfessorTaskDto> findAllUpcomingTasks(int daysBefore) {
		Date periodBegin = new Date();
		Date periodEnd = new Date(periodBegin.getTime() + daysBefore * TimeUtils.MILLISECONDS_IN_DAY);
		return findAllTasksBetween(periodBegin, periodEnd);
	}

	@Override
	public Set<ProfessorTaskDto> findAllByProfessor(Integer professorId) {
		return repository.findAllByProfessor_Id(professorId)
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet());
	}

	@Override
	public void delete(Integer id) {
		repository.findById(id).ifPresent(repository::delete);
	}
}
