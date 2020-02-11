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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfessorDto extends DataTransferObject implements UserContainer {
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

    @NotEmpty
    @JsonProperty(access = WRITE_ONLY)
    private Set<Integer> subjects = new HashSet<>();

    @JsonIgnore
    private Integer user;

    @JsonIgnore
    private Set<Integer> lessons = new HashSet<>();

    @JsonIgnore
    private Set<Integer> professorTasks = new HashSet<>();
}
