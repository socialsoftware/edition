package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.CitationDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;


import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FeVirtualRequiresInterface {
    // Uses Frontend User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses User Module
    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://localhost:8082/api");
//    private final WebClient.Builder webClientUser = WebClient.builder().baseUrl("http://docker-user:8082/api");


    public UserDto getUser(String username) {
        return webClientUser.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .blockOptional()
                .orElse(null);
        //        return this.userProvidesInterface.getUser(username);
    }


    // Uses Virtual Module
    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
//        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/virtualEditions/getPublicVirtualEditionsOrUserIsParticipant")
                .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .toStream().collect(Collectors.toSet());
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

    // Uses Text Module
    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://localhost:8081/api");
//    public WebClient.Builder webClient = WebClient.builder().baseUrl("http://docker-text:8081/api");


    public List<CitationDto> getCitationsWithInfoRanges() {
        return   webClient.build()
                .get()
                .uri("/citationsInfoRanges")
                .retrieve()
                .bodyToFlux(CitationDto.class).toStream().collect(Collectors.toList());
        //        return this.textProvidesInterface.getCitationsWithInfoRanges();
    }

    public void createVirtualEdition(String authenticatedUser, String acronym, String title, LocalDate date, boolean pub, String usedAcronym) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/createVirtualEdition")
                .queryParam("username", authenticatedUser)
                .queryParam("acronym", acronym)
                .queryParam("title", title)
                .queryParam("pub", pub)
                .queryParam("acronymOfUsed", usedAcronym)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //         this.virtualProvidesInterface.createVirtualEdition(authenticatedUser, acronym, title, date, pub, usedAcronym, null);
    }

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/ext/" + externalId)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public void searchForAwareAnnotations(String externalId) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/searchForAwareAnnotations")
                .queryParam("externalId", externalId)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.searchForAwareAnnotations(externalId);
    }

    public TopicListDTO generateTopicModeler(String username, String externalId, int numTopics, int numWords, int thresholdCategories, int numIterations) throws IOException {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/generateTopicModeler")
                .queryParam("username", username)
                .queryParam("externalId", externalId)
                .queryParam("numTopics", numTopics)
                .queryParam("numWords", numWords)
                .queryParam("thresholdCategories", thresholdCategories)
                .queryParam("numIterations", numIterations)
                .build())
                .retrieve()
                .bodyToMono(TopicListDTO.class)
                .block();
        //        this.virtualProvidesInterface.generateTopicModeler(username, externalId, numTopics, numWords, thresholdCategories, numIterations);
    }

    public TaxonomyDto getTaxonomyByExternalId(String externalId) {
        return webClientVirtual.build()
                .get()
                .uri("/taxonomy/ext/" + externalId)
                .retrieve()
                .bodyToMono(TaxonomyDto.class)
                .block();
        //        return this.virtualProvidesInterface.getTaxonomyByExternalId(externalId);
    }

    public CategoryDto getCategoryByExternalId(String externalId) {
        return webClientVirtual.build()
                .get()
                .uri("/category/ext/" + externalId)
                .retrieve()
                .bodyToMono(CategoryDto.class)
                .block();
        //        return this.virtualProvidesInterface.getCategoryByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/ext/" + externalId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public String getWriteVirtualEditionToFileExport() throws IOException {
        return webClientVirtual.build()
                .get()
                .uri("/writeVirtualEditionToFileExport")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getWriteVirtualEditionToFileExport();
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

    public List<VirtualEditionDto> getVirtualEditionsList() {
//        return this.virtualProvidesInterface.getVirtualEditions().stream()
//                .sorted((v1, v2) -> v1.getAcronym().compareTo(v2.getAcronym())).collect(Collectors.toList());
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditions")
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .collectList()
                .block().stream()
                .sorted((v1, v2) -> v1.getAcronym().compareTo(v2.getAcronym())).collect(Collectors.toList());
    }

    public List<TwitterCitationDto> getAllTwitterCitations() {
        return webClientVirtual.build()
                .get()
                .uri("/allTwitterCitations")
                .retrieve()
                .bodyToFlux(TwitterCitationDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getAllTwitterCitations();
    }

    public Set<TweetDto> getAllTweets() {
        return webClientVirtual.build()
                .get()
                .uri("/allTweets")
                .retrieve()
                .bodyToFlux(TweetDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getAllTweets();
    }

    public void removeTweets() {
        webClientVirtual.build()
                .post()
                .uri("/removeTweets")
                .bodyValue(Void.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
//        this.virtualProvidesInterface.removeTweets();
    }

    public void detectCitation() throws IOException {
        webClientVirtual.build()
                .post()
                .uri("/detectCitation")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.detectCitation();
    }

    public void createTweetFactory() throws IOException {
        webClientVirtual.build()
                .post()
                .uri("/createTweetFactory")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.createTweetFactory();
    }

    public void generateAwareAnnotations() throws IOException {
        webClientVirtual.build()
                .post()
                .uri("/generateAwareAnnotations")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.generateAwareAnnotations();
    }

    public void dailyRegenerateTwitterCitationEdition() {
        webClientVirtual.build()
                .post()
                .uri("/dailyRegenerateTwitterCitationEdition")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.dailyRegenerateTwitterCitationEdition();
    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym)
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }


    public VirtualEditionInterListDto getVirtualEditionInterList(String acronym, boolean deep) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/virtualEdition/" + acronym + "/interList")
                .queryParam("deep", deep)
                .build())
                .retrieve()
                .bodyToMono(VirtualEditionInterListDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterList(acronym, deep);
    }

    public List<VirtualEditionDto> getVirtualEditionsUserIsParticipantSelectedOrPublic(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/virtualEditionsUserIsParticipantSelectedOrPublic")
                .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionsUserIsParticipantSelectedOrPublic(username);
    }

    //Uses Virtual and Game Module
    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();

    public String importVirtualEditionFragmentFromTEI(MultipartFile file) throws IOException {
        String result = webClientVirtual.build()
                .post()
                .uri("/importVirtualEditionFragmentFromTEI")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(file.getInputStream().readAllBytes())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        this.gameProvidesInterface.importGamesFromTEI(file.getInputStream());
        return result;
    }

    public String importVirtualEditionFragmentFromTEI(File file) throws IOException {
        String result = webClientVirtual.build()
                .post()
                .uri("/importVirtualEditionFragmentFromTEI")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(new FileInputStream(file).readAllBytes())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        this.gameProvidesInterface.importGamesFromTEI(new FileInputStream(file));
        return result;
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

    public void fetchCitationsFromTwitter() throws IOException {
        webClientVirtual.build()
                .post()
                .uri("/fetchCitationsFromTwitter")
                .retrieve()
                .bodyToMono(Void.class)
                .blockOptional();
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterSet() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInterSet")
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public Set<VirtualEditionDto> getVirtualEditions() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditions")
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public boolean isInterInVirtualEdition(String xmlId, String acronym) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/isInterInVirtualEdition")
                    .queryParam("xmlId", xmlId)
                    .queryParam("acronym", acronym)
                    .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public void associateVirtualEditionInterCategories(String xmlId, String username, Set<String> categories) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/associateVirtualEditionInterCategories")
                    .queryParam("xmlId", xmlId)
                    .queryParam("username", username)
                    .queryParam("categories", categories)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void dissociateVirtualEditionInterCategory(String xmlId, String username, String categoryExternalId) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/dissociateVirtualEditionInterCategory")
                    .queryParam("xmlId", xmlId)
                    .queryParam("username", username)
                    .queryParam("categoryExternalId", categoryExternalId)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public HumanAnnotationDto getHumanAnnotationfromId(String id) {
        return webClientVirtual.build()
                .get()
                .uri("/humanAnnotation/ext/" + id)
                .retrieve()
                .bodyToMono(HumanAnnotationDto.class)
                .block();
    }

    public boolean canManipulateAnnotation(String acronym, String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + acronym + "/canManipulateAnnotation")
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public HumanAnnotationDto createHumanAnnotation(String xmlId, String quote, String text, String user, List<RangeJson> ranges, List<String> tags) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createHumanAnnotation")
                    .queryParam("xmlId", xmlId)
                    .queryParam("quote", quote)
                    .queryParam("text", text)
                    .queryParam("user", user)
                    .queryParam("tags", tags)
                .build())
                .bodyValue(BodyInserters.fromValue(ranges))
                .retrieve()
                .bodyToMono(HumanAnnotationDto.class)
                .block();
    }

    public boolean canUserUpdateHumanAnnotation(String id, String user) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/humanAnnotation/" + id + "/canUserUpdate")
                    .queryParam("user", user)
                .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public HumanAnnotationDto updateHumanAnnotation(String id, String text, List<String> tags) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/humanAnnotation/" + id + "/update")
                    .queryParam("text", text)
                    .queryParam("tags", tags)
                .build())
                .retrieve()
                .bodyToMono(HumanAnnotationDto.class)
                .block();
    }

    public boolean canUserDeleteHumanAnnotation(String id, String user) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/humanAnnotation/" + id + "/canUserDelete")
                    .queryParam("user", user)
                .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public void removeHumanAnnotation(String id) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/removeHumanAnnotation")
                    .queryParam("id", id)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/urlId/" + urlId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
    }

    public TaxonomyDto getVirtualEditionTaxonomy(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/taxonomy")
                .retrieve()
                .bodyToMono(TaxonomyDto.class)
                .block();
    }

    public Set<VirtualEditionInterDto> getVirtualEditionIntersUserIsContributor(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionsInters/getVirtualEditionIntersUserIsContributor")
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream()
                .collect(Collectors.toSet());
    }

    public String getVirtualEditionTitleByAcronym(String acronym) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + acronym + "/title")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getVirtualEditionInterTitle(String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + xmlId + "/title")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    // Test remote calls

    public TwitterCitationDto createTwitterCitation(String fragmentXmlId, String sourceLink, String date, String fragText, String tweetText,
                                                    long tweetID, String location, String country, String username, String profURL, String profImgURL) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder ->  uriBuilder
                        .path("/createTwitterCitation")
                        .queryParam("fragmentXmlId", fragmentXmlId)
                        .queryParam("sourceLink", sourceLink)
                        .queryParam("date", date)
                        .queryParam("fragText", fragText)
                        .queryParam("tweetText", tweetText)
                        .queryParam("tweetID", tweetID)
                        .queryParam("location", location)
                        .queryParam("country", country)
                        .queryParam("username", username)
                        .queryParam("profURL", profURL)
                        .queryParam("profImgURL", profImgURL)
                        .build())
                .retrieve()
                .bodyToMono(TwitterCitationDto.class)
                .block();
    }


    public void createTweet(String sourceLink, String date, String tweetText, long tweetID, String location,
                            String country, String username, String profURL, String profImgURL, long originalTweetID, boolean isRetweet) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createTweet")
                    .queryParam("sourceLink", sourceLink)
                    .queryParam("date", date)
                    .queryParam("tweetText", tweetText)
                    .queryParam("tweetID", tweetID)
                    .queryParam("location", location)
                    .queryParam("country", country)
                    .queryParam("username", username)
                    .queryParam("profURL", profURL)
                    .queryParam("profImgURL", profImgURL)
                    .queryParam("originalTweetID", originalTweetID)
                    .queryParam("isRetweet", isRetweet)
                .build())
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

    public VirtualEditionInterDto getVirtualEditionInterByXmlId(String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/xmlId/" + xmlId)
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
    }

    public String exportFragment(String fragXmlId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/exportVirtualEditionFragments")
                    .queryParam("fragXmlId", fragXmlId)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Set<VirtualEditionInterDto> getVirtualEditionInterSetFromFragment(String xmlId) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionIntersFromFragment")
                    .queryParam("fragXmlId", xmlId)
                .build())
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .toStream().collect(Collectors.toSet());
    }

    public TwitterCitationDto createTwitterCitationFromCitation(CitationDto citation) {
        return webClientVirtual.build()
                .post()
                .uri("/createTwitterCitationFromCitation")
                .bodyValue(BodyInserters.fromValue(citation))
                .retrieve()
                .bodyToMono(TwitterCitationDto.class)
                .block();
    }

    public void createInfoRange(long id, String xmlId, String s, int htmlStart, String s1, int htmlEnd, String infoQuote, String infoText) {
        webClient.build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/createInfoRange")
                        .queryParam("id", id)
                        .queryParam("xmlId", xmlId)
                        .queryParam("s", s)
                        .queryParam("htmlStart", htmlStart)
                        .queryParam("s1", s1)
                        .queryParam("htmlEnd", htmlEnd)
                        .queryParam("infoQuote", infoQuote)
                        .queryParam("infoText", infoText)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public String exportVirtualEditionsTEICorpus() {
        return webClientVirtual.build()
                .get()
                .uri("/exportVirtualEditionsTEICorpus")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void createMediaSource(String editionAcronym, String name) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEdition/" + editionAcronym + "/createMediaSource")
                    .queryParam("name", name)
                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void createTimeWindow(String editionAcronym, LocalDate beginDate, LocalDate endDate) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + editionAcronym + "/createTimeWindow")
                        .queryParam("beginDate", beginDate)
                        .queryParam("endDate", endDate)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void createGeographicLocation(String editionAcronym, String country, String location) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + editionAcronym + "/createGeographicLocation")
                        .queryParam("country", country)
                        .queryParam("location", location)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void createFrequency(String editionAcronym, int frequency) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEdition/" + editionAcronym + "/createFrequency")
                        .queryParam("frequency", frequency)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public int citationDetecterLastIndexOfCapitalLetter(String teste, int i) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/citationDetecterLastIndexOfCapitalLetter")
                    .queryParam("teste", teste)
                    .queryParam("i", i)
                .build())
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
    }

    public List<String> patternFinding(String text, String pattern ) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/citationDetecterPatternFinding")
                        .queryParam("text", text)
                        .queryParam("pattern", pattern)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    public List<String> maxJaroValue(String text, String wordToFind) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/citationDetecterPatternFinding")
                        .queryParam("text", text)
                        .queryParam("wordToFind", wordToFind)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    public String cleanTweetText(String s) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/citationDetecterCleanTweetText")
                        .queryParam("s", s)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public int countOccurencesOfSubstring(String string, String substring, int position) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/citationDetecterCountOccurencesOfSubstring")
                        .queryParam("string", string)
                        .queryParam("substring", substring)
                        .queryParam("position", position)
                        .build())
                .retrieve()
                .bodyToMono(Integer.class)
                .blockOptional().orElse(0);
    }

    public boolean startBiggerThanEnd(int hStart, int hEnd, int nStart, int nEnd ) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/citationDetecterStartBiggerThanEnd")
                        .queryParam("hStart", hStart)
                        .queryParam("hEnd", hEnd)
                        .queryParam("nStart", nStart)
                        .queryParam("nEnd", nEnd)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .blockOptional().orElse(false);
    }

    public List<String> checkTitleStmtLoad() {
        return webClientVirtual.build()
                .get()
                .uri("/checkTitleStmtLoad")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
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
}