package com.ncec.companion.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncec.companion.model.dto.DataTransferObject;
import com.ncec.companion.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends DataTransferObject implements UserContainer {
	@Email
	@NotBlank
	@JsonProperty(access = WRITE_ONLY)
	private String email;

	@NotBlank
	@JsonProperty(access = WRITE_ONLY)
	private String firstName;

	@NotBlank
	@JsonProperty(access = WRITE_ONLY)
	private String secondName;

	@NotNull
	@JsonProperty(access = WRITE_ONLY)
	private UserRole role;

	@JsonIgnore
	private String password;

	@NotNull
    @PositiveOrZero
	@JsonProperty(access = WRITE_ONLY)
	private Integer group;

	@JsonIgnore
	private Integer user;

	@JsonIgnore
	private Set<Integer> studentTasks = new HashSet<>();
}
