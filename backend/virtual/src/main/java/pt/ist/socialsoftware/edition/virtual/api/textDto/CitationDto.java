//package pt.ist.socialsoftware.edition.virtual.api.textDto;
//
//
//
//import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class CitationDto {
//
//
//
//    private long id;
//    private String externalId;
//    private String date;
//    private String fragmentXmlId;
//    private String fragmentTitle;
//    private String sourceLink;
//    private boolean hasNoInfoRange;
//
//    public CitationDto() {
//        super();
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public boolean isTwitterCitation() {
//        return false;
//    }
//
//    public String getExternalId() {
//        return externalId;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public String getFragmentXmlId() {
//        return fragmentXmlId;
//    }
//
//    public String getFragmentTitle() {
//        return fragmentTitle;
//    }
//
//    public String getSourceLink() {
//        return sourceLink;
//    }
//
//    public boolean isHasNoInfoRange() {
//        return hasNoInfoRange;
//    }
//
//    public LocalDateTime getFormatedDate() {
//        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
//        return LocalDateTime.parse(getDate(), formater);
//    }
//
//    public void remove() {
//        VirtualRequiresInterface.getInstance().removeAllCitations();
//    }
//
//    public int getNumberOfTimesCited() {
//        return VirtualRequiresInterface.getInstance().getInfoRangeDtoSetFromCitation(id).size();
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public void setFragmentXmlId(String fragmentXmlId) {
//        this.fragmentXmlId = fragmentXmlId;
//    }
//
//    public void setFragmentTitle(String fragmentTitle) {
//        this.fragmentTitle = fragmentTitle;
//    }
//
//    public void setSourceLink(String sourceLink) {
//        this.sourceLink = sourceLink;
//    }
//
//    public void setHasNoInfoRange(boolean hasNoInfoRange) {
//        this.hasNoInfoRange = hasNoInfoRange;
//    }
//}
