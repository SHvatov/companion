package com.ncec.companion.service.business.attachment;

import com.ncec.companion.model.dto.AttachmentDto;
import com.ncec.companion.service.amazon.AmazonS3Service;
import com.ncec.companion.service.crud.attachment.AttachmentCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentCrudService attachmentCrudService;
    private final AmazonS3Service amazonS3Service;

    @Override
    public AttachmentDto upload(MultipartFile file) {
        AttachmentDto attachment = amazonS3Service.uploadFile(file);
        return attachmentCrudService.create(attachment);
    }

    @Override
    public void delete(Integer id) {
        AttachmentDto attachment = attachmentCrudService.findById(id);
        amazonS3Service.deleteFile(attachment.getFileKey(), attachment.getFileBucket());
        attachmentCrudService.delete(id);
    }
}
