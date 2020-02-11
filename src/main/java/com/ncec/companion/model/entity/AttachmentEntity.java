package com.ncec.companion.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "attachments",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"fileKey", "fileBucket"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttachmentEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "fileKey", nullable = false)
    private String fileKey;

    @NotNull
    @Column(name = "fileBucket", nullable = false)
    private String fileBucket;

    @NotNull
    @Column(name = "endpointUrl", nullable = false)
    private String endpointUrl;
}
