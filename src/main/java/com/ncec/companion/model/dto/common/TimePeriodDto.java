package com.ncec.companion.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimePeriodDto {
    @NotNull
    private Date begin;

    @NotNull
    private Date end;
}
