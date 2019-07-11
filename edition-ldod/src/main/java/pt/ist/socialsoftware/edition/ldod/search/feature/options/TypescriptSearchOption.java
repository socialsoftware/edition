package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;

public final class TypescriptSearchOption extends AuthoralSearchOption {
    public static final String TYPESCRIPT = "datil";

    public TypescriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
                                  @JsonProperty("date") DateSearchOption date) {
        super(hasLdoD, date);
    }

    @Override
    protected boolean isOfDocumentType(SourceDto source) {
        return source.hasTypeNoteSet();
    }

    @Override
    protected String getDocumentType() {
        return TYPESCRIPT;
    }

}