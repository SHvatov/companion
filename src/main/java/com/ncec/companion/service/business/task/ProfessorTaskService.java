package com.ncec.companion.service.business.task;

import com.ncec.companion.model.dto.ProfessorTaskDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfessorTaskService {
    ProfessorTaskDto save(ProfessorTaskDto dto);

    ProfessorTaskDto update(ProfessorTaskDto dto);

    ProfessorTaskDto addAttachment(Integer taskId, MultipartFile file);

    ProfessorTaskDto removeAttachment(Integer taskId, Integer attachmentId);
}
