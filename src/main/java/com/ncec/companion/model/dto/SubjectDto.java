package com.ncec.companion.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncec.companion.model.enums.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubjectDto extends DataTransferObject {
    @NotBlank
    private String name;

    @NotNull
    private SubjectType type;

    @JsonIgnore
    private Set<Integer> professors = new HashSet<>();
}
