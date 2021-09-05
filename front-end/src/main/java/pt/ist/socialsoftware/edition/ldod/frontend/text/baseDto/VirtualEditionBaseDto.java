//package pt.ist.socialsoftware.edition.ldod.frontend.text.baseDto;
//
//import org.joda.time.LocalDate;
//import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;
//
//public class VirtualEditionBaseDto {
//
//    private  String xmlId;
//    private  String acronym;
//
//    // cached attributes
//    private String externalId;
//    private String title;
//    private String reference;
//    private String synopsis;
//    private boolean isLdoDEdition;
//    private LocalDate date;
//    private boolean pub;
//    private boolean openVocabulary;
//    private String shortAcronym;
//    private int max;
//
//    public VirtualEditionBaseDto(VirtualEditionDto virtualEditionDto) {
//        this.xmlId = virtualEditionDto.getXmlId();
//        this.acronym = virtualEditionDto.getAcronym();
//        this.externalId = virtualEditionDto.getExternalId();
//        this.title = virtualEditionDto.getTitle();
//        this.reference = virtualEditionDto.getReference();
//        this.synopsis = virtualEditionDto.getSynopsis();
//        this.isLdoDEdition = virtualEditionDto.isLdoDEdition();
//        this.date = virtualEditionDto.getDate();
//        this.pub = virtualEditionDto.getPub();
//        this.openVocabulary = virtualEditionDto.getOpenVocabulary();
//        this.shortAcronym = virtualEditionDto.getShortAcronym();
//        this.max = virtualEditionDto.getMaxFragNumber();
//    }
//
//    public String getXmlId() {
//        return xmlId;
//    }
//
//    public void setXmlId(String xmlId) {
//        this.xmlId = xmlId;
//    }
//
//    public String getAcronym() {
//        return acronym;
//    }
//
//    public void setAcronym(String acronym) {
//        this.acronym = acronym;
//    }
//
//    public String getExternalId() {
//        return externalId;
//    }
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getReference() {
//        return reference;
//    }
//
//    public void setReference(String reference) {
//        this.reference = reference;
//    }
//
//    public String getSynopsis() {
//        return synopsis;
//    }
//
//    public void setSynopsis(String synopsis) {
//        this.synopsis = synopsis;
//    }
//
//    public boolean isLdoDEdition() {
//        return isLdoDEdition;
//    }
//
//    public void setLdoDEdition(boolean ldoDEdition) {
//        isLdoDEdition = ldoDEdition;
//    }
//
//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    public boolean isPub() {
//        return pub;
//    }
//
//    public void setPub(boolean pub) {
//        this.pub = pub;
//    }
//
//    public boolean isOpenVocabulary() {
//        return openVocabulary;
//    }
//
//    public void setOpenVocabulary(boolean openVocabulary) {
//        this.openVocabulary = openVocabulary;
//    }
//
//    public String getShortAcronym() {
//        return shortAcronym;
//    }
//
//    public void setShortAcronym(String shortAcronym) {
//        this.shortAcronym = shortAcronym;
//    }
//
//    public int getMax() {
//        return max;
//    }
//
//    public void setMax(int max) {
//        this.max = max;
//    }
//}
