package com.ncec.companion.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @OneToMany(
            mappedBy = "group",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<StudentEntity> students = new HashSet<>();

    @ManyToMany(
            mappedBy = "groups",
            fetch = FetchType.LAZY
    )
    private Set<LessonEntity> lessons = new HashSet<>();
}