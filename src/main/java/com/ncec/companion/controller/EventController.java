package com.ncec.companion.controller;

import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.model.dto.EventDto;
import com.ncec.companion.model.enums.ParticipantType;
import com.ncec.companion.model.enums.UserRole;
import com.ncec.companion.service.business.event.EventService;
import com.ncec.companion.service.crud.attachment.AttachmentCrudService;
import com.ncec.companion.service.crud.event.EventCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final AttachmentCrudService attachmentCrudService;
    private final EventCrudService eventCrudService;
    private final UserCrudService userCrudService;
    private final EventService eventService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<EventDto> create(@RequestBody @Valid EventDto eventDto) {
        return ResponseEntity.ok(eventService.save(eventDto));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY')")
    public ResponseEntity<Set<EventDto>> getAll() {
        return ResponseEntity.ok(eventCrudService.findAll());
    }

    @GetMapping("/{id}/get")
    @PreAuthorize("hasAnyRole('ROLE_DEANERY', 'ROLE_STUDENT', 'ROLE_PROFESSOR')")
    public ResponseEntity<EventDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventCrudService.findById(id));
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<EventDto> update(@PathVariable Integer id,
                                           @RequestBody @Valid EventDto eventDto) {
        return ResponseEntity.ok(eventService.update(eventDto));
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public void delete(@PathVariable Integer id) {
        eventService.delete(id);
    }

    @GetMapping("{eventId}/is_participating/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Boolean> isParticipating(@PathVariable Integer eventId,
                                                   @PathVariable Integer userId) {
        return ResponseEntity.ok(eventCrudService.findById(eventId).getVisitors().contains(userId));
    }

    @GetMapping("{userId}/all")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<EventDto>> getAllVisiting(@PathVariable Integer userId) {
        return ResponseEntity.ok(eventCrudService.findAllByVisitor(userId));
    }

    @GetMapping("{userId}/suitable")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<EventDto>> getAllSuitable(@PathVariable Integer userId) {
        ParticipantType type =
                userCrudService.findById(userId).getRole() == UserRole.ROLE_PROFESSOR
                        ? ParticipantType.PROFESSORS
                        : ParticipantType.STUDENTS;
        return ResponseEntity.ok(eventCrudService.findAllSuitable(type));
    }

	@PutMapping("/{eventId}/{userId}/participate")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<EventDto> participate(@PathVariable Integer eventId,
                                                @PathVariable Integer userId) {
        return ResponseEntity.ok(eventService.addParticipant(eventId, userId));
    }

	@PutMapping("/{eventId}/{userId}/refuse")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<EventDto> refuse(@PathVariable Integer eventId,
                                           @PathVariable Integer userId) {
        return ResponseEntity.ok(eventService.removeParticipant(eventId, userId));
    }

    @GetMapping("{eventId}/attachments")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Set<AttachmentDto>> getAttachmentsByEvent(@PathVariable Integer eventId) {
        EventDto eventDto = eventCrudService.findById(eventId);
        return ResponseEntity.ok(
                eventDto.getAttachments()
                        .stream()
                        .map(attachmentCrudService::findById)
                        .collect(Collectors.toSet())
        );
    }

    @GetMapping("{eventId}/visitors")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_PROFESSOR', 'ROLE_DEANERY')")
    public ResponseEntity<Integer> getVisitorsByEvent(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventCrudService.findById(eventId).getVisitors().size());
    }

    @PostMapping("/{id}/attachment/add")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<EventDto> addAttachment(@PathVariable Integer id,
                                                  @RequestPart(value = "file") MultipartFile file) {
        return ResponseEntity.ok(eventService.addAttachment(id, file));
    }

    @DeleteMapping("/{eventId}/attachment/{attachmentId}/delete")
    @PreAuthorize("hasRole('ROLE_DEANERY')")
    public ResponseEntity<EventDto> removeAttachment(@PathVariable Integer eventId,
                                                     @PathVariable Integer attachmentId) {
        return ResponseEntity.ok(eventService.removeAttachment(eventId, attachmentId));
    }
}
