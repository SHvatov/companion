package com.ncec.companion.controller;

import com.ncec.companion.model.dto.auth.AuthCredentialsDto;
import com.ncec.companion.model.dto.auth.SuccessfulAuthDto;
import com.ncec.companion.service.security.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final UserAuthenticationService authenticationService;

	@PostMapping(value = "/login")
	public ResponseEntity<SuccessfulAuthDto> login(@RequestBody AuthCredentialsDto credentials) {
		return ResponseEntity.ok(authenticationService.login(credentials));
	}
}
