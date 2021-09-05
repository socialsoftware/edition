package pt.ist.socialsoftware.edition.ldod.frontend.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserModuleSignInAdapter implements SignInAdapter {
    private static final Logger log = LoggerFactory.getLogger(UserModuleSignInAdapter.class);

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    private final RequestCache requestCache;

    @Inject
    public UserModuleSignInAdapter(RequestCache requestCache) {
        log.debug("UserModuleSignInAdapter");

        this.requestCache = requestCache;
    }

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        log.debug("signIn localUserId:{}", localUserId);

        SigninUtils.signin(request, this.feUserRequiresInterface.getUser(localUserId));

        return extractOriginalUrl(request);
    }

    private String extractOriginalUrl(NativeWebRequest request) {
        HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
        SavedRequest saved = this.requestCache.getRequest(nativeReq, nativeRes);
        if (saved == null) {
            return null;
        }
        this.requestCache.removeRequest(nativeReq, nativeRes);
        removeAutheticationAttributes(nativeReq.getSession(false));

        log.debug("extractOriginalUrl saved.getRedirectUrl():{}", saved.getRedirectUrl(), saved.getMethod(),
                saved.getHeaderNames());

        return null; // saved.getRedirectUrl();
    }

    private void removeAutheticationAttributes(HttpSession session) {
        log.debug("removeAutheticationAttributes session:{}", session);

        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}