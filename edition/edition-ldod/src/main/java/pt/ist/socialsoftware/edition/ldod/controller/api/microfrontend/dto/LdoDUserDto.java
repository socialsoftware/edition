package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class LdoDUserDto {

	private String username;

	public LdoDUserDto(LdoDUser user) {
		this.setUsername(user.getUsername());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
