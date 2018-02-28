package pt.ist.socialsoftware.edition.core.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.core.domain.ManuscriptSource;

public final class ManuscriptSearchOption extends AuthoralSearchOption {
	public static final String MANUSCRIPTID = "manus";

	public ManuscriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
			@JsonProperty("date") DateSearchOption date) {
		super(hasLdoD, date);
	}

	@Override
	protected boolean isOfDocumentType(ManuscriptSource source) {
		return !source.getHandNoteSet().isEmpty();
	}

	@Override
	protected String getDocumentType() {
		return MANUSCRIPTID;
	}

}
