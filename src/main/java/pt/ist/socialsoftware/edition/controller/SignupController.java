package pt.ist.socialsoftware.edition.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.security.SigninUtils;
import pt.ist.socialsoftware.edition.security.SignupForm;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
public class SignupController {
	private static Logger log = LoggerFactory.getLogger(SignupController.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	private final ProviderSignInUtils providerSignInUtils;

	@Inject
	public SignupController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository) {
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		log.debug("signupForm");

		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message",
					"Your " + StringUtils.capitalize(connection.getKey().getProviderId())
							+ " account is not associated with a LdoD Archive account. If you\'re new, please sign up.",
					// new Message(MessageType.INFO,
					// "Your " + StringUtils.capitalize(
					// connection.getKey().getProviderId())
					// + " account is not associated with a LdoD Archive
					// account. If you're new, please sign up."),
					WebRequest.SCOPE_REQUEST);

			return SignupForm.fromProviderUser(connection.fetchUserProfile(), connection.getKey().getProviderId());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		log.debug("signup username:{}, firstName:{}, lastName:{}, email:{}, socialMedia:{}, socialId:{}",
				form.getUsername(), form.getFirstName(), form.getLastName(), form.getEmail(),
				form.getSocialMediaService(), form.getSocialMediaId());

		if (formBinding.hasErrors()) {
			return null;
		}

		SocialMediaService socialMediaService = form.getSocialMediaService() != ""
				? SocialMediaService.valueOf(form.getSocialMediaService().toUpperCase()) : null;

		LdoDUser user = null;
		try {
			user = LdoD.getInstance().createUser(passwordEncoder, form.getUsername(), form.getPassword(),
					form.getFirstName(), form.getLastName(), form.getEmail(), socialMediaService,
					form.getSocialMediaId());
		} catch (LdoDException e) {
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
		}

		if (user != null) {
			SigninUtils.signin(request, user);
			providerSignInUtils.doPostSignUp(user.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}

}
