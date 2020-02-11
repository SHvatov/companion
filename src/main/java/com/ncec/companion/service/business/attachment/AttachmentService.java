package com.ncec.companion.service.business.attachment;

import com.ncec.companion.model.dto.AttachmentDto;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentDto upload(MultipartFile file);

    void delete(Integer id);
}
