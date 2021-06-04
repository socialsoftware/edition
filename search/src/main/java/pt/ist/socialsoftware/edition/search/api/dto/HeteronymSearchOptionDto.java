package pt.ist.socialsoftware.edition.search.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.search.feature.options.HeteronymSearchOption;


public final class HeteronymSearchOptionDto extends SearchOptionDto {
    private static final Logger logger = LoggerFactory.getLogger(HeteronymSearchOptionDto.class);

    private final String xmlId4Heteronym;

    public HeteronymSearchOptionDto(@JsonProperty("xmlId4Heteronym") String xmlId) {
        logger.debug("HeteronymSearchOption xmlId: {}", xmlId);
        this.xmlId4Heteronym = xmlId.equals("null") ? null : xmlId;
    }

    @Override
    public HeteronymSearchOption createSearchOption() {
        return new HeteronymSearchOption(this);
    }

    public String getXmlId4Heteronym() {
        return this.xmlId4Heteronym;
    }
}
