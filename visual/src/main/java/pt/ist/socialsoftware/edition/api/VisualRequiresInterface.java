package pt.ist.socialsoftware.edition.api;


import pt.ist.socialsoftware.edition.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.recommendation.api.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.text.api.dto.ExpertEditionInterListDto;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;


import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterListDto;

import java.util.List;
import java.util.Map;

public class VisualRequiresInterface {
    // Requires the Text Module
    TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    //Check later
    public List<ExpertEditionInterListDto> getEditionInterListDto() {
        return this.textProvidesInterface.getEditionInterListDto();
    }

    public ExpertEditionDto getExpertEditionDto(String acronym) {
        return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean highlightDiff) {
        return this.textProvidesInterface.getWriteFromPlainHtmlWriter4OneInter(xmlId, highlightDiff);
    }

    // Requires the Virtual Module
    VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<VirtualEditionInterListDto> getPublicVirtualEditionInterListDto() {
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
