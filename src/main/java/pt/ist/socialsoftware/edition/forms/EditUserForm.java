package pt.ist.socialsoftware.edition.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class EditUserForm {

	@NotEmpty
	private String oldUsername;

	@NotEmpty
	private String newUsername;

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	@Email
	private String email;

	private boolean user;

	private boolean admin;

	private String newPassword;

	public String getOldUsername() {
		return oldUsername;
	}

	public void setOldUsername(String oldUsername) {
		this.oldUsername = oldUsername;
	}

	public String getNewUsername() {
		return newUsername;
	}

	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean roleUser) {
		this.user = roleUser;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean roleAdmin) {
		this.admin = roleAdmin;
	}

}
