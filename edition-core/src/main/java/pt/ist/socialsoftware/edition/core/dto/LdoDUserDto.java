package pt.ist.socialsoftware.edition.core.dto;

import org.hibernate.validator.constraints.NotBlank;

public class LdoDUserDto {
	private long id;
	@NotBlank
	private String username;
	@NotBlank
	private String password;

	public long getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}