package pt.ist.socialsoftware.edition.controller;

import java.util.UUID;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.forms.SignupForm;
import pt.ist.socialsoftware.edition.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

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
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request,
			HttpServletRequest servletRequest, Model model) {
		log.debug("signup username:{}, firstName:{}, lastName:{}, email:{}, socialMedia:{}, socialId:{}",
				form.getUsername(), form.getFirstName(), form.getLastName(), form.getEmail(),
				form.getSocialMediaService(), form.getSocialMediaId());

		if (formBinding.hasErrors()) {
			return null;
		}

		SocialMediaService socialMediaService = form.getSocialMediaService().equals("") ? null
				: SocialMediaService.valueOf(form.getSocialMediaService().toUpperCase());

		LdoDUser user = null;
		RegistrationToken token = null;
		try {
			user = LdoD.getInstance().createUser(passwordEncoder, form.getUsername(), form.getPassword(),
					form.getFirstName(), form.getLastName(), form.getEmail(), socialMediaService,
					form.getSocialMediaId());
			token = user.createRegistrationToken(UUID.randomUUID().toString());
		} catch (LdoDException e) {
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
		}

		if (user != null) {
			try {
				token.confirmRegistration(servletRequest);
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			providerSignInUtils.doPostSignUp(user.getUsername(), request);

			model.addAttribute("message",
					"An email from the e-address:"
							+ PropertiesManager.getProperties().getProperty("registration.confirmation.email.address")
							+ " was sent to your mailbox. The confirmation should occur within 60 minutes");
			return "utils/okMessage";
		}
		return null;
	}

	@RequestMapping(value = "/signup/registrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
		RegistrationToken registrationToken = LdoD.getInstance().getTokenSet(token);

		if (registrationToken == null) {
			model.addAttribute("message", "invalid token");
			return "utils/notOkMessage";
		}

		LdoDUser user = registrationToken.getUser();
		if ((registrationToken.getExpireTimeDateTime().getMillis() - DateTime.now().getMillis()) <= 0) {
			model.addAttribute("message", "expired token");
			return "utils/notOkMessage";
		}

		user.enableUnconfirmedUser();

		return "redirect:/signin";
	}

}
