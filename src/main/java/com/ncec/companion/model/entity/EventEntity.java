package com.ncec.companion.model.entity;

import com.ncec.companion.model.enums.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @NotNull
    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @NotNull
    @Column(name = "participantType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipantType participantType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "events_visitors",
            joinColumns = {@JoinColumn(name = "eventId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "userId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<UserEntity> visitors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "events_attachments",
            joinColumns = {@JoinColumn(name = "eventId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "attachmentId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<AttachmentEntity> attachments = new HashSet<>();
}
