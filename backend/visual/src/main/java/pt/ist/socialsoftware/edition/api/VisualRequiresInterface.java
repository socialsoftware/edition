package pt.ist.socialsoftware.edition.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.WeightsDto;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.wrappers.IntersByDistance;
import pt.ist.socialsoftware.edition.notification.dtos.text.*;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterListDto;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.*;

public class VisualRequiresInterface {

    private final WebClient.Builder webClient = WebClient.builder().baseUrl(TEXT_SERVICE_URL);

    // Requires the Text Module

    //Check later
    public List<ExpertEditionInterListDto> getEditionInterListDto() {
        return webClient.build()
                .get()
                .uri("/expertEditionInters")
                .retrieve()
                .bodyToFlux(ExpertEditionInterListDto.class)
                .collectList()
                .block();
        //  return this.textProvidesInterface.getEditionInterListDto();
    }

    public ExpertEditionDto getExpertEditionDto(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/acronym/" + acronym)
                .retrieve()
                .bodyToMono(ExpertEditionDto.class)
                .blockOptional().orElse(null);
        //  return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/" + acronym + "/scholarInterList")
                .retrieve()
                .bodyToFlux(ScholarInterDto.class)
                .collectList()
                .block();
        //  return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean highlightDiff) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/writeFromPlainHtmlWriter4OneInter/")
                        .queryParam("xmlId", xmlId)
                        .queryParam("highlightDiff", highlightDiff)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public ScholarInterDto getScholarInterbyExternalId(String interId) {
        return webClient.build()
                .get()
                .uri("/scholarinter/ext/" + interId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().orElse(null);
    }

    public List<Map.Entry<String, Double>> getScholarInterTermFrequency(ScholarInterDto scholarInterDto) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + scholarInterDto.getXmlId() + "/termFrequency")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map.Entry<String, Double>>() {})
                .collectList()
                .block();
        //  return this.textProvidesInterface.getScholarInterTermFrequency(scholarInterDto);
    }


    // Requires the Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);

    public List<VirtualEditionInterListDto> getPublicVirtualEditionInterListDto() {
        return webClientVirtual.build()
                .get()
                .uri("/publicVirtualEditionInterList")
                .retrieve()
                .bodyToFlux(VirtualEditionInterListDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getPublicVirtualEditionInterListDto();
    }

    public VirtualEditionDto getVirtualEdition(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public ScholarInterDto getScholarInterByExternalIdOfInter(String interId) {
        ScholarInterDto scholarInterDto =
                this.webClientVirtual.build()
                        .get()
                        .uri("/scholarInter/ext/" + interId)
                        .retrieve()
                        .bodyToMono(ScholarInterDto.class)
                        .block();

//                        .getScholarInterbyExternalId(interId);

        if (scholarInterDto == null) {
            scholarInterDto = getScholarInterbyExternalId(interId);
        }

        return scholarInterDto;
    }


    // Requires the Recommendation Module
    private final WebClient.Builder webClientRecommendation = WebClient.builder().baseUrl(RECOMMENDATION_SERVICE_URL);

    public List<InterIdDistancePairDto> getIntersByDistance(String externalId, WeightsDto weights) {

        ScholarInterDto scholarInterDto = getScholarInterbyExternalId(externalId);
        if (scholarInterDto != null) {
            return getScholarIntersByDistance(scholarInterDto, weights);
        }

        VirtualEditionInterDto virtualEditionInterDto = webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/ext/" + externalId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
//                this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
        if (virtualEditionInterDto != null) {
            return geVirtualEditiontIntersByDistance(virtualEditionInterDto, weights);
        }
        return null;
    }


    private List<InterIdDistancePairDto> getScholarIntersByDistance(ScholarInterDto scholarInterDto, WeightsDto weightsDto) {
        return webClientRecommendation.build()
                .post()
                .uri("/scholarIntersByDistance")
                .bodyValue(new IntersByDistance(scholarInterDto, weightsDto))
                .retrieve()
                .bodyToFlux(InterIdDistancePairDto.class)
                .collectList()
                .block();
    }

    private List<InterIdDistancePairDto> geVirtualEditiontIntersByDistance(VirtualEditionInterDto virtualEditionInterDto, WeightsDto weightsDto) {
        return webClientRecommendation.build()
                .post()
                .uri("/virtualEditionIntersByDistance")
                .bodyValue(new IntersByDistance(virtualEditionInterDto, weightsDto))
                .retrieve()
                .bodyToFlux(InterIdDistancePairDto.class)
                .collectList()
                .block();
    }


    // Test Method Calls

    public List<HeteronymDto> getSortedHeteronyms() {
        return  webClient.build()
                .get()
                .uri("/sortedHeteronyms")
                .retrieve()
                .bodyToFlux(HeteronymDto.class).toStream()
                .collect(Collectors.toList());
    }

    public void getLoaderTEICorpus(InputStream file) {
        try {
            webClient.build().post()
                    .uri("/loadTEICorpus")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(file.readAllBytes())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTEICorpusVirtual(InputStream inputStream) {
        try {
            webClientVirtual.build()
                    .post()
                    .uri("/loadTEICorpusVirtual")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(inputStream.readAllBytes())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importVirtualEditionCorupus(InputStream inputStream) {
        try {
            webClientVirtual.build()
                    .post()
                    .uri("/importVirtualEditionCorpus")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(inputStream.readAllBytes())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        this.virtualProvidesInterface.importVirtualEditionCorpus(inputStream);
    }

    public String getLoadTEIFragmentsAtOnce(InputStream file) {
        try {
            return webClient.build()
                    .post()
                    .uri("/loadFragmentsAtOnce")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(file.readAllBytes())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void getFragmentCorpusGenerator() {
        Set<FragmentDto> fragments = webClient.build()
                .get()
                .uri("/fragmentDtosWithScholarInterDtos")
                .retrieve()
                .bodyToFlux(FragmentDto.class).toStream()
                .collect(Collectors.toSet());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(fragments);
            webClientVirtual.build()
                    .post()
                    .uri("/loadTEIFragmentCorpus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(json)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //       this.virtualProvidesInterface.loadTEIFragmentCorpus(fragments);
    }

    public void cleanFragmentMapCache() {
        webClient.build()
                .get()
                .uri("/cleanFragmentMapCache")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void cleanScholarInterMapCache() {
        webClient.build()
                .get()
                .uri("/cleanScholarInterMapCache")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void cleanVirtualEditionInterMapByUrlIdCache() {
        webClientVirtual.build()
                .post()
                .uri("/cleanVirtualEditionInterMapByUrlIdCache")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void cleanVirtualEditionInterMapByXmlIdCache() {
        webClientVirtual.build()
                .post()
                .uri("/cleanVirtualEditionInterMapByXmlIdCache")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void cleanVirtualEditionMapCache() {
        webClientVirtual.build()
                .post()
                .uri("/cleanVirtualEditionMapCache")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void removeTextModule() {
        webClient.build()
                .post()
                .uri("/removeModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void removeVirtualModule() {
        webClientVirtual.build()
                .post()
                .uri("/removeVirtualModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void importFragmentFromTEI(InputStream inputStream) {
        try {
            webClientVirtual.build()
                    .post()
                    .uri("/importVirtualEditionFragmentFromTEI")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(inputStream.readAllBytes())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExpertEditionDto getExpertEditionByAcronym(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/acronym/" + acronym)
                .retrieve()
                .bodyToMono(ExpertEditionDto.class)
                .blockOptional().orElse(null);
    }

    public VirtualEditionDto getArchiveEdition() {
        return webClientVirtual.build()
                .get()
                .uri("/archiveVirtualEdition")
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
//        return this.virtualProvidesInterface.getArchiveVirtualEdition();
    }

    public boolean initializeTextModule() {
        return webClient.build().get()
                .uri("/initializeTextModule")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //return this.textProvidesInterface.initializeTextModule();
    }

    public boolean initializeVirtualModule() {
        return webClientVirtual.build()
                .post()
                .uri("/initializeVirtualModule")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //        return this.virtualProvidesInterface.initializeVirtualModule();
    }
}
