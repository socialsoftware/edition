package pt.ist.socialsoftware.edition.ldod.bff.user.services;

import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.GoogleIdentityDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SigninRequestDto;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SignupDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.RegistrationToken;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.security.jwt.GoogleAuthTokenVerifier;
import pt.ist.socialsoftware.edition.ldod.security.jwt.JWTTokenProvider;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;
import pt.ist.socialsoftware.edition.ldod.utils.Emailer;
import pt.ist.socialsoftware.edition.ldod.validator.SignupValidator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    LdoDUserService userService;

    @Autowired
    SignupValidator signupValidator;

    @Autowired
    private Emailer emailer;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TextEncryptor textEncryptor;


    public JWTAuthenticationDto authenticationService(SigninRequestDto signinRequestDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(signinRequestDto.getUsername(), signinRequestDto.getPassword());
        Authentication auth = this.authenticationManager.authenticate(user);
        LdoD.getInstance().getUser(signinRequestDto.getUsername()).setLogin(LocalDate.now());
        return new JWTAuthenticationDto(this.tokenProvider.generateToken(auth));
    }

    public Object googleAuthenticationService(JWTAuthenticationDto tokenDto) throws TokenVerifier.VerificationException, IllegalArgumentException {
        JsonWebToken.Payload payload = googleTokenVerifier.verify(tokenDto.getAccessToken()).getPayload();
        Optional<LdoDUser> user = userService.findUserBySocialId(payload.getSubject());
        if (user.isPresent() && user.get().getEnabled()) {
            user.ifPresent((ldoDUser) -> ldoDUser.setLogin(LocalDate.now()));
            return new JWTAuthenticationDto(this.tokenProvider.generateToken(user.get().getUsername()));
        }

        LdoD.getInstance()
                .getUserConnectionSet()
                .stream()
                .filter(userConnection -> userConnection.getProviderUserId().equals(payload.getSubject())).findFirst().ifPresent(conn -> {
                    throw new UsernameNotFoundException(String.format(Message.USERNAME_NOT_FOUND.getLabel(), conn.getUserId()));
                });

        return new GoogleIdentityDto(payload, "google");
    }

    public void signupService(SignupDto signupDto, HttpServletRequest servletRequest) throws LdoDException, javax.mail.MessagingException {

        signupValidator.validate(signupDto);
        LdoDUser user;
        RegistrationToken token;
        try {
            user = userService.createUserService(signupDto);
            createUserConnection(signupDto);
        } catch (LdoDDuplicateUsernameException e) {
            throw new LdoDException(String.format("Duplicated username %s", signupDto.getUsername()));
        }
        token = user.createRegistrationToken(UUID.randomUUID().toString());
        token.requestAuthorization(servletRequest, emailer);
    }

    public void createUserConnection(SignupDto signupDto) {
        if (!signupDto.getSocialId().equals(""))
            LdoD.getInstance().createUserConnection(signupDto);
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


    private RegistrationToken checkRegistrationToken(String token) throws LdoDException {
        RegistrationToken registrationToken = LdoD.getInstance().getTokenSet(token);
        if (registrationToken == null)
            throw new LdoDException("Token invalid");
        if ((registrationToken.getExpireTimeDateTime().getMillis() - DateTime.now().getMillis()) <= 0)
            throw new LdoDException("Token expired");
        return registrationToken;
    }
}


