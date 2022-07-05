package pt.ist.socialsoftware.edition.ldod.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;

public final class TypescriptSearchOption extends AuthoralSearchOption {
	public static final String TYPESCRIPT = "datil";

	public TypescriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
			@JsonProperty("date") DateSearchOption date) {
		super(hasLdoD, date);
	}

	@Override
	protected boolean isOfDocumentType(ManuscriptSource source) {
		return !source.getTypeNoteSet().isEmpty();
	}

	@Override
	protected String getDocumentType() {
		return TYPESCRIPT;
	}

}