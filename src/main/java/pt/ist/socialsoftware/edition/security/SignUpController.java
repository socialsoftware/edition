package pt.ist.socialsoftware.edition.security;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
public class SignUpController {

    @Inject
    private PasswordEncoder passwordEncoder;

    private final ProviderSignInUtils providerSignInUtils;

    @Inject
    public SignUpController(ConnectionFactoryLocator connectionFactoryLocator,
            UsersConnectionRepository connectionRepository) {
        this.providerSignInUtils = new ProviderSignInUtils(
                connectionFactoryLocator, connectionRepository);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public SignUpForm signupForm(WebRequest request) {
        Connection<?> connection = providerSignInUtils
                .getConnectionFromSession(request);
        if (connection != null) {
            request.setAttribute("message",
                    "Your account is not associated with a LdoD Archive account",
                    // new Message(MessageType.INFO,
                    // "Your " + StringUtils.capitalize(
                    // connection.getKey().getProviderId())
                    // + " account is not associated with a LdoD Archive
                    // account. If you're new, please sign up."),
                    WebRequest.SCOPE_REQUEST);
            return SignUpForm.fromProviderUser(connection.fetchUserProfile());
        } else {
            return new SignUpForm();
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Valid SignUpForm form, BindingResult formBinding,
            WebRequest request) {
        if (formBinding.hasErrors()) {
            return null;
        }

        LdoDUser user = null;
        try {
            user = LdoD.getInstance().createUser(passwordEncoder,
                    form.getUsername(), form.getPassword(), form.getFirstName(),
                    form.getLastName());
        } catch (LdoDException e) {
            formBinding.rejectValue("username", "user.duplicateUsername",
                    "already in use");
        }

        if (user != null) {
            SignInUtils.signin(user.getUsername());
            providerSignInUtils.doPostSignUp(user.getUsername(), request);
            return "redirect:/";
        }
        return null;
    }

}
