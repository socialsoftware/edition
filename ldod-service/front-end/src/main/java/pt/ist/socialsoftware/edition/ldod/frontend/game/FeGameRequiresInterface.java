package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.CategoryDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.TagDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;

import java.io.InputStream;
import java.util.List;
import java.util.Set;


public class FeGameRequiresInterface {
    private static final Logger logger = LoggerFactory.getLogger(FeGameRequiresInterface.class);

    // Uses Frontend User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public String getFirstName(String username) {
        return this.userProvidesInterface.getFirstName(username);
    }

    public String getLastName(String username) {
        return this.userProvidesInterface.getLastName(username);
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public String getVirtualEditionExternalIdByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(acronym);
    }

    public boolean getVirtualEditionTaxonomyAnnotationStatus(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionTaxonomyAnnotationStatus(acronym);
    }

    public String getVirtualEditionInterTitle(String interId) {
        return this.virtualProvidesInterface.getVirtualEditionInterTitle(interId);
    }

    public TagDto getTagInInter(String interId, String tagId) {
        return this.virtualProvidesInterface.getTagInInter(interId, tagId);
    }

    public CategoryDto getTagCategory(String interId, String tagId) {
        return this.virtualProvidesInterface.getTagCategory(interId, tagId);
    }

    //Uses Game Module
    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();

    public Set<ClassificationGameDto> getClassificationGamesForEdition(String acronym) {
        return this.gameProvidesInterface.getClassificationGamesForEdition(acronym);
    }

    public void createClassificationGame(VirtualEditionDto virtualEdition, String description, DateTime parse, VirtualEditionInterDto inter, String authenticatedUser) {
        this.gameProvidesInterface.createClassificationGame(virtualEdition, description, parse, inter, authenticatedUser);
    }

    public ClassificationGameDto getClassificationGameByExternalId(String externalId) {
        return this.gameProvidesInterface.getClassificationGameByExternalId(externalId);
    }

    public void importGamesFromTEI(InputStream inputStream) {
        this.gameProvidesInterface.importGamesFromTEI(inputStream);
    }

    public void initializeGameModule() {
        this.gameProvidesInterface.initializeGameModule();
    }

    public void startGameRunner(String id) {
        this.gameProvidesInterface.startGameRunner(id);
    }

    public List<String> getGamesForScheduledTasks(DateTime now) {
        return this.gameProvidesInterface.getGamesForScheduledTasks(now);
    }

    public void manageDailyClassificationGames() {
        this.gameProvidesInterface.manageDailyClassificationGames();
    }
}
