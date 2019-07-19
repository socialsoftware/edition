package pt.ist.socialsoftware.edition.ldod.recommendation.api;

import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationModule;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecommendationRequiresInterface {
    public void notifyEvent(Event event) {
        if (event.getType().equals(Event.EventType.USER_REMOVE)) {
            String username = event.getIdentifier();
            RecommendationModule recommendationModule = RecommendationModule.getInstance();

            recommendationModule.getRecommendationWeightsSet().stream()
                    .filter(recommendationWeights -> recommendationWeights.getUser().equals(username))
                    .forEach(recommendationWeights -> recommendationWeights.remove());
        } else if (event.getType().equals(Event.EventType.VIRTUAL_EDITION_REMOVE)) {
            String virtualEditionAcronym = event.getIdentifier();
            RecommendationModule recommendationModule = RecommendationModule.getInstance();

            recommendationModule.getRecommendationWeightsSet().stream()
                    .filter(recommendationWeights -> recommendationWeights.getVirtualEditionAcronym().equals(virtualEditionAcronym))
                    .forEach(recommendationWeights -> recommendationWeights.remove());
        }

    }

    // Uses Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public Set<FragmentDto> getFragments() {
        return this.textProvidesInterface.getFragmentDtoSet();
    }

    public List<HeteronymDto> getSortedHeteronymsList() {
        return this.textProvidesInterface.getSortedHeteronymList();
    }

    public Map<String, Double> getFragmentTFIDF(String xmlId, List<String> commonTerms) {
        return this.textProvidesInterface.getFragmentTFIDF(xmlId, commonTerms);
    }

    public List<String> getFragmentTFIDF(String xmlId, int numberOfTerms) {
        return this.textProvidesInterface.getFragmentTFIDF(xmlId, numberOfTerms);
    }


    // Uses Virtual Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public List<String> getVirtualEditionSortedCategoryList(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionSortedCategoryList(acronym);
    }

    public List<String> getFragmentCategoriesInVirtualEditon(String acronym, String xmlId) {
        return this.virtualProvidesInterface.getFragmentCategoriesInVirtualEditon(acronym, xmlId);
    }

}
