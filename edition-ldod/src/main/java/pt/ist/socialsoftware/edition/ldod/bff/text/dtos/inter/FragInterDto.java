package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;

public abstract class FragInterDto {

    private String externalId;
    private String title;
    private String xmlId;
    private String urlId;
    private String date = null;
    private String precision;
    private String shortName;

    private String editionTitle;
    private int number;


    public FragInterDto() {
    }

    public FragInterDto(FragInter fragInter) {
        setEditionTitle(StringEscapeUtils.unescapeHtml(fragInter.getEdition().getTitle()));
        setExternalId(fragInter.getExternalId());
        setXmlId(fragInter.getFragment().getXmlId());
        setUrlId(fragInter.getUrlId());
        setDate(fragInter.getLdoDDate());
        setNumber(fragInter.getNumber());
    }

    public int getNumber() {
        return number;
    }

    public String getEditionTitle() {
        return editionTitle;
    }

    public void setEditionTitle(String editionTitle) {
        this.editionTitle = editionTitle;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
