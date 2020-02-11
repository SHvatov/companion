package com.ncec.companion.service.business.message;

import com.ncec.companion.model.dto.MessageDto;
import com.ncec.companion.model.dto.NotificationDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.enums.NotificationType;
import com.ncec.companion.service.crud.message.MessageCrudService;
import com.ncec.companion.service.crud.notification.NotificationCrudService;
import com.ncec.companion.service.crud.stask.StudentTaskCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserCrudService userCrudService;
    private final MessageCrudService messageCrudService;
    private final StudentTaskCrudService studentTaskCrudService;
    private final NotificationCrudService notificationCrudService;
    private final EmailSupportService emailSupportService;

    @Override
    public MessageDto save(MessageDto dto) {
        UserDto sender = userCrudService.findById(dto.getSender());
        UserDto receiver = userCrudService.findById(dto.getReceiver());

        MessageDto created = messageCrudService.create(dto);
        emailSupportService.sendNewMessageNotificationMessage(receiver.getEmail(), sender.getFullName(), dto.getText());

        NotificationDto notificationDto = new NotificationDto(
                sender.getId(),
                receiver.getId(),
                dto.getStudentTask(),
                NotificationType.NEW_MESSAGE
        );

	    notificationCrudService.create(notificationDto);
        return created;
    }

    @Override
    public Set<MessageDto> getAllByTask(Integer taskId) {
        Set<Integer> messages = studentTaskCrudService.findById(taskId).getMessages();
        return messages.stream()
                .map(messageCrudService::findById)
                .collect(Collectors.toSet());
    }
}
