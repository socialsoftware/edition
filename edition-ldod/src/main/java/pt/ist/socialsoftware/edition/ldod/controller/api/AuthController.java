package pt.ist.socialsoftware.edition.ldod.controller.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<JWTAuthenticationDto> authenticateUser(@Valid @RequestBody LdoDUserDto loginRequest) {
		System.out.println(loginRequest.getUsername() + loginRequest.getPassword());
		try {
			Authentication authentication = this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = this.tokenProvider.generateToken(authentication);
			return new ResponseEntity<JWTAuthenticationDto>(new JWTAuthenticationDto(jwt), HttpStatus.OK);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

}