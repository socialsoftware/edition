package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import org.joda.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.socialsoftware.edition.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;


import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.CitationDto;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public UserDto getUser(String username) {
        return this.userProvidesInterface.getUser(username);
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

    public VirtualEditionDto getArchiveEdition() {
        return this.virtualProvidesInterface.getArchiveVirtualEdition();
    }

    // Uses Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public List<CitationDto> getCitationsWithInfoRanges() {
        return this.textProvidesInterface.getCitationsWithInfoRanges();
    }

    public void createVirtualEdition(String authenticatedUser, String acronym, String title, LocalDate date, boolean pub, String usedAcronym) {
         this.virtualProvidesInterface.createVirtualEdition(authenticatedUser, acronym, title, date, pub, usedAcronym, null);
    }

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public void searchForAwareAnnotations(String externalId) {
        this.virtualProvidesInterface.searchForAwareAnnotations(externalId);
    }

    public void generateTopicModeler(String username, String externalId, int numTopics, int numWords, int thresholdCategories, int numIterations) throws IOException {
        this.virtualProvidesInterface.generateTopicModeler(username, externalId, numTopics, numWords, thresholdCategories, numIterations);
    }

    public TaxonomyDto getTaxonomyByExternalId(String externalId) {
        return this.virtualProvidesInterface.getTaxonomyByExternalId(externalId);
    }

    public CategoryDto getCategoryByExternalId(String externalId) {
        return this.virtualProvidesInterface.getCategoryByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public String getWriteVirtualEditionToFileExport() throws IOException {
        return this.virtualProvidesInterface.getWriteVirtualEditionToFileExport();
    }

    public void importVirtualEditionCorupus(InputStream inputStream) {
        this.virtualProvidesInterface.importVirtualEditionCorpus(inputStream);
    }

    public List<VirtualEditionDto> getVirtualEditionsList() {
        return this.virtualProvidesInterface.getVirtualEditions().stream()
                .sorted((v1, v2) -> v1.getAcronym().compareTo(v2.getAcronym())).collect(Collectors.toList());
    }

    public List<TwitterCitationDto> getAllTwitterCitations() {
        return this.virtualProvidesInterface.getAllTwitterCitations();
    }

    public Set<TweetDto> getAllTweets() {
        return this.virtualProvidesInterface.getAllTweets();
    }

    public void removeTweets() {
        this.virtualProvidesInterface.removeTweets();
    }

    public void detectCitation() throws IOException {
        this.virtualProvidesInterface.detectCitation();
    }

    public void createTweetFactory() throws IOException {
        this.virtualProvidesInterface.createTweetFactory();
    }

    public void generateAwareAnnotations() throws IOException {
        this.virtualProvidesInterface.generateAwareAnnotations();
    }

    public void dailyRegenerateTwitterCitationEdition() {
        this.virtualProvidesInterface.dailyRegenerateTwitterCitationEdition();
    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public VirtualEditionInterListDto getVirtualEditionInterList(String acronym, boolean deep) {
        return this.virtualProvidesInterface.getVirtualEditionInterList(acronym, deep);
    }

    public List<VirtualEditionDto> getVirtualEditionsUserIsParticipantSelectedOrPublic(String username) {
        return this.virtualProvidesInterface.getVirtualEditionsUserIsParticipantSelectedOrPublic(username);
    }

    //Uses Virtual and Game Module
    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();

    public String importVirtualEditionFragmentFromTEI(MultipartFile file) throws IOException {
        String result = this.virtualProvidesInterface.importVirtualEditionFragmentFromTEI(file.getInputStream());
        this.gameProvidesInterface.importGamesFromTEI(file.getInputStream());
        return result;
    }

    public String importVirtualEditionFragmentFromTEI(File file) throws IOException {
        String result = this.virtualProvidesInterface.importVirtualEditionFragmentFromTEI(new FileInputStream(file));
        this.gameProvidesInterface.importGamesFromTEI(new FileInputStream(file));
        return result;
    }

    public boolean initializeVirtualModule() {
        return this.virtualProvidesInterface.initializeVirtualModule();
    }

    public void fetchCitationsFromTwitter() throws IOException {
        this.virtualProvidesInterface.fetchCitationsFromTwitter();
    }
}