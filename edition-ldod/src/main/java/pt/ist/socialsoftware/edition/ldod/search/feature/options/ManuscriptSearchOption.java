package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;

public final class ManuscriptSearchOption extends AuthoralSearchOption {
    public static final String MANUSCRIPTID = "manus";

    public ManuscriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
                                  @JsonProperty("date") DateSearchOption date) {
        super(hasLdoD, date);
    }

    @Override
    protected boolean isOfDocumentType(SourceDto source) {
        return source.hasHandNoteSet();
    }

    @Override
    protected String getDocumentType() {
        return MANUSCRIPTID;
    }

}
