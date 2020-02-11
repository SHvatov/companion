package com.ncec.companion.model.mapper;

import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.ProfessorTaskDto;
import com.ncec.companion.model.entity.*;
import com.ncec.companion.model.enums.SubjectType;
import com.ncec.companion.model.repository.AttachmentRepository;
import com.ncec.companion.model.repository.ProfessorRepository;
import com.ncec.companion.model.repository.SubjectRepository;
import com.ncec.companion.utils.Pair;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProfessorTaskMapper extends AbstractMapper<ProfessorTaskEntity, ProfessorTaskDto> {
	private static final String SPLIT_SYMBOL = ";";
	private static final int SUBJECT_NAME = 0;
	private static final int SUBJECT_TYPE = 1;
	private final ProfessorRepository professorRepository;
	private final AttachmentRepository attachmentRepository;
	private final SubjectRepository subjectRepository;

	@Autowired
	public ProfessorTaskMapper(ModelMapper modelMapper,
	                           ProfessorRepository professorRepository,
	                           AttachmentRepository attachmentRepository,
	                           SubjectRepository subjectRepository) {
		super(modelMapper);
		this.professorRepository = professorRepository;
		this.attachmentRepository = attachmentRepository;
		this.subjectRepository = subjectRepository;
	}

	@PostConstruct
	public void initMapper() {
		mapper.createTypeMap(dtoClass, entityClass).addMappings(m -> {
			m.skip(ProfessorTaskEntity::setId);
			m.skip(ProfessorTaskEntity::setProfessor);
			m.skip(ProfessorTaskEntity::setStudentsTasks);
			m.skip(ProfessorTaskEntity::setAttachments);
			m.skip(ProfessorTaskEntity::setSubject);
		}).setPostConverter(convertToEntity());

		mapper.createTypeMap(entityClass, dtoClass).addMappings(m -> {
			m.skip(ProfessorTaskDto::setProfessor);
			m.skip(ProfessorTaskDto::setStudentsTasks);
			m.skip(ProfessorTaskDto::setAttachments);
			m.skip(ProfessorTaskDto::setAssignees);
			m.skip(ProfessorTaskDto::setSubject);
		}).setPostConverter(convertToDto());
	}

	@Override
	protected void mapSpecificFields(ProfessorTaskEntity source, ProfessorTaskDto destination) {
		destination.setProfessor(source.getProfessor().getId());

		String[] subject = source.getSubject().split(SPLIT_SYMBOL);
		SubjectEntity subjectEntity = subjectRepository
				.findByNameAndType(subject[SUBJECT_NAME].trim(), SubjectType.valueOf(subject[SUBJECT_TYPE].trim()))
				.orElseThrow(() -> new EntityNotFoundException(SubjectEntity.class, new Pair<>(subject[SUBJECT_NAME], subject[SUBJECT_TYPE])));
		destination.setSubject(subjectEntity.getId());

		destination.setAttachments(source.getAttachments()
				.stream()
				.map(AttachmentEntity::getId)
				.collect(Collectors.toSet()));

		destination.setStudentsTasks(source.getStudentsTasks()
				.stream()
				.map(StudentTaskEntity::getId)
				.collect(Collectors.toSet()));
	}

	@Override
	protected void mapSpecificFields(ProfessorTaskDto source, ProfessorTaskEntity destination) {
		ProfessorEntity professorEntity = professorRepository.findById(source.getProfessor())
				.orElseThrow(() -> new EntityNotFoundException(ProfessorEntity.class, source.getProfessor()));
		Optional.ofNullable(destination.getProfessor())
				.ifPresent(current -> current.getProfessorTasks().remove(destination));
		destination.setProfessor(professorEntity);
		professorEntity.getProfessorTasks().add(destination);

		SubjectEntity subjectEntity = subjectRepository.findById(source.getSubject())
				.orElseThrow(() -> new EntityNotFoundException(SubjectEntity.class, source.getSubject()));
		String subject = subjectEntity.getName() +
				SPLIT_SYMBOL +
				subjectEntity.getType().toString();
		destination.setSubject(subject);

		destination.getAttachments().clear();
		source.getAttachments().forEach(id -> {
			AttachmentEntity attachmentEntity = attachmentRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException(id));
			destination.getAttachments().add(attachmentEntity);
		});
	}
}
