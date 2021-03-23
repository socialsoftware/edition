package pt.ist.socialsoftware.edition.recommendation.api;


import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.SubscribeInterface;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecommendationRequiresInterface implements SubscribeInterface {

    private static RecommendationRequiresInterface instance;

    public static RecommendationRequiresInterface getInstance() {
        if (instance == null) {
            instance = new RecommendationRequiresInterface();
        }
        return instance;
    }

    protected RecommendationRequiresInterface() {
        EventInterface.getInstance().subscribe(this);
    }

    public void notify(Event event) {
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

    public void cleanTermsTFIDFCache() {
        this.textProvidesInterface.clearTermsTFIDFCache();
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
