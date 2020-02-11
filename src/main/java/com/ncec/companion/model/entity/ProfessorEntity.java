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
@Table(name = "professors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfessorEntity extends AbstractDataBaseEntity {
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(
			name = "userId",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
			unique = true,
			nullable = false
	)
	private UserEntity user;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "professors_subjects",
			joinColumns = {@JoinColumn(name = "professorId", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "subjectId", referencedColumnName = "id")},
			foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
			inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
	)
	private Set<SubjectEntity> subjects = new HashSet<>();

	@OneToMany(
			mappedBy = "professor",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL
	)
	private Set<LessonEntity> lessons = new HashSet<>();

	@OneToMany(
			mappedBy = "professor",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL
	)
	private Set<ProfessorTaskEntity> professorTasks = new HashSet<>();
}
