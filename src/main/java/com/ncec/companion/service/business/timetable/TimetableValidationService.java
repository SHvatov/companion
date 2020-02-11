package com.ncec.companion.service.business.timetable;

import com.ncec.companion.model.dto.LessonDto;

public interface TimetableValidationService {
    void validate(LessonDto dto);
}
