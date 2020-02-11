package com.ncec.companion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttachmentDto extends DataTransferObject {
    @NotBlank
    private String fileKey;

    @NotBlank
    private String fileBucket;

    @NotBlank
    private String endpointUrl;
}
