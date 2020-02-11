package com.ncec.companion.model.dto.auth;

import com.ncec.companion.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulAuthDto implements Serializable {
	private Integer id;
	private Integer userId;
	private String token;
	private UserRole role;
}
