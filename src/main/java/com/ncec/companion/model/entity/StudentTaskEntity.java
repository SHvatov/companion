package com.ncec.companion.model.entity;

import com.ncec.companion.model.enums.TaskAssessment;
import com.ncec.companion.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "students_tasks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "professorTaskId"})
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentTaskEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "assessment", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskAssessment assessment;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "professorTaskId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private ProfessorTaskEntity professorTask;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "studentId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private StudentEntity student;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "students_tasks_attachments",
            joinColumns = {@JoinColumn(name = "studentTaskId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "attachmentId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<AttachmentEntity> attachments = new HashSet<>();

    @OneToMany(
            mappedBy = "studentTask",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<MessageEntity> messages = new HashSet<>();

    @OneToMany(
            mappedBy = "studentTask",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<NotificationEntity> notifications = new HashSet<>();
}
