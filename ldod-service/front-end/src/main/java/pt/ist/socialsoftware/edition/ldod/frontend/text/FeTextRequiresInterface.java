package pt.ist.socialsoftware.edition.ldod.frontend.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.*;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class FeTextRequiresInterface {

    @Autowired
//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");



    // Uses Frontend User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses Text Module


    public FragmentDto getFragmentByXmlId(String xmlId) {
        return   webClient.build()
                .get()
                .uri("/fragment/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().orElse(null);
    }

    public FragmentDto getFragmentByExternalId(String externalId) {
        return   webClient.build()
                .get()
                .uri("/fragment/ext/" + externalId)
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .blockOptional().orElse(null);
    }

    public ScholarInterDto getScholarInterByExternalId(String externalId) {
        return   webClient.build()
                .get()
                .uri("/scholarinter/ext/" + externalId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional().orElse(null);
    }

    public ScholarInterDto getScholarInterByXmlId(String xmlId) {
        return webClient.build()
                .get()
                .uri("/scholarInter/" + xmlId)
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .blockOptional()
                .orElse(null);
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return   webClient.build()
                .get()
                .uri("/sortedExpertEditions")
                .retrieve()
                .bodyToFlux(ExpertEditionDto.class).toStream()
                .collect(Collectors.toList());
    }

    public ExpertEditionDto getExpertEditionByAcronym(String acronym) {
        return webClient.build()
                .get()
                .uri("/expertEdition/acronym/" + acronym)
                .retrieve()
                .bodyToMono(ExpertEditionDto.class)
                .blockOptional().orElse(null);
    }

    public ExpertEditionDto getExpertEditionByExternalId(String externalId) {
        return webClient.build()
                .get()
                .uri("/expertEdition/ext/" + externalId)
                .retrieve()
                .bodyToMono(ExpertEditionDto.class)
                .blockOptional().orElse(null);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return webClient.build()
                .get()
                .uri("/fragments")
                .retrieve()
                .bodyToFlux(FragmentDto.class).toStream()
                .collect(Collectors.toSet());
    }

    public List<FragmentDto> getFragmentDtosWithSourceDtos() {
     return   webClient.build()
                .get()
                .uri("/fragmentDtosWithSourceDtos")
                .retrieve()
                .bodyToFlux(FragmentDto.class).toStream().collect(Collectors.toList());
    }

    public Set<FragmentDto> getFragmentDtosWithScholarInterDtos() {
        return   webClient.build()
                .get()
                .uri("/fragmentDtosWithScholarInterDtos")
                .retrieve()
                .bodyToFlux(FragmentDto.class).toStream().collect(Collectors.toSet());
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

    public String getLoadTEIFragmentsStepByStep(InputStream file) {

        try {
            return webClient.build()
                    .post()
                    .uri("/loadFragmentsStepByStep")
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
                .blockOptional().orElse(null);
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean displayDiff, boolean displayDel, boolean highlightIns, boolean highlightSubst, boolean showNotes, boolean showFacs, String pbText) {
        return webClient.build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/writeFromPlainHtmlWriter4OneInter/")
                        .queryParam("xmlId", xmlId)
                        .queryParam("displayDiff", displayDiff)
                        .queryParam("displayDel", displayDel)
                        .queryParam("highlightIns", highlightIns)
                        .queryParam("highlightSubst", highlightSubst)
                        .queryParam("showNotes", showNotes)
                        .queryParam("showFacs", showFacs)
                        .queryParam("pbTextId", pbText)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse(null);
    }

    public void removeFragmentByExternalId(String externalId) {
        webClient.build()
                .delete()
                .uri("/fragment/ext/" + externalId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public String getAppTranscription(String xmlId, String externalAppId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/appTranscriptionFromHtmlWriter4Variations")
                        .queryParam("xmlId", xmlId)
                        .queryParam("externalAppId",  externalAppId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional()
                .orElse(null);
    }


    public List<String> putAppTextWithVariations(String externalId, List<String> scholarInters) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/appTextWithVariations")
                        .queryParam("externalId", externalId)
                        .queryParam("scholarInters", scholarInters)
                        .build())
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
    }

    public String exportWithQueryExpertEditionTEI(String query) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/exportExpertEdition")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String exportAllExpertEditionTEI() {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/exportAllExpertEdition")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String exportRandomExpertEditionTEI() {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/exportRandomExpertEdition")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public boolean isDomainObjectScholarInter(String interId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/isScholarInter")
                        .queryParam("interId", interId)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public boolean isDomainObjectScholarEdition(String externalId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/isScholarEdition")
                        .queryParam("externalId", externalId)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public String getScholarEditionAcronymbyExternal(String externalId) {
        return   webClient.build().get()
                .uri("/scholarEdition/acronym/ext/" + externalId)
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional().orElse(null);
//        return  this.textProvidesInterface.getScholarEditionAcronymbyExternal(externalId);
    }

    public String getWriteFromHtmlWriter2CompIntersLineByLine(List<ScholarInterDto> scholarInters, boolean lineByLine, boolean showSpaces) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                    .path("/writeFromHtmlWriter2CompIntersLineByLine")
                        .queryParam("scholarInters", scholarInters.stream().map(ScholarInterDto::getExternalId).collect(Collectors.toList()))
                        .queryParam("lineByLine", lineByLine)
                        .queryParam("showSpaces", showSpaces)
                .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getTranscriptionFromHtmlWriter2CompInters(List<ScholarInterDto> scholarInters, ScholarInterDto inter, boolean lineByLine, boolean showSpaces) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/transcriptionFromHtmlWriter2CompInters")
                        .queryParam("scholarInters", scholarInters.stream().map(ScholarInterDto::getExternalId).collect(Collectors.toList()))
                        .queryParam("inter", inter.getExternalId())
                        .queryParam("lineByLine", lineByLine)
                        .queryParam("showSpaces", showSpaces)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public SurfaceDto getSurfaceFromPbTextId(String pbTextId, String interId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/surface")
                        .queryParam("pbTextId", pbTextId)
                        .queryParam("interID", interId)
                        .build())
                .retrieve()
                .bodyToMono(SurfaceDto.class)
                .block();
    }

    public SurfaceDto getPrevSurface(String pbTextID, String interId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/surface/prev")
                        .queryParam("pbTextId", pbTextID)
                        .queryParam("interID", interId)
                        .build())
                .retrieve()
                .bodyToMono(SurfaceDto.class)
                .block();
    }

    public SurfaceDto getNextSurface(String pbTextID, String interId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/surface/next")
                        .queryParam("pbTextId", pbTextID)
                        .queryParam("interID", interId)
                        .build())
                .retrieve()
                .bodyToMono(SurfaceDto.class)
                .block();
    }

    public String getPrevPbTextExternalId(String pbTextID, String interId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/prevPb/prev/externalId")
                        .queryParam("pbTextId", pbTextID)
                        .queryParam("interID", interId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getNextPbTextExternalId(String pbTextID, String interId) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/prevPb/next/externalId")
                        .queryParam("pbTextId", pbTextID)
                        .queryParam("interID", interId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public HeteronymDto getHeteronymByExternalId(String externalId) {
        return  webClient.build()
                .get()
                .uri("/heteronym/ext/" + externalId)
                .retrieve()
                .bodyToMono(HeteronymDto.class)
                .blockOptional().orElse(null);
    }


    public List<String> getSourceInterFacUrls(String xmlId) {
        return webClient.build().get()
                .uri("/sourceInter/{xmlId}/facUrls")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
    }

    public Map<String, String> getMultipleInterTranscription(List<String> externalIds, boolean lineByLine, boolean showSpaces) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/multipleInterTranscription")
                        .queryParam("externalIds", externalIds)
                        .queryParam("lineByLine", lineByLine)
                        .queryParam("showSpaces", showSpaces)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }

    public Map<String, String> getMultipleInterVariations(List<String> externalIds) {
        return webClient.build().get()
                .uri( uriBuilder -> uriBuilder
                        .path("/multipleInterVariations")
                        .queryParam("externalIds", externalIds)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }

    public boolean isExpertEdition(String acronym) {
        return webClient.build().get()
                .uri("/expertEdition/" + acronym + "/isExpert")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }



    // Uses Virtual Edition Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByUrlId(urlId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public void getLoaderTEICorpusVirtual(InputStream file) {
        this.virtualProvidesInterface.loadTEICorpusVirtual(file);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragmentCorpusGenerator() {
       Set<FragmentDto> fragments = this.getFragmentDtosWithScholarInterDtos();
       this.virtualProvidesInterface.loadTEIFragmentCorpus(fragments);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public boolean initializeTextModule() {
        return webClient.build().get()
                .uri("/initializeTextModule")
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
        //return this.textProvidesInterface.initializeTextModule();
    }


    public List<HeteronymDto> getSortedHeteronyms() {
        return  webClient.build()
                .get()
                .uri("/sortedHeteronyms")
                .retrieve()
                .bodyToFlux(HeteronymDto.class).toStream().collect(Collectors.toList());
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

    public void removeTextModule() {
        webClient.build()
                .post()
                .uri("/removeModule")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public FragmentDto createFragment(String title, String xmlId) {
        return webClient.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/createFragment")
                        .queryParam("title", title)
                        .queryParam("xmlId", xmlId)
                        .build())
                .retrieve()
                .bodyToMono(FragmentDto.class)
                .block();
    }

    public ScholarInterDto createExpertInter(String fragXmlId, String acronym, String xmlId) {
        return webClient.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/createExpertInter")
                        .queryParam("fragXmlId", fragXmlId)
                        .queryParam("acronym", acronym)
                        .queryParam("xmlId", xmlId)
                        .build())
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .block();
    }

    public ScholarInterDto createSourceInter(String fragXmlId, String xmlId) {
        return webClient.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/createSourceInter")
                        .queryParam("fragXmlId", fragXmlId)
                        .queryParam("xmlId", xmlId)
                        .build())
                .retrieve()
                .bodyToMono(ScholarInterDto.class)
                .block();
    }
}