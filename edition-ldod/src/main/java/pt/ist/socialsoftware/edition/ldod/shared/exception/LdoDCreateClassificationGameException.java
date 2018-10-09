package pt.ist.socialsoftware.edition.ldod.shared.exception;

import java.util.ArrayList;
import java.util.List;

public class LdoDCreateClassificationGameException extends LdoDException {
	private static final long serialVersionUID = 1L;

	private List<String> errors = new ArrayList<>();
	private final String description;
	private final String interExternalId;
	private final String date;

	public LdoDCreateClassificationGameException(List<String> errors, String description, String date,
			String interExternalId) {
		this.errors = errors;
		this.description = description;
		this.date = date;
		this.interExternalId = interExternalId;
	}

	public List<String> getErrors() {
		return this.errors;
	}

	public String getDescription() {
		return this.description;
	}

	public String getDate() {
		return this.date;
	}

	public String getInterExternalId() {
		return this.interExternalId;
	}

}
