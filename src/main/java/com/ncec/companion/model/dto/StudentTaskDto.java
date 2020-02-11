package com.ncec.companion.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncec.companion.model.enums.TaskAssessment;
import com.ncec.companion.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentTaskDto extends DataTransferObject {
    @NotNull
    private TaskAssessment assessment;

    @NotNull
    private TaskStatus status;

    @JsonIgnore
    private Integer professorTask;

    @JsonIgnore
    private Integer student;

    @JsonIgnore
    private Set<Integer> attachments = new HashSet<>();

    @JsonIgnore
    private Set<Integer> messages = new HashSet<>();

	@JsonIgnore
	private Set<Integer> notifications = new HashSet<>();
}
