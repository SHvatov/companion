package com.ncec.companion.model.dto;

import com.ncec.companion.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationDto extends DataTransferObject {
    private Integer creator;
    private Integer receiver;
    private Integer studentTask;
    private NotificationType type;
}
