package com.ncec.companion.model.entity;

import com.ncec.companion.model.enums.TaskPriority;
import com.ncec.companion.model.enums.TaskType;
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
@Table(name = "professors_tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfessorTaskEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @NotNull
    @Column(name = "dueDate", nullable = false)
    private Date dueDate;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "creatorId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private ProfessorEntity professor;

    @OneToMany(
            mappedBy = "professorTask",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<StudentTaskEntity> studentsTasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "professors_tasks_attachments",
            joinColumns = {@JoinColumn(name = "professorTaskId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "attachmentId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<AttachmentEntity> attachments = new HashSet<>();
}
