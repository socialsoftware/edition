package pt.ist.socialsoftware.edition.ldod.frontend.user.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.ldod.frontend.user.security.UserModuleUserDetails;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;

import java.util.Date;

@Component
public class JWTTokenProvider {
    private final static long EXPIRATION_TIME = Long
            .parseLong(PropertiesManager.getProperties().getProperty("spring.security.jwt.expiration.time"));
    private final static String SECRET = PropertiesManager.getProperties().getProperty("spring.security.jwt.secret");

    public String generateToken(Authentication auth) {
        return Jwts.builder().setSubject(((UserModuleUserDetails) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
    }
}