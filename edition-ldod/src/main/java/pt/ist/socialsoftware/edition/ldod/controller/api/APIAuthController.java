package pt.ist.socialsoftware.edition.ldod.controller.api;

import javax.validation.Valid;

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

import pt.ist.socialsoftware.edition.ldod.dto.APIResponse;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDTO;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDTO;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class APIAuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LdoDUserDTO loginRequest) {

		try{
			Authentication authentication = this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt = this.tokenProvider.generateToken(authentication);
			return ResponseEntity.ok(new JWTAuthenticationDTO(jwt));
		}catch (AuthenticationException e){
			return new ResponseEntity(new APIResponse(false, "Your Username or Password is incorrect. Please try again!"),
					HttpStatus.UNAUTHORIZED);
		}
	}

}