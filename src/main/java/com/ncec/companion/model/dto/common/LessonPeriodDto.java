package com.ncec.companion.model.dto.common;

import com.ncec.companion.model.enums.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonPeriodDto {
    @NotNull
    private WeekDay weekDay;

    @NotNull
    private Integer start;

    @NotNull
    @Positive
    private Integer duration;
}
