package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
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


import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.RegistrationTokenDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.Emailer;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDException;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.frontend.user.forms.SignupForm;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class SignupController {
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private Emailer emailer;

    private final ProviderSignInUtils providerSignInUtils;

    @Inject
    public SignupController(ConnectionFactoryLocator connectionFactoryLocator,
                            UsersConnectionRepository connectionRepository) {
        this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public SignupForm signupForm(WebRequest request) {
        logger.debug("signupForm");

        Connection<?> connection = this.providerSignInUtils.getConnectionFromSession(request);
        if (connection != null) {
            request.setAttribute("message", "signup.errorMessage",
                    // new Message(MessageType.INFO,
                    // "Your " + StringUtils.capitalize(
                    // connection.getKey().getProviderId())
                    // + " account is not associated with a VirtualModule Archive
                    // account. If you're new, please sign up."),
                    WebRequest.SCOPE_REQUEST);
            request.setAttribute("account", StringUtils.capitalize(connection.getKey().getProviderId()),
                    WebRequest.SCOPE_REQUEST);

            return SignupForm.fromProviderUser(connection.fetchUserProfile(), connection.getKey().getProviderId());
        } else {
            return new SignupForm();
        }
    }

//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request,
//                         HttpServletRequest servletRequest, Model model) {
//        logger.debug("signup username:{}, firstName:{}, lastName:{}, email:{}, socialMedia:{}, socialId:{}",
//                form.getUsername(), form.getFirstName(), form.getLastName(), form.getEmail(),
//                form.getSocialMediaService(), form.getSocialMediaId());
//
//        if (formBinding.hasErrors()) {
//            return null;
//        }
//
//        if (!form.isConduct()) {
//            formBinding.rejectValue("conduct", "header.conduct.error");
//        } else {
//            User.SocialMediaService socialMediaService = form.getSocialMediaService().equals("") ? null
//                    : User.SocialMediaService.valueOf(form.getSocialMediaService().toUpperCase());
//
//            User user = null;
//            RegistrationToken token = null;
//            try {
//                user = UserModule.getInstance().createUser(this.passwordEncoder, form.getUsername(), form.getPassword(),
//                        form.getFirstName(), form.getLastName(), form.getEmail(), socialMediaService,
//                        form.getSocialMediaId());
//                token = user.createRegistrationToken(UUID.randomUUID().toString());
//            } catch (LdoDException e) {
//                formBinding.rejectValue("username", "user.duplicateUsername");
//            }
//
//            if (user != null) {
//                try {
//                    token.requestAuthorization(servletRequest, emailer);
//                } catch (AddressException e) {
//                    throw new LdoDException("Token Confirmation - AddressException");
//                } catch (MessagingException e) {
//                    throw new LdoDException("Token Confirmation - MessagingException");
//
//                }
//                this.providerSignInUtils.doPostSignUp(user.getUsername(), request);
//
//                model.addAttribute("message", "signup.confirmation");
//                model.addAttribute("argument",
//                        PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));
//
//                return "signin";
//            }
//        }
//        return null;
//    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request,
                         HttpServletRequest servletRequest, Model model) {
        logger.debug("signup username:{}, firstName:{}, lastName:{}, email:{}, socialMedia:{}, socialId:{}",
                form.getUsername(), form.getFirstName(), form.getLastName(), form.getEmail(),
                form.getSocialMediaService(), form.getSocialMediaId());

        if (formBinding.hasErrors()) {
            return null;
        }

        if (!form.isConduct()) {
            formBinding.rejectValue("conduct", "header.conduct.error");
        } else {
            //#TODO remove this enum dependency
//            SocialMediaService socialMediaService = form.getSocialMediaService().equals("") ? null
//                    : SocialMediaService.valueOf(form.getSocialMediaService().toUpperCase());

            UserDto user = null;
            RegistrationTokenDto token = null;
            try {
                user = this.feUserRequiresInterface.createUser(this.passwordEncoder, form.getUsername(), form.getPassword(),
                        form.getFirstName(), form.getLastName(), form.getEmail(), form.getSocialMediaService(),
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
                this.providerSignInUtils.doPostSignUp(user.getUsername(), request);

                model.addAttribute("message", "signup.confirmation");
                model.addAttribute("argument",
                        PropertiesManager.getProperties().getProperty("registration.confirmation.email.address"));

                return "signin";
            }
        }
        return null;
    }

    @RequestMapping(value = "/signup/registrationAuthorization", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String authorizeRegistration(WebRequest request, HttpServletRequest servletRequest, Model model,
                                        @RequestParam("token") String token) {
        logger.debug("authorizeRegistration");

        RegistrationTokenDto registrationToken = this.feUserRequiresInterface.getRegistrationToken(token);

        if (registrationToken == null) {
            model.addAttribute("message", "signup.token.invalid");
            return "signin";
        }

        if ((registrationToken.getExpireTimeDateTime().getMillis() - DateTime.now().getMillis()) <= 0) {
            model.addAttribute("message", "signup.token.expired");
            return "signin";
        }

        registrationToken.setTokenAuthorized(true);

        try {
            registrationToken.requestConfirmation(servletRequest, emailer);
        } catch (MessagingException e) {
            throw new LdoDException("Token Confirmation - MessagingException");
        }

        model.addAttribute("message", "signup.token.authorized");

        return "signin";
    }

    @RequestMapping(value = "/signup/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        logger.debug("confirmRegistration");

        RegistrationTokenDto registrationToken = this.feUserRequiresInterface.getRegistrationToken(token);

        if (registrationToken == null) {
            model.addAttribute("message", "signup.token.invalid");
            return "signin";
        }

        if (!registrationToken.isAuthorized()) {
            model.addAttribute("message", "signup.token.authorized.not");
            return "signin";
        }

        UserDto user = registrationToken.getUser();
        if ((registrationToken.getExpireTimeDateTime().getMillis() - DateTime.now().getMillis()) <= 0) {
            model.addAttribute("message", "signup.token.expired");
            return "signin";
        }

        user.enableUnconfirmedUser();

        return "redirect:/signin";
    }

}
