package com.ncec.companion.service.security;

import com.ncec.companion.model.dto.auth.AuthCredentialsDto;
import com.ncec.companion.model.dto.auth.SuccessfulAuthDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.enums.UserRole;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {
	private final UserCrudService userCrudService;
	private final ProfessorCrudService professorCrudService;
	private final StudentCrudService studentCrudService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public SuccessfulAuthDto login(AuthCredentialsDto credentials) {
		// check if such user exists
		if (!userCrudService.existByEmail(credentials.getEmail())) {
			throw new BadCredentialsException("Incorrect credentials were passed!");
		}

		// check if password is correct
		UserDto user = userCrudService.findByEmail(credentials.getEmail());
		if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect credentials were passed!");
		}

		// will fail with AuthenticationException if invalid user / password anyway
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						credentials.getEmail(),
						credentials.getPassword()
				)
		);

		// get user's role
		List<UserRole> roles = new ArrayList<>();
		roles.add(user.getRole());

		// get user's id depending on his role in the system
		Integer id;
		if (user.getRole() == UserRole.ROLE_DEANERY) {
			id = user.getId();
		} else if (user.getRole() == UserRole.ROLE_PROFESSOR) {
			id = professorCrudService.findByUserId(user.getId()).getId();
		} else {
			id = studentCrudService.findByUserId(user.getId()).getId();
		}

		// create token and return it to the user
		return new SuccessfulAuthDto(
				id,
				user.getId(),
				jwtTokenProvider.createToken(credentials.getEmail(), roles),
				user.getRole()
		);
	}
}
