package com.ncec.companion.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupDto extends DataTransferObject {
    @NotBlank
    @Length(min = 5, max = 10)
    @Pattern(regexp = "[0-9]+\\/[0-9]+")
    private String number;

    @JsonIgnore
    private Set<Integer> students = new HashSet<>();

    @JsonIgnore
    private Set<Integer> lessons = new HashSet<>();
}
