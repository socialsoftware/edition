package pt.ist.socialsoftware.edition.ldod.security;

import static pt.ist.socialsoftware.edition.ldod.security.SecurityConstants.HEADER_STRING;
import static pt.ist.socialsoftware.edition.ldod.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ist.socialsoftware.edition.ldod.dto.LdoDUserDto;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		this.logger.info("attemptAuthentication");
		try {
			LdoDUserDto creds = new ObjectMapper().readValue(req.getInputStream(), LdoDUserDto.class);

			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		this.logger.info("successfulAuthentication");

		String token = this.tokenProvider.generateToken(auth);
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}

}
