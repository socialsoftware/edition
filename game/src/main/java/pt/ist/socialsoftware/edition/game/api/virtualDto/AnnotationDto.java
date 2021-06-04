package pt.ist.socialsoftware.edition.game.api.virtualDto;


import org.springframework.web.reactive.function.client.WebClient;

public abstract class AnnotationDto {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    private String quote;
    private String username;
    private String text;
    private String externalId;
    private String interExternalId;
    private String interXmlId;
    private String user;

    public AnnotationDto() {
    }

    public String getQuote() {
        return this.quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getInterExternalId() {
        return interExternalId;
    }

//    public Set<Range> getRangeSet() {
//        return this.virtualProvidesInterface.getRangeSetFromAnnotation(externalId);
//    }

    public String getUser() {
        return user;
    }

    public VirtualEditionDto getVirtualEdition() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + interXmlId + "/virtualEdition")
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(interXmlId);
    }

    public abstract boolean isHumanAnnotation();
}
