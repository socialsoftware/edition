package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class ParticipantDto {
	
	private String userName;
	private String firstName;
	private String lastName;

	public ParticipantDto(LdoDUser ldodUser) {
		this.setUserName(ldodUser.getUsername());
		this.setFirstName(ldodUser.getFirstName());
		this.setLastName(ldodUser.getLastName());
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
