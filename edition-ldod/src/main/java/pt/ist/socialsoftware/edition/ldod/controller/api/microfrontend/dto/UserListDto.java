package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.session.SessionInformation;

import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class UserListDto {

	private boolean ldoDAdmin;
	private List<UserDto> userList;
	private List<SessionDto> sessionList;

	public UserListDto(LdoD instance, List<LdoDUser> list, List<SessionInformation> sessionInformation) {
		this.setLdoDAdmin(instance.getAdmin());
		this.setUserList(list.stream().map(UserDto::new).collect(Collectors.toList()));
		this.setSessionList(sessionInformation.stream().map(SessionDto::new).collect(Collectors.toList()));
	}

	public boolean isLdoDAdmin() {
		return ldoDAdmin;
	}

	public void setLdoDAdmin(boolean ldoDAdmin) {
		this.ldoDAdmin = ldoDAdmin;
	}

	public List<UserDto> getUserList() {
		return userList;
	}

	public void setUserList(List<UserDto> userList) {
		this.userList = userList;
	}

	public List<SessionDto> getSessionList() {
		return sessionList;
	}

	public void setSessionList(List<SessionDto> sessionList) {
		this.sessionList = sessionList;
	}
	
}
