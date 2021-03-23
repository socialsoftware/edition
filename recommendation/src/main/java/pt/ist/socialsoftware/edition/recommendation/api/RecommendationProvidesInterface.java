package pt.ist.socialsoftware.edition.recommendation.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.recommendation.api.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMScholarInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.utils.RecommendationBootstrap;
import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationProvidesInterface.class);

    //private final RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();

    public List<InterIdDistancePairDto> getIntersByDistance(ScholarInterDto scholarInterDto, WeightsDto weights) {
        List<ScholarInterDto> inters = scholarInterDto.getExpertEdition().getExpertEditionInters();

        VSMScholarInterRecommender recommender = new VSMScholarInterRecommender();

        inters.remove(scholarInterDto);

        List<InterIdDistancePairDto> recommendedEdition = new ArrayList<>();

        List<Property> properties = weights.getProperties();
        for (ScholarInterDto inter : inters) {
            recommendedEdition.add(new InterIdDistancePairDto(inter.getExternalId(),
                    recommender.calculateSimilarity(scholarInterDto, inter, properties)));
        }

        recommendedEdition = recommendedEdition.stream().sorted(Comparator.comparing(InterIdDistancePairDto::getDistance).reversed()).collect(Collectors.toList());
        recommendedEdition.add(0, new InterIdDistancePairDto(scholarInterDto.getExternalId(), 1.0d));

        return recommendedEdition;
    }

    public List<InterIdDistancePairDto> getIntersByDistance(VirtualEditionInterDto virtualEditionInterDto, WeightsDto weights) {
        VirtualEditionDto virtualEditionDto = virtualEditionInterDto.getVirtualEditionDto();

        List<VirtualEditionInterDto> inters = virtualEditionDto.getSortedVirtualEditionInterDtoList();

        VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

        inters.remove(virtualEditionInterDto);

        List<InterIdDistancePairDto> recommendedEdition = new ArrayList<>();

        List<Property> properties = weights.getProperties(virtualEditionDto.getAcronym());
        for (VirtualEditionInterDto inter : inters) {
            recommendedEdition.add(new InterIdDistancePairDto(inter.getExternalId(),
                    recommender.calculateSimilarity(virtualEditionInterDto, inter, properties)));
        }

        recommendedEdition = recommendedEdition.stream().sorted(Comparator.comparing(InterIdDistancePairDto::getDistance).reversed()).collect(Collectors.toList());

        recommendedEdition.add(0, new InterIdDistancePairDto(virtualEditionInterDto.getExternalId(), 1.0d));

        return recommendedEdition;
    }

    public List<VirtualEditionInterDto> generateRecommendationFromVirtualEditionInter(VirtualEditionInterDto inter,
                                                                                      String username,
                                                                                      VirtualEditionDto virtualEdition,
                                                                                      List<Property> properties) {
        List<VirtualEditionInterDto> recommendedEdition = new ArrayList<>();

        List<VirtualEditionInterDto> inters = virtualEdition.getSortedVirtualEditionInterDtoList();

        if (inters.isEmpty()) {
            return recommendedEdition;
        }

        VirtualEditionInterDto selected;
        RecommendationWeights recommendationWeights =
                RecommendationModule.getInstance().getRecommendationWeightsForUserInVirtualEdition(username, virtualEdition.getAcronym());
        if (inter == null) {
            selected = inters.get(0);
            recommendationWeights.setWeightsZero();

        } else {
            selected = inter;
            recommendationWeights.setWeights(properties);

        }

        inters.remove(selected);

        VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

        recommendedEdition.add(selected);
        recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(selected, inters, recommendationWeights.getPropertiesWithStoredWeights()));

        return recommendedEdition;
    }

    public List<Map.Entry<FragmentDto, Double>> getMostSimilarFragmentsOfGivenFragment(FragmentDto toReadFragment, Set<FragmentDto> toBeRecommended, WeightsDto weightsDto) {
        VSMFragmentRecommender recommender = new VSMFragmentRecommender();
        List<Property> properties = weightsDto.getProperties().stream().map(property -> {
            if (property instanceof TaxonomyProperty) {
                property.setCached(Property.PropertyCache.ON);
                return property;
            } else {
                return property;
            }
        }).collect(Collectors.toList());
        return recommender.getMostSimilarItems(toReadFragment, toBeRecommended,
                properties);
    }

    public void initializeRecommendationModule() {
        RecommendationBootstrap.initializeRecommendationModule();
    }

    public void loadRecommendationCache() {
        RecommendationBootstrap.loadRecommendationCache();
    }
}
