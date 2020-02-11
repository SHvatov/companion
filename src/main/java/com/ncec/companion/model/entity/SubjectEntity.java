package com.ncec.companion.model.entity;

import com.ncec.companion.model.enums.SubjectType;
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
        name = "subjects",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"})
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectEntity extends AbstractDataBaseEntity {
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType type;

    @ManyToMany(
            mappedBy = "subjects",
            fetch = FetchType.LAZY
    )
    private Set<ProfessorEntity> professors = new HashSet<>();
}
