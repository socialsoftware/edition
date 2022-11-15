package pt.ist.socialsoftware.edition.ldod.bff.reading.dto;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;

public class RecommendedInterDto {

    private String externalId;
    private int number;
    private String xmlId;
    private String urlId;
    private String acronym;

    public RecommendedInterDto(ExpertEditionInter inter) {
        setExternalId(inter.getExternalId());
        setNumber(inter.getNumber());
        setAcronym(inter.getEdition().getAcronym());
        setXmlId(inter.getFragment().getXmlId());
        setUrlId(inter.getUrlId());
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
}
