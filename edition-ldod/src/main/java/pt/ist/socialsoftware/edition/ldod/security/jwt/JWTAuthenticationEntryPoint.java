package pt.ist.socialsoftware.edition.ldod.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static Logger logger = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.error("Responding with unauthorized error. Message - {}", authException.getMessage());
        if (!request.getRequestURI().startsWith("/api/"))
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sorry, not authorized to access this resource");
        else {
            if(response.getStatus() == 200) response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
