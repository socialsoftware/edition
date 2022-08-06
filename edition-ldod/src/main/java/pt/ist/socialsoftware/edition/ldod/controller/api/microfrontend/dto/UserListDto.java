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

	private UserListDto() {}
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

	public static final class UserListDtoBuilder {
		private boolean ldoDAdmin;
		private List<UserDto> userList;
		private List<SessionDto> sessionList;

		private UserListDtoBuilder() {
		}

		public static UserListDtoBuilder anUserListDto() {
			return new UserListDtoBuilder();
		}

		public UserListDtoBuilder ldoDAdmin(boolean ldoDAdmin) {
			this.ldoDAdmin = ldoDAdmin;
			return this;
		}

		public UserListDtoBuilder userList(List<UserDto> userList) {
			this.userList = userList;
			return this;
		}

		public UserListDtoBuilder sessionList(List<SessionDto> sessionList) {
			this.sessionList = sessionList;
			return this;
		}

		public UserListDto build() {
			UserListDto userListDto = new UserListDto();
			userListDto.setLdoDAdmin(ldoDAdmin);
			userListDto.setUserList(userList);
			userListDto.setSessionList(sessionList);
			return userListDto;
		}
	}
}
