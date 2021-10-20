package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.Date;

import org.springframework.security.core.session.SessionInformation;

public class SessionDto {
	private String sessionId;
	private Date lastRequest;

	public SessionDto(SessionInformation session) {
		this.setSessionId(session.getSessionId());
		this.setLastRequest(session.getLastRequest());
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(Date lastRequest) {
		this.lastRequest = lastRequest;
	}
}
