package pt.ist.socialsoftware.edition.ldod.frontend.text.textDto;


import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class CitationDto {

    private final WebClient.Builder webClientText = WebClient.builder().baseUrl("http://localhost:8081/api");

    private long id;
    private String externalId;
    private String date;
    private String fragmentXmlId;
    private String fragmentTitle;
    private String sourceLink;
    private boolean hasNoInfoRange;

    public CitationDto() {
        super();
    }

    public long getId() {
        return id;
    }

    public boolean isTwitterCitation() {
        return false;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getDate() {
        return date;
    }

    public String getFragmentXmlId() {
        return fragmentXmlId;
    }

    public String getFragmentTitle() {
        return fragmentTitle;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public boolean isHasNoInfoRange() {
        return hasNoInfoRange;
    }

    public LocalDateTime getFormatedDate() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return LocalDateTime.parse(getDate(), formater);
    }

    public void remove() {
        webClientText.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeCitation")
                    .queryParam("externalId", externalId)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public int getNumberOfTimesCited() {
        return webClientText.build()
                .get()
                .uri("/citation/" + id + "/infoRange")
                .retrieve()
                .bodyToFlux(InfoRangeDto.class)
                .toStream()
                .collect(Collectors.toSet()).size();
    }
}
