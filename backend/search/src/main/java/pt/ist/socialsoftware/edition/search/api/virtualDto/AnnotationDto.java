//package pt.ist.socialsoftware.edition.search.api.virtualDto;
//
//
//import org.springframework.web.reactive.function.client.WebClient;
//
//public abstract class AnnotationDto {
//
//    private String quote;
//    private String username;
//    private String text;
//    private String externalId;
//    private String interExternalId;
//    private String interXmlId;
//    private String user;
//
//    public AnnotationDto() {
//    }
//
//    public String getQuote() {
//        return this.quote;
//    }
//
//    public void setQuote(String quote) {
//        this.quote = quote;
//    }
//
//    public String getUsername() {
//        return this.username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public String getExternalId() {
//        return externalId;
//    }
//
//    public String getInterExternalId() {
//        return interExternalId;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public abstract boolean isHumanAnnotation();
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//
//    public void setInterExternalId(String interExternalId) {
//        this.interExternalId = interExternalId;
//    }
//
//    public void setInterXmlId(String interXmlId) {
//        this.interXmlId = interXmlId;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
//    }
//}
