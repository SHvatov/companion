package com.ncec.companion.service.mail;

import com.ncec.companion.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@PropertySource("classpath:mail-message.properties")
@RequiredArgsConstructor
public class EmailSupportService {
    private final MailSender mailSender;

    @Value("${mail.server.username}")
    private String serverEmail;

    @Value("${mail.welcome.subject}")
    private String welcomeSubject;

    @Value("${mail.welcome.text}")
    private String welcomeText;

    @Value("${mail.update.password.subject}")
    private String updatePasswordSubject;

    @Value("${mail.update.password.text}")
    private String updatePasswordText;

    @Value("${mail.delete.user.subject}")
    private String deleteUserSubject;

    @Value("${mail.delete.user.text}")
    private String deleteUserText;

    @Value("${mail.new.event.subject}")
    private String newEventSubject;

    @Value("${mail.new.event.text}")
    private String newEventText;

    @Value("${mail.update.event.subject}")
    private String updateEventSubject;

    @Value("${mail.update.event.text}")
    private String updateEventText;

    @Value("${mail.cancel.event.subject}")
    private String canceledEventSubject;

    @Value("${mail.cancel.event.text}")
    private String canceledEventText;

    @Value("${mail.event.notification.subject}")
    private String eventNotificationSubject;

    @Value("${mail.event.notification.text}")
    private String eventNotificationText;

    @Value("${mail.message.notification.subject}")
    private String messageNotificationSubject;

    @Value("${mail.message.notification.text}")
    private String messageNotificationText;

    @Value("${mail.task.deadline.subject}")
    private String taskNotificationSubject;

    @Value("${mail.task.deadline.text}")
    private String taskNotificationText;

    @Async
    public void sendWelcomeMessage(String email, String rawPassword, UserRole role) {
        String message = String.format(
                welcomeText,
                email,
                rawPassword,
                role.toString().substring("ROLE_".length()).toLowerCase()
        );
        send(email, welcomeSubject, message);
    }

    @Async
    public void sendUserPasswordUpdatedMessage(String email, String rawPassword) {
        String message = String.format(
                updatePasswordText,
                email,
                rawPassword
        );
        send(email, updatePasswordSubject, message);
    }

    @Async
    public void sendUserDeletedMessage(String email) {
        String message = String.format(
                deleteUserText,
                email
        );
        send(email, deleteUserSubject, message);
    }

    @Async
    public void sendNewEventMessage(String email, String title) {
        String message = String.format(
                newEventText,
                title
        );
        send(email, newEventSubject, message);
    }

    @Async
    public void sendUpdatedEventMessage(String email, String title) {
        String message = String.format(
                updateEventText,
                title
        );
        send(email, updateEventSubject, message);
    }

    @Async
    public void sendCanceledEventMessage(String email, String title) {
        String message = String.format(
                canceledEventText,
                title
        );
        send(email, canceledEventSubject, message);
    }

    @Async
    public void sendEventNotificationMessage(String email, String title, long daysBefore) {
        String message = String.format(
                eventNotificationText,
                title,
                daysBefore
        );
        send(email, eventNotificationSubject, message);
    }

    @Async
    public void sendNewMessageNotificationMessage(String email, String sender, String message) {
        String text = String.format(
                messageNotificationText,
                sender,
                message
        );
        send(email, messageNotificationSubject, text);
    }

    @Async
    public void sendTaskDeadlineNotificationMessage(String email, String title, Date dueDate) {
        String text = String.format(
                taskNotificationText,
                title,
                dueDate
        );
        send(email, taskNotificationSubject, text);
    }

    private void send(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom(serverEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }
}
