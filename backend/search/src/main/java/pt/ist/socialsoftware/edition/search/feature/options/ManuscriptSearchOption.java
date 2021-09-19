package pt.ist.socialsoftware.edition.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.notification.dtos.text.SourceDto;
import pt.ist.socialsoftware.edition.search.api.dto.ManuscriptSearchOptionDto;

public final class ManuscriptSearchOption extends AuthoralSearchOption {
    public static final String MANUSCRIPTID = "manus";

    public ManuscriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
                                  @JsonProperty("date") DateSearchOption date) {
        super(hasLdoD, date);
    }

    public ManuscriptSearchOption(ManuscriptSearchOptionDto manuscriptSearchOptionDto) {
        super(manuscriptSearchOptionDto.getHasLdoD(), manuscriptSearchOptionDto.getDateSearchOption().createSearchOption());
    }

    @Override
    protected boolean isOfDocumentType(SourceDto source) {
        return source.hasHandNoteSet();
    }

}
