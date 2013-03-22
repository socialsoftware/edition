package pt.ist.socialsoftware.edition.shared.exception;

public class LdoDException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;

	public LdoDException() {

	}

	public LdoDException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
