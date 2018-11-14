package pt.ist.socialsoftware.edition.user.shared.exception;

import pt.ist.socialsoftware.edition.text.shared.exception.LdoDException;

public class LdoDDuplicateUsernameException extends LdoDException {

	private static final long serialVersionUID = 1L;

	public LdoDDuplicateUsernameException(String message) {
		super(message);
	}

}
