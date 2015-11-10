package pt.ist.socialsoftware.edition.security;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public class SimpleSignInAdapter implements SignInAdapter {
    private static Logger log = LoggerFactory
            .getLogger(SimpleSignInAdapter.class);

    private final RequestCache requestCache;

    @Inject
    public SimpleSignInAdapter(RequestCache requestCache) {
        log.debug("SimpleSignInAdapter");

        this.requestCache = requestCache;
    }

    @Override
    public String signIn(String localUserId, Connection<?> connection,
            NativeWebRequest request) {
        log.debug("signIn localUserId:{}", localUserId);

        SigninUtils.signin(localUserId);
        return extractOriginalUrl(request);
    }

    private String extractOriginalUrl(NativeWebRequest request) {
        HttpServletRequest nativeReq = request
                .getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeRes = request
                .getNativeResponse(HttpServletResponse.class);
        SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
        if (saved == null) {
            return null;
        }
        requestCache.removeRequest(nativeReq, nativeRes);
        removeAutheticationAttributes(nativeReq.getSession(false));
        return saved.getRedirectUrl();
    }

    private void removeAutheticationAttributes(HttpSession session) {
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}