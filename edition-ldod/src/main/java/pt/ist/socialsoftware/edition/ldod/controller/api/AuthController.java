package pt.ist.socialsoftware.edition.ldod.controller.api;

import java.util.UUID;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.ldod.forms.SignupForm;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTTokenProvider;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;
import pt.ist.socialsoftware.edition.ldod.validator.ChangePasswordValidator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTTokenProvider tokenProvider;
	
	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private Emailer emailer;

	private ProviderSignInUtils providerSignInUtils;


	@PostMapping("/signin")
	public ResponseEntity<JWTAuthenticationDto> authenticateUser(@Valid @RequestBody LdoDUserDto loginRequest) {
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
	
	@PostMapping(value = "/signup")
	public String signup(@RequestBody SignupForm form, BindingResult formBinding, WebRequest request,
			HttpServletRequest servletRequest) throws javax.mail.MessagingException {
		logger.debug("signup username:{}, firstName:{}, lastName:{}, email:{}, socialMedia:{}, socialId:{}",
				form.getUsername(), form.getFirstName(), form.getLastName(), form.getEmail(),
				form.getSocialMediaService(), form.getSocialMediaId());

		if (formBinding.hasErrors()) {
			return "primeiro erro";
		}

		if (!form.isConduct()) {
			formBinding.rejectValue("conduct", "header.conduct.error");
		} else {
			LdoDUser.SocialMediaService socialMediaService = form.getSocialMediaService().equals("") ? null
					: LdoDUser.SocialMediaService.valueOf(form.getSocialMediaService().toUpperCase());

			LdoDUser user = null;
			RegistrationToken token = null;
			try {
				user = LdoD.getInstance().createUser(this.passwordEncoder, form.getUsername(), form.getPassword(),
						form.getFirstName(), form.getLastName(), form.getEmail(), socialMediaService,
						form.getSocialMediaId());
				token = user.createRegistrationToken(UUID.randomUUID().toString());
			} catch (LdoDException e) {
				formBinding.rejectValue("username", "user.duplicateUsername");
			}

			if (user != null) {
				try {
					token.requestAuthorization(servletRequest, emailer);
				} catch (AddressException e) {
					throw new LdoDException("Token Confirmation - AddressException");
				} catch (MessagingException e) {
					throw new LdoDException("Token Confirmation - MessagingException");

				}
				//this.providerSignInUtils.doPostSignUp(user.getUsername(), request);
				return "success";
			}
		}
		return "error";
	}
	
	@PostMapping(value = "/changePassword")
	public String changePassword(@RequestBody ChangePasswordForm form, BindingResult formBinding) {
		logger.debug("changePassword username:{}", form.getUsername());

		ChangePasswordValidator validator = new ChangePasswordValidator(this.passwordEncoder);
		validator.validate(form, formBinding);

		if (formBinding.hasErrors()) {
			return "error";
		}

		LdoDUser user = LdoD.getInstance().getUser(form.getUsername());

		user.updatePassword(this.passwordEncoder, form.getCurrentPassword(), form.getNewPassword());

		return "success";
	}


}