package com.ncec.companion.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncec.companion.model.dto.DataTransferObject;
import com.ncec.companion.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends DataTransferObject {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    @NotNull
    private UserRole role;

    @JsonIgnore
    private String password;

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + secondName;
    }
}
