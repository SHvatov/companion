package com.ncec.companion.service.scheduled.event;

import com.ncec.companion.model.dto.EventDto;
import com.ncec.companion.service.crud.event.EventCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import com.ncec.companion.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventScheduledServiceImpl implements EventScheduledService {
    private static final int DAYS_BEFORE_EVENT = 3;
    private final EventCrudService eventCrudService;
    private final EmailSupportService emailSupportService;


    @Override
    @Scheduled(cron = "0 30 0 * * ?") // at 0:30 am
    public void notifyAboutUpcomingEvents() {
        eventCrudService.findAllUpcomingEvents(DAYS_BEFORE_EVENT)
                .forEach(this::notifyUsers);
    }

    private void notifyUsers(EventDto eventDto) {
        long daysBefore = Math.round(
                (double) (eventDto.getStartDate().getTime() - new Date().getTime()) / TimeUtils.MILLISECONDS_IN_DAY
        );

        Set<String> emails = eventCrudService.getAllVisitorsEmails(eventDto.getId());
        emails.forEach(email ->
                emailSupportService.sendEventNotificationMessage(email, eventDto.getTitle(), daysBefore)
        );
    }
}
