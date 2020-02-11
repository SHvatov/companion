package com.ncec.companion.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncec.companion.model.enums.TaskPriority;
import com.ncec.companion.model.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfessorTaskDto extends DataTransferObject {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private TaskType type;

    @NotNull
    private Date dueDate;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer subject;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer professor;

    @JsonProperty(access = WRITE_ONLY)
    private Set<Integer> assignees = new HashSet<>();

    @JsonIgnore
    private Set<Integer> studentsTasks = new HashSet<>();

    @JsonIgnore
    private Set<Integer> attachments = new HashSet<>();
}
