package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

public final class ManuscriptSearchOption extends AuthoralSearchOption {
	public static final String MANUSCRIPTID = "manus";

	public ManuscriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD, @JsonProperty("date") DateSearchOption date) {
		super(hasLdoD, date);
	}

	@Override
	protected String getDocumentType() {
		return MANUSCRIPTID;
	}
}
