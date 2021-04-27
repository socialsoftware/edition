package pt.ist.socialsoftware.edition.ldod.frontend.user.dto;

import pt.ist.socialsoftware.edition.ldod.frontend.utils.Emailer;

import javax.servlet.http.HttpServletRequest;

public class TokenRequestDto {

    private Emailer emailer;
    private HttpServletRequest servletRequest;

    public TokenRequestDto(Emailer emailer, HttpServletRequest servletRequest) {
        this.emailer = emailer;
        this.servletRequest = servletRequest;
    }


    public Emailer getEmailer() {
        return emailer;
    }

    public void setEmailer(Emailer emailer) {
        this.emailer = emailer;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }
}
