package com.ncec.companion.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncec.companion.model.enums.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventDto extends DataTransferObject {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private ParticipantType participantType;

    @JsonIgnore
    private Set<Integer> visitors = new HashSet<>();

    @JsonIgnore
    private Set<Integer> attachments = new HashSet<>();
}
