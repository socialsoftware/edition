package pt.ist.socialsoftware.edition.ldod.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.util.Date;

@Component
public class JWTTokenProvider {
    private final static long EXPIRATION_TIME = Long
            .parseLong(PropertiesManager.getProperties().getProperty("spring.security.jwt.expiration.time"));
    private final static String SECRET = PropertiesManager.getProperties().getProperty("spring.security.jwt.secret");

    public String generateToken(Authentication auth) {
        return Jwts.builder().setSubject(((LdoDUserDetails) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
    }
}
