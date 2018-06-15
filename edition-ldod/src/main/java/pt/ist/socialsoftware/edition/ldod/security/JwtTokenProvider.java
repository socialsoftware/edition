package pt.ist.socialsoftware.edition.ldod.security;

import static pt.ist.socialsoftware.edition.ldod.security.SecurityConstants.EXPIRATION_TIME;
import static pt.ist.socialsoftware.edition.ldod.security.SecurityConstants.SECRET;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	public String generateToken(Authentication auth) {
		return Jwts.builder().setSubject(((LdoDUserDetails) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).compact();
	}

}
