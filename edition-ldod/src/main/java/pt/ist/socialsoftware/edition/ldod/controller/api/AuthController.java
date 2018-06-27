package pt.ist.socialsoftware.edition.ldod.controller.api;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.*;

import pt.ist.socialsoftware.edition.ldod.dto.*;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTTokenProvider;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static Logger logger = LoggerFactory.getLogger(AuthController.class);

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
			logger.debug("Bad Credentials");
			return new ResponseEntity(new APIResponse(false, "Your Username or Password is incorrect. Please try again!"),
					HttpStatus.UNAUTHORIZED);
		}

	}

	@GetMapping("/social")
	public ResponseEntity<?> authenticateConfigSocial(HttpSession session, Principal user) {
		String name = user == null ? null : user.getName();
		logger.debug("resource " + user.getName());

		String jwt = this.tokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());
		//session.invalidate();
		return ResponseEntity.ok(new JWTAuthenticationDTO(jwt));
	}

}