package pt.ist.socialsoftware.edition.ldod.frontend.user;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.Set;

public class FeUserRequiresInterface {
    // Uses User Module
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

    public UserDto getUser(String username) {
        return this.userProvidesInterface.getUser(username);
    }

    public void setLastLogin(String username, LocalDate now) {
        this.userProvidesInterface.setUserLastLogin(username, now);
    }

    // User Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();


    public Set<VirtualEditionDto> getPublicVirtualEditionsOrUserIsParticipant(String username) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username);
    }

    public VirtualEditionDto getVirtualEditionByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionOfTaxonomyByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionOfTaxonomyByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionOfCategoryByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionOfCategoryByExternalId(externalId);
    }

    public VirtualEditionDto getVirtualEditionOfTagByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionOfTagByExternalId(externalId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByUrlId(urlId);
    }

}