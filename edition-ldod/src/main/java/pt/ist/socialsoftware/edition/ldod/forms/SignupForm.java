package pt.ist.socialsoftware.edition.ldod.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.social.connect.UserProfile;

public class SignupForm {

	@NotEmpty
	@Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "{required.alphanumeric}")
	private String username;

	@Size(min = 6)
	private String password;

	@NotEmpty
	@Pattern(regexp = "^[\\p{L}\\s]+$", message = "{required.alpha}")
	private String firstName;

	@NotEmpty
	@Pattern(regexp = "^[\\p{L}\\s]+$", message = "{required.alpha}")
	private String lastName;

	@NotEmpty
	@Email
	private String email;

	private boolean conduct;

	private String socialMediaService;

	private String socialMediaId;

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

	public boolean isConduct() {
		return this.conduct;
	}

	public void setConduct(boolean conduct) {
		this.conduct = conduct;
	}

	public String getSocialMediaService() {
		return this.socialMediaService;
	}

	public void setSocialMediaService(String socialMediaService) {
		this.socialMediaService = socialMediaService;
	}

	public String getSocialMediaId() {
		return this.socialMediaId;
	}

	public void setSocialMediaId(String socialMediaId) {
		this.socialMediaId = socialMediaId;
	}

	public static SignupForm fromProviderUser(UserProfile providerUser, String providerId) {
		SignupForm form = new SignupForm();
		form.setFirstName(providerUser.getFirstName());
		form.setLastName(providerUser.getLastName());
		form.setUsername(providerUser.getUsername());
		form.setEmail(providerUser.getEmail());
		form.setSocialMediaService(providerId);
		form.setSocialMediaId(providerUser.getUsername());

		return form;
	}

}
