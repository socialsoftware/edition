package pt.ist.socialsoftware.edition.shared.exception;

public class LdoDLoadException extends LdoDException {

	private final String message;

	public LdoDLoadException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
