package pt.ist.socialsoftware.edition.ldod.security.jwt;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

@Component
public class GoogleAuthTokenVerifier {

    private final static String CLIENT_ID = PropertiesManager.getProperties().getProperty("spring.social.google.appId");

    public JsonWebSignature verify(String token) throws TokenVerifier.VerificationException {
        return TokenVerifier
                .newBuilder()
                .setAudience(CLIENT_ID)
                .build().verify(token);

    }


}
