package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

public final class DactiloscryptSearchOption extends AuthoralSearchOption {
	public static final String DATILOSCRIPTID = "datil";

	public DactiloscryptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD, @JsonProperty("date") DateSearchOption date) {
		super(hasLdoD, date);
	}

	@Override
	protected String getDocumentType() {
		return DATILOSCRIPTID;
	}
}