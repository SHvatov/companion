package com.ncec.companion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageDto extends DataTransferObject {
    @NotBlank
    private String text;

    @NotNull
    private Date sentDate;

    @NotNull
    private Integer sender;

    @NotNull
    private Integer receiver;

    @NotNull
    private Integer studentTask;
}
