package pt.ist.socialsoftware.edition.ldod.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class EditUserForm {

	@NotEmpty
	private String oldUsername;

	@NotEmpty
	@Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "{required.alphanumeric}")
	private String newUsername;

	@NotEmpty
	@Pattern(regexp = "^[\\p{L}\\s]+$", message = "{required.alpha}")
	private String firstName;

	@NotEmpty
	@Pattern(regexp = "^[\\p{L}\\s]+$", message = "{required.alpha}")
	private String lastName;

	@NotEmpty
	@Email
	private String email;

	private boolean user;

	private boolean admin;

	private boolean enabled;

	private String newPassword;

	public String getOldUsername() {
		return this.oldUsername;
	}

	public void setOldUsername(String oldUsername) {
		this.oldUsername = oldUsername;
	}

	public String getNewUsername() {
		return this.newUsername;
	}

	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isUser() {
		return this.user;
	}

	public void setUser(boolean roleUser) {
		this.user = roleUser;
	}

	public boolean isAdmin() {
		return this.admin;
	}

	public void setAdmin(boolean roleAdmin) {
		this.admin = roleAdmin;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
