package pt.ist.socialsoftware.edition.ldod.frontend.reading;

import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FEReadingRequiresInterface {
    // Uses Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }

    public ScholarInterDto getExpertEditionFirstInterpretation(String acronym) {
        return this.textProvidesInterface.getExpertEditionFirstInterpretation(acronym);
    }

    public ScholarInterDto getScholarInterbyExternalId(String expertEditionInterId) {
        return this.textProvidesInterface.getScholarInterbyExternalId(expertEditionInterId);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return this.textProvidesInterface.getFragmentDtoSet();
    }

    // Uses Recommendation Module
    private final RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    public List<Map.Entry<FragmentDto, Double>> getMostSimilarFragmentsOfGivenFragment(FragmentDto toReadFragment, Set<FragmentDto> toBeRecommended, WeightsDto weightsDto) {
        return this.recommendationProvidesInterface.getMostSimilarFragmentsOfGivenFragment(toReadFragment, toBeRecommended, weightsDto);
    }


}
