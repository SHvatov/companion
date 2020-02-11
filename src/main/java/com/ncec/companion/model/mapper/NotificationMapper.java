package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.NotificationDto;
import com.ncec.companion.model.entity.NotificationEntity;
import com.ncec.companion.model.entity.StudentTaskEntity;
import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.repository.StudentTaskRepository;
import com.ncec.companion.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NotificationMapper extends AbstractMapper<NotificationEntity, NotificationDto> {
	private final UserRepository userRepository;
	private final StudentTaskRepository studentTaskRepository;

	@Autowired
	public NotificationMapper(ModelMapper modelMapper,
	                          UserRepository userRepository,
	                          StudentTaskRepository studentTaskRepository) {
		super(modelMapper);
		this.userRepository = userRepository;
		this.studentTaskRepository = studentTaskRepository;
	}

	@PostConstruct
	public void initMapper() {
		mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
			m.skip(NotificationEntity::setId);
			m.skip(NotificationEntity::setCreator);
			m.skip(NotificationEntity::setReceiver);
			m.skip(NotificationEntity::setStudentTask);
		}).setPostConverter(convertToEntity());

		mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
			m.skip(NotificationDto::setCreator);
			m.skip(NotificationDto::setReceiver);
			m.skip(NotificationDto::setStudentTask);
		}).setPostConverter(convertToDto());
	}

	@Override
	protected void mapSpecificFields(NotificationEntity source, NotificationDto destination) {
		destination.setCreator(source.getCreator().getId());
		destination.setReceiver(source.getReceiver().getId());
		destination.setStudentTask(source.getStudentTask().getId());
	}

	@Override
	protected void mapSpecificFields(NotificationDto source, NotificationEntity destination) {
		UserEntity creator = userRepository.findById(source.getCreator())
				.orElseThrow(() -> new EntityNotFoundException(UserEntity.class, source.getCreator()));
		destination.setCreator(creator);

		UserEntity receiver = userRepository.findById(source.getReceiver())
				.orElseThrow(() -> new EntityNotFoundException(UserEntity.class, source.getCreator()));
		destination.setReceiver(receiver);

		StudentTaskEntity task = studentTaskRepository.findById(source.getStudentTask())
				.orElseThrow(() -> new EntityNotFoundException(StudentTaskEntity.class, source.getCreator()));
		destination.setStudentTask(task);
	}
}
