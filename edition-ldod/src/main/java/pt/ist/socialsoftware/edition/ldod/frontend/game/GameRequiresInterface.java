package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.TagDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

public class GameRequiresInterface {
    private static final Logger logger = LoggerFactory.getLogger(GameRequiresInterface.class);

    // Uses User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public String getFirstName(String username){
        return this.userProvidesInterface.getFirstName(username);
    }

    public String getLastName(String username){
        return this.userProvidesInterface.getLastName(username);
    }

    public String getAuthenticatedUser(){
        return this.userProvidesInterface.getAuthenticatedUser();
    }

    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId){
        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId){
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public String getVirtualEditionExternalIdByAcronym(String acronym){
        return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(acronym);
    }

    public boolean getVirtualEditionTaxonomyAnnotationStatus(String acronym){
        return this.virtualProvidesInterface.getVirtualEditionTaxonomyAnnotationStatus(acronym);
    }

    public String getVirtualEditionInterTitle(String interId){
        return this.virtualProvidesInterface.getVirtualEditionInterTitle(interId);
    }

    public TagDto getTagInInter(String interId, String tagId){
        return this.virtualProvidesInterface.getTagInInter(interId, tagId);
    }

    public CategoryDto getTagCategory(String interId, String tagId){
        return this.virtualProvidesInterface.getTagCategory(interId, tagId);
    }
}
