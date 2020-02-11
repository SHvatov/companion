package com.ncec.companion.service.business.event;

import com.ncec.companion.exception.BusinessLogicException;
import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.model.dto.EventDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.enums.ParticipantType;
import com.ncec.companion.model.enums.UserRole;
import com.ncec.companion.service.business.attachment.AttachmentService;
import com.ncec.companion.service.crud.event.EventCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import com.ncec.companion.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventCrudService eventCrudService;
	private final UserCrudService userCrudService;
	private final AttachmentService attachmentService;
	private final EmailSupportService emailSupportService;

	@Override
	public EventDto save(EventDto dto) {
		TimeUtils.validateTimePeriod(dto.getStartDate(), dto.getEndDate());
		EventDto saved = eventCrudService.create(dto);

		Set<String> emails;
		if (dto.getParticipantType() == ParticipantType.ALL) {
			emails = userCrudService.findAll()
					.stream()
					.map(UserDto::getEmail)
					.collect(Collectors.toSet());
		} else {
			UserRole participantsRole =
					dto.getParticipantType() == ParticipantType.PROFESSORS
							? UserRole.ROLE_PROFESSOR
							: UserRole.ROLE_STUDENT;
			emails = userCrudService.findAllWithRole(participantsRole)
					.stream()
					.map(UserDto::getEmail)
					.collect(Collectors.toSet());
		}

		emails.forEach(email -> emailSupportService.sendNewEventMessage(email, dto.getTitle()));
		return saved;
	}

	@Override
	public EventDto update(EventDto dto) {
		EventDto current = eventCrudService.findById(dto.getId());

		if (!current.getStartDate().equals(dto.getStartDate())
				|| !current.getEndDate().equals(dto.getEndDate())) {
			TimeUtils.validateTimePeriod(dto.getStartDate(), dto.getEndDate());
		}

		current.setTitle(dto.getTitle());
		current.setDescription(dto.getDescription());
		current.setStartDate(dto.getStartDate());
		current.setEndDate(dto.getEndDate());
		EventDto updated = eventCrudService.update(current);

		Set<String> emails = eventCrudService.getAllVisitorsEmails(dto.getId());
		emails.forEach(email -> emailSupportService.sendUpdatedEventMessage(email, dto.getTitle()));

		return updated;
	}

	@Override
	public EventDto addParticipant(Integer eventId, Integer participantId) {
		EventDto current = eventCrudService.findById(eventId);
		if (new Date().after(current.getStartDate())) {
			throw new BusinessLogicException("Cannot participate in this event - out of date");
		}

		current.getVisitors().add(participantId);
		return eventCrudService.update(current);
	}

	@Override
	public EventDto removeParticipant(Integer eventId, Integer participantId) {
		EventDto current = eventCrudService.findById(eventId);
		if (new Date().after(current.getStartDate())) {
			throw new BusinessLogicException("Cannot modify this event - out of date");
		}

		current.getVisitors().remove(participantId);
		return eventCrudService.update(current);
	}

	@Override
	public EventDto addAttachment(Integer eventId, MultipartFile file) {
		AttachmentDto attachmentDto = attachmentService.upload(file);

		EventDto current = eventCrudService.findById(eventId);
		current.getAttachments().add(attachmentDto.getId());
		return eventCrudService.update(current);
	}

	@Override
	public EventDto removeAttachment(Integer eventId, Integer attachmentId) {
		EventDto current = eventCrudService.findById(eventId);
		current.getAttachments().remove(attachmentId);
		EventDto updated = eventCrudService.update(current);

		attachmentService.delete(attachmentId);
		return updated;
	}

	@Override
	public void delete(Integer id) {
		if (eventCrudService.existsById(id)) {
			EventDto eventDto = eventCrudService.findById(id);
			eventCrudService.getAllVisitorsEmails(id)
					.forEach(email -> emailSupportService.sendCanceledEventMessage(email, eventDto.getTitle()));
			eventCrudService.delete(id);
		}
	}
}
