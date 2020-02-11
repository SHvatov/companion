package com.ncec.companion.service.crud.notification;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.NotificationDto;
import com.ncec.companion.model.entity.NotificationEntity;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.NotificationRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationCrudServiceImpl
		extends AbstractCrudService<NotificationEntity, NotificationDto, NotificationRepository>
		implements NotificationCrudService {

	@Autowired
	public NotificationCrudServiceImpl(NotificationRepository repository,
	                                   Mapper<NotificationEntity, NotificationDto> mapper) {
		super(repository, mapper);
	}

	@Override
	public NotificationDto create(NotificationDto dto) {
		NotificationEntity notificationEntity = new NotificationEntity();
		mapper.map(dto, notificationEntity);
		return mapper.map(repository.save(notificationEntity));
	}

	@Override
	public NotificationDto update(NotificationDto dto) {
		NotificationEntity notificationEntity = repository.findById(dto.getId())
				.orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class, dto.getId()));
		mapper.map(dto, notificationEntity);
		return mapper.map(repository.save(notificationEntity));
	}

	@Override
	public void delete(Integer id) {
		repository.findById(id).ifPresent(repository::delete);
	}

	@Override
	public Set<NotificationDto> findAllByReceiver(Integer receiverId) {
		return repository.findAllByReceiver_Id(receiverId)
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<NotificationDto> findAllByStudentTaskAndReceiver(Integer taskId, Integer receiverId) {
		return repository.findAllByStudentTask_IdAndReceiver_Id(taskId, receiverId)
				.stream()
				.map(mapper::map)
				.collect(Collectors.toSet());
	}

	@Override
	public void deleteAllByStudentTask(Integer taskId) {
		repository.deleteAllByStudentTask_Id(taskId);
	}

	@Override
	public void deleteAllByStudentTaskAndReceiver(Integer taskId, Integer receiverId) {
		repository.deleteAllByStudentTask_IdAndReceiver_Id(taskId, receiverId);
	}
}
