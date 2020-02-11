package com.ncec.companion.service.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ncec.companion.exception.BusinessLogicException;
import com.ncec.companion.exception.InnerLogicException;
import com.ncec.companion.model.dto.AttachmentDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:amazons3.properties")
@RequiredArgsConstructor
public class AmazonS3Service {
    private final AmazonS3 s3client;

    @Value("${aws.endpoint-url}")
    private String endpointUrl;

    @Value("${aws.access-key}")
    private String awsAccessKey;

    @Value("${aws.secret-key}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.max-file-size}")
    private Integer maxFileSize;

    @Value("${aws.mex-file-name-len}")
    private Integer maxFileNameLen;

    @Value("${aws.allowed.ext}")
    private String[] allowedExtensions;


    public AttachmentDto uploadFile(MultipartFile multipartFile) {
        try {
            validateFile(multipartFile);
            String fileKey = uploadToAmazon(multipartFile);
            return new AttachmentDto(fileKey, bucketName, endpointUrl);
        } catch (IOException ex) {
            throw new InnerLogicException(AmazonS3Service.class, "uploadFile", ex);
        }
    }

    public void deleteFile(String fileKey, String fileBucket) {
        s3client.deleteObject(new DeleteObjectRequest(fileBucket, fileKey));
    }

    private void validateFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile)) {
            throw new BusinessLogicException("Uploading file is null");
        }

        if (multipartFile.isEmpty() || multipartFile.getSize() >= maxFileSize) {
            throw new BusinessLogicException("Uploading file's size must be from 1 to " + maxFileSize + " bytes");
        }

        if (Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new BusinessLogicException("Cannot get original name of the file");
        }

        if (!ArrayUtils.contains(allowedExtensions, FilenameUtils.getExtension(multipartFile.getOriginalFilename()))) {
            throw new BusinessLogicException("Invalid file extension! Must be: " + ArrayUtils.toString(allowedExtensions));
        }

        Optional.ofNullable(multipartFile.getOriginalFilename()).ifPresent(filename -> {
            if (filename.length() >= maxFileNameLen) {
                throw new BusinessLogicException(
                        "Length of the filename must be from 1 to " + maxFileNameLen + " symbols"
                );
            }
        });
    }

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        // get the filename or generate random one
        File converted = new File(Optional.ofNullable(multipartFile.getOriginalFilename())
                .orElse(RandomStringUtils.random(maxFileNameLen, true, true)));

        try (FileOutputStream fos = new FileOutputStream(converted)) {
            fos.write(multipartFile.getBytes());
        }
        return converted;
    }

    private String generateFileName(String filename) {
        return String.format(
                "%s-%s.%s",
                FilenameUtils.removeExtension(filename),
                new SimpleDateFormat("hh-mm-ss@dd-MM-yyyy").format(new Date().getTime()),
                FilenameUtils.getExtension(filename)
        );
    }

    private String uploadToAmazon(MultipartFile multipartFile) throws IOException {
        File file = convertToFile(multipartFile);

        String fileKey = generateFileName(file.getName());
        uploadFileTos3bucket(fileKey, file);

        Files.deleteIfExists(file.toPath());
        return fileKey;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
