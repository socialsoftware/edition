package pt.ist.socialsoftware.edition.ldod.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ChangePasswordForm {

	@NotEmpty
	private String username;

	@NotEmpty
	private String currentPassword;

	@Size(min = 6)
	private String newPassword;

	@Size(min = 6)
	private String retypedPassword;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCurrentPassword() {
		return this.currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRetypedPassword() {
		return this.retypedPassword;
	}

	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}

}
