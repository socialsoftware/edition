package pt.ist.socialsoftware.edition.ldod.visual.api;

import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.util.List;
import java.util.Map;

public class VisualRequiresInterface {
    // Requires the Text Module
    TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public List<EditionInterListDto> getEditionInterListDto() {
        return this.textProvidesInterface.getEditionInterListDto();
    }

    public ExpertEditionDto getExpertEditionDto(String acronym) {
        return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    // Requires the Virtual Module
    VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<EditionInterListDto> getPublicVirtualEditionInterListDto() {
        return this.virtualProvidesInterface.getPublicVirtualEditionInterListDto();
    }

    public VirtualEditionDto getVirtualEdition(String acronym) {
        return this.virtualProvidesInterface.getVirtualEdition(acronym);
    }

    public ScholarInterDto getScholarInterByExternalIdOfInter(String interId) {
        ScholarInterDto scholarInterDto =
                this.virtualProvidesInterface.getScholarInterbyExternalId(interId);

        if (scholarInterDto == null) {
            scholarInterDto = this.textProvidesInterface.getScholarInterbyExternalId(interId);
        }

        return scholarInterDto;
    }

    public List<Map.Entry<String, Double>> getScholarInterTermFrequency(ScholarInterDto scholarInterDto) {
        return this.textProvidesInterface.getScholarInterTermFrequency(scholarInterDto);
    }

    // Requires the Recommendation Module
    RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    public List<InterIdDistancePairDto> getIntersByDistance(String externalId, WeightsDto weights) {
        VirtualEditionInterDto virtualEditionInterDto = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
        if (virtualEditionInterDto != null) {
            return this.recommendationProvidesInterface.getIntersByDistance(virtualEditionInterDto, weights);
        }

        ScholarInterDto scholarInterDto = this.textProvidesInterface.getScholarInterbyExternalId(externalId);
        if (scholarInterDto != null) {
            return this.recommendationProvidesInterface.getIntersByDistance(scholarInterDto, weights);
        }

        return null;
    }

}
