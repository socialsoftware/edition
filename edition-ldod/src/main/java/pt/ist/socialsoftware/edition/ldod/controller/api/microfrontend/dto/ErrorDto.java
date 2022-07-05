package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

public class ErrorDto {
	private String error;

	public ErrorDto(String error) {
		this.setError(error);
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
