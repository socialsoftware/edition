package pt.ist.socialsoftware.edition.notification.event.ui;


//import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
//import pt.ist.socialsoftware.edition.text.domain.ScholarInter;

//public class FragInterDto {
//
//    public enum InterType {
//        AUTHORIAL("authorial"), EDITORIAL("editorial"), VIRTUAL("virtual");
//
//        private final String desc;
//
//        InterType(String desc) {
//            this.desc = desc;
//        }
//
//        public String getDesc() {
//            return this.desc;
//        }
//    }
//
//    private InterType type;
//    private String fragmentXmlId;
//    private String urlId;
//    private String shortName;
//    private String externalId;
//
//
//    public FragInterDto(VirtualEditionInter uses) {
//        setType(InterType.VIRTUAL);
//        setFragmentXmlId(uses.getFragmentXmlId());
//        setUrlId(uses.getUrlId());
//        setShortName(uses.getShortName());
//        setExternalId(uses.getExternalId());
//    }
//
//    public FragInterDto(ScholarInter uses) {
//        setType(uses.isExpertInter() ? InterType.EDITORIAL : InterType.AUTHORIAL);
//        setFragmentXmlId(uses.getFragment().getXmlId());
//        setUrlId(uses.getUrlId());
//        setShortName(uses.getShortName());
//        setExternalId(uses.getExternalId());
//    }
//
//    public InterType getType() {
//        return this.type;
//    }
//
//    public void setType(InterType type) {
//        this.type = type;
//    }
//
//    public String getFragmentXmlId() {
//        return this.fragmentXmlId;
//    }
//
//    public void setFragmentXmlId(String fragmentXmlId) {
//        this.fragmentXmlId = fragmentXmlId;
//    }
//
//    public String getUrlId() {
//        return this.urlId;
//    }
//
//    public void setUrlId(String urlId) {
//        this.urlId = urlId;
//    }
//
//    public String getShortName() {
//        return this.shortName;
//    }
//
//    public void setShortName(String shortName) {
//        this.shortName = shortName;
//    }
//
//    public String getExternalId() {
//        return this.externalId;
//    }
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//}
