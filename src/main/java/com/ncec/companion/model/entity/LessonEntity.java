package com.ncec.companion.model.entity;

import com.ncec.companion.model.enums.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LessonEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "day", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @NotNull
    @Column(name = "startTime", nullable = false)
    private Integer startTime;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Column(name = "auditory", nullable = false)
    private String auditory;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subjectId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private SubjectEntity subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "professorId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private ProfessorEntity professor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lessons_groups",
            joinColumns = {@JoinColumn(name = "classId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "groupId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<GroupEntity> groups = new HashSet<>();
}
