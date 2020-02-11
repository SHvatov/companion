package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.StudentTaskDto;
import com.ncec.companion.model.entity.*;
import com.ncec.companion.model.repository.AttachmentRepository;
import com.ncec.companion.model.repository.ProfessorTaskRepository;
import com.ncec.companion.model.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StudentTaskMapper extends AbstractMapper<StudentTaskEntity, StudentTaskDto> {
	private final ProfessorTaskRepository professorTaskRepository;
	private final StudentRepository studentRepository;
	private final AttachmentRepository attachmentRepository;

	@Autowired
	public StudentTaskMapper(ModelMapper modelMapper,
	                         ProfessorTaskRepository professorTaskRepository,
	                         StudentRepository studentRepository,
	                         AttachmentRepository attachmentRepository) {
		super(modelMapper);
		this.professorTaskRepository = professorTaskRepository;
		this.studentRepository = studentRepository;
		this.attachmentRepository = attachmentRepository;
	}

	@PostConstruct
	public void initMapper() {
		mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
			m.skip(StudentTaskEntity::setId);
			m.skip(StudentTaskEntity::setAttachments);
			m.skip(StudentTaskEntity::setMessages);
			m.skip(StudentTaskEntity::setNotifications);
			m.skip(StudentTaskEntity::setProfessorTask);
			m.skip(StudentTaskEntity::setStudent);
		}).setPostConverter(convertToEntity());

		mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
			m.skip(StudentTaskDto::setAttachments);
			m.skip(StudentTaskDto::setMessages);
			m.skip(StudentTaskDto::setNotifications);
			m.skip(StudentTaskDto::setProfessorTask);
			m.skip(StudentTaskDto::setStudent);
		}).setPostConverter(convertToDto());
	}

	@Override
	protected void mapSpecificFields(StudentTaskEntity source, StudentTaskDto destination) {
		destination.setStudent(source.getStudent().getId());
		destination.setProfessorTask(source.getProfessorTask().getId());

		destination.setMessages(source.getMessages()
				.stream()
				.map(MessageEntity::getId)
				.collect(Collectors.toSet()));

		destination.setNotifications(source.getNotifications()
				.stream()
				.map(NotificationEntity::getId)
				.collect(Collectors.toSet()));

		destination.setAttachments(source.getAttachments()
				.stream()
				.map(AttachmentEntity::getId)
				.collect(Collectors.toSet()));
	}

	@Override
	protected void mapSpecificFields(StudentTaskDto source, StudentTaskEntity destination) {
		StudentEntity studentEntity = studentRepository.findById(source.getStudent())
				.orElseThrow(() -> new EntityNotFoundException(StudentEntity.class, source.getStudent()));
		Optional.ofNullable(destination.getStudent())
				.ifPresent(current -> current.getStudentTasks().remove(destination));
		destination.setStudent(studentEntity);
		studentEntity.getStudentTasks().add(destination);

		ProfessorTaskEntity professorTaskEntity = professorTaskRepository.findById(source.getProfessorTask())
				.orElseThrow(() -> new EntityNotFoundException(ProfessorTaskEntity.class, source.getProfessorTask()));
		Optional.ofNullable(destination.getProfessorTask())
				.ifPresent(current -> current.getStudentsTasks().remove(destination));
		destination.setProfessorTask(professorTaskEntity);
		professorTaskEntity.getStudentsTasks().add(destination);

		destination.getAttachments().clear();
		source.getAttachments().forEach(id -> {
			AttachmentEntity attachmentEntity = attachmentRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException(AttachmentEntity.class, id));
			destination.getAttachments().add(attachmentEntity);
		});
	}
}
