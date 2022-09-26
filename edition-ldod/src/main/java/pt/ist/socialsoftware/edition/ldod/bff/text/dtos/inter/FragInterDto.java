package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;

public abstract class FragInterDto {

    private String externalId;

    private String title;
    private String xmlId;
    private String urlId;
    private String date = null;
    private String precision;

    public FragInterDto(FragInter fragInter) {
        setExternalId(fragInter.getExternalId());
        setXmlId(fragInter.getFragment().getXmlId());
        setUrlId(fragInter.getUrlId());
        setDate(fragInter.getLdoDDate());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public void setDate(LdoDDate date) {
        if (date != null) {
            setPrecision(date);
            this.date = date.print();
        }
    }

    public void setPrecision(LdoDDate date) {
        this.precision = date.getPrecision() != null ? date.getPrecision().getDesc() : null;
    }

    public String getDate() {
        return date;
    }

    public String getPrecision() {
        return precision;
    }
}
