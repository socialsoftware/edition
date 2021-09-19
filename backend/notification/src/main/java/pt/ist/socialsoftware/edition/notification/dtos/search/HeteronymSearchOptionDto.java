package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class HeteronymSearchOptionDto extends SearchOptionDto {
    private static final Logger logger = LoggerFactory.getLogger(HeteronymSearchOptionDto.class);

    private final String xmlId4Heteronym;

    public HeteronymSearchOptionDto(@JsonProperty("heteronym") String xmlId) {
        logger.debug("HeteronymSearchOption xmlId: {}", xmlId);
        this.xmlId4Heteronym = xmlId.equals("null") ? null : xmlId;
    }

    public String getXmlId4Heteronym() {
        return this.xmlId4Heteronym;
    }
}
