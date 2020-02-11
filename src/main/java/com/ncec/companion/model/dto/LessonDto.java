package com.ncec.companion.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncec.companion.model.enums.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LessonDto extends DataTransferObject {
    @NotNull
    private WeekDay day;

    @NotNull
    @Min(28800) // 8:00
    @Max(72000) // 20:00
    private Integer startTime;

    @NotNull
    @Positive
    @Min(2700) // 45 min
    @Max(5400) // 1:30
    private Integer duration;

    @NotBlank
    @Pattern(regexp = "[0-9]*[\\.\\\\\\/]?[0-9]*")
    private String auditory;

    @NotBlank
    private String location;

    @NotNull
    @JsonProperty(access = WRITE_ONLY)
    private Integer subject;

    @NotNull
    @JsonProperty(access = WRITE_ONLY)
    private Integer professor;

    @NotEmpty
    @JsonProperty(access = WRITE_ONLY)
    private Set<Integer> groups = new HashSet<>();
}
