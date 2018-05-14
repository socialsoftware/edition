package pt.ist.socialsoftware.edition.core.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import pt.ist.socialsoftware.edition.core.dto.LdoDUserDTO;
import pt.ist.socialsoftware.edition.core.security.TokenAuthenticationService;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static Logger log = LoggerFactory.getLogger(JWTLoginFilter.class);
	private TokenAuthenticationService tokenAuthenticationService = new TokenAuthenticationServiceImpl();

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {


		// Retrieve username and password from the http request and save them in an LdoD object.


		LdoDUserDTO u = new LdoDUserDTO(req.getParameter("username"),req.getParameter("password"));
		
		// Verify if the correctness of login details.
		// If correct, the successfulAuthentication() method is executed.
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
						u.getUsername(),
						u.getPassword(),
						Collections.emptyList()
						)
				);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		
		// Pass authenticated user data to the tokenAuthenticationService in order to add a JWT to the http response.
		tokenAuthenticationService.addAuthentication(res, auth);
		chain.doFilter(req,res);
	}

}
