package pt.ist.socialsoftware.edition.ldod.frontend.virtual.assistedordering;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FEUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.PropertyDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FEAssistedOrderingRequiresInterface {
    // Uses User Module
    private final FEUserProvidesInterface feUserProvidesInterface = new FEUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public void saveVirtualEdition(String acronym, String[] inters) {
        this.virtualProvidesInterface.saveVirtualEdition(acronym, inters);
    }

    public void createVirtualEdition(String username, String acronym, String title, LocalDate localDate, boolean pub, String[] inters) {
        this.virtualProvidesInterface.createVirtualEdition(username, acronym, title, localDate, pub, inters);
    }

    // Uses Recommendation Module
    private final RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    public List<VirtualEditionInterDto> generateRecommendationFromVirtualEditionInter(VirtualEditionInterDto virtualEditionInterDto, String username, VirtualEditionDto virtualEdition, List<PropertyDto> properties) {
        return this.recommendationProvidesInterface.generateRecommendationFromVirtualEditionInter(virtualEditionInterDto, username, virtualEdition, properties.stream().map(PropertyDto::getProperty).collect(Collectors.toList()));
    }


    public List<VirtualEditionDto> getVirtualEditionsUserIsParticipant(String username) {
        return new ArrayList<>(this.virtualProvidesInterface.getVirtualEditionsUserIsParticipant(username));
    }

}