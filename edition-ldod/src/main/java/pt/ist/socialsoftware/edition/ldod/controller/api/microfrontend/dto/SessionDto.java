package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.Date;

import org.springframework.security.core.session.SessionInformation;

import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;

public class SessionDto {
	private String sessionId;
	private Date lastRequest;
	private String userName;
	private String firstName;
	private String lastName;

	public SessionDto(SessionInformation session) {
		this.setSessionId(session.getSessionId());
		this.setLastRequest(session.getLastRequest());
		LdoDUserDetails userInfo = (LdoDUserDetails) session.getPrincipal();
		this.setUserName(userInfo.getUser().getUsername());
		this.setFirstName(userInfo.getUser().getFirstName());
		this.setLastName(userInfo.getUser().getLastName());
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
