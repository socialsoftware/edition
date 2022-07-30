package pt.ist.socialsoftware.edition.ldod.bff.user.services;

import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.GoogleIdentityDto;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.SigninRequestDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.dto.ldodMfes.SignupDto;
import pt.ist.socialsoftware.edition.ldod.forms.ChangePasswordForm;
import pt.ist.socialsoftware.edition.ldod.security.jwt.GoogleAuthTokenVerifier;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTTokenProvider;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;
import pt.ist.socialsoftware.edition.ldod.validator.ChangePasswordValidator;
import pt.ist.socialsoftware.edition.ldod.validator.SignupValidator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenProvider tokenProvider;

    @Autowired
    GoogleAuthTokenVerifier googleTokenVerifier;

    @Autowired
    UserService userService;

    @Autowired
    SignupValidator signupValidator;

    @Autowired
    private Emailer emailer;

    @Autowired
    PasswordEncoder passwordEncoder;

    public JWTAuthenticationDto authenticationService(SigninRequestDto signinRequestDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(signinRequestDto.getUsername(), signinRequestDto.getPassword());
        Authentication auth = this.authenticationManager.authenticate(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new JWTAuthenticationDto(this.tokenProvider.generateToken(auth));
    }

    public Object googleAuthenticationService(JWTAuthenticationDto tokenDto) throws TokenVerifier.VerificationException, IllegalArgumentException {
        JsonWebToken.Payload payload = googleTokenVerifier.verify(tokenDto.getAccessToken()).getPayload();
        Optional<LdoDUser> user = userService.findUserBySocialId(payload.getSubject());
        return user.isPresent() && user.get().getEnabled()
                ? new JWTAuthenticationDto(this.tokenProvider.generateToken(user.get().getUsername()))
                : new GoogleIdentityDto(payload, "google");
    }

    public void signupService(SignupDto signupForm, HttpServletRequest servletRequest) throws LdoDException, javax.mail.MessagingException {

        signupValidator.validate(signupForm);
        LdoDUser user;
        RegistrationToken token;
        try {
            user = userService.createUserService(signupForm);
        } catch (LdoDDuplicateUsernameException e) {
            throw new LdoDException(String.format("Duplicated username %s", signupForm.getUsername()));
        }
        token = user.createRegistrationToken(UUID.randomUUID().toString());
        token.requestAuthorization(servletRequest, emailer);
    }

    public void registerTokenService(String token, HttpServletRequest servletRequest) throws MessagingException, LdoDException {
        RegistrationToken registrationToken = checkRegistrationToken(token);
        registrationToken.setAuthorized(true);
        registrationToken.requestConfirmation(servletRequest, emailer);
    }

    public Optional<LdoDUser> confirmationTokenService(String token) {
        RegistrationToken registrationToken = checkRegistrationToken(token);
        if (!registrationToken.getAuthorized()) return Optional.empty();
        LdoDUser user = registrationToken.getUser();
        user.enableUnconfirmedUser();
        return Optional.of(user);
    }

    public Optional<LdoDUser> changePasswordService(ChangePasswordForm form, BindingResult formBinding) {
        ChangePasswordValidator validator = new ChangePasswordValidator(this.passwordEncoder);
        validator.validate(form, formBinding);
        if (formBinding.hasErrors()) return Optional.empty();
        LdoDUser user = LdoD.getInstance().getUser(form.getUsername());
        user.updatePassword(this.passwordEncoder, form.getCurrentPassword(), form.getNewPassword());
        return Optional.of(user);
    }

    private RegistrationToken checkRegistrationToken(String token) throws LdoDException {
        RegistrationToken registrationToken = LdoD.getInstance().getTokenSet(token);
        if (registrationToken == null)
            throw new LdoDException("Token invalid");
        if ((registrationToken.getExpireTimeDateTime().getMillis() - DateTime.now().getMillis()) <= 0)
            throw new LdoDException("Token expired");
        return registrationToken;
    }
}


