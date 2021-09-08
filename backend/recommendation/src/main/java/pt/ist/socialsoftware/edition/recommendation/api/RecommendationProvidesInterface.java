package pt.ist.socialsoftware.edition.recommendation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.Atomic;

import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.PropertyDto;

import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.recommendation.api.wrappers.IntersByDistance;
import pt.ist.socialsoftware.edition.recommendation.api.wrappers.MostSimilarFragments;
import pt.ist.socialsoftware.edition.recommendation.api.wrappers.RecommendationVirtualEditionInter;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.recommendation.api.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMScholarInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.utils.RecommendationBootstrap;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RecommendationProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationProvidesInterface.class);

    //private final RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();

    @PostMapping("/scholarIntersByDistance")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<InterIdDistancePairDto> getScholarIntersByDistance(@RequestBody IntersByDistance wrapper) {
        logger.debug("getScholarIntersByDistance");
        ScholarInterDto scholarInterDto = wrapper.getScholarInterDto();
        WeightsDto weights = wrapper.getWeightsDto();

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

    @PostMapping("/virtualEditionIntersByDistance")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<InterIdDistancePairDto> geVirtualEditiontIntersByDistance(@RequestBody IntersByDistance wrapper) {
        logger.debug("virtualEditionIntersByDistance");
        VirtualEditionInterDto virtualEditionInterDto = wrapper.getVirtualEditionInterDto();
        WeightsDto weights = wrapper.getWeightsDto();

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

    @PostMapping("/generateRecommendationFromVirtualEditionInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public List<VirtualEditionInterDto> generateRecommendationFromVirtualEditionInter(@RequestBody RecommendationVirtualEditionInter wrapper) {
        logger.debug("generateRecommendationFromVirtualEditionInter");
        VirtualEditionInterDto inter = wrapper.getInter();
        String username = wrapper.getUsername();
        VirtualEditionDto virtualEdition = wrapper.getVirtualEditionDto();
        List<Property> properties = wrapper.getProperties().stream().map(PropertyDto::getProperty).collect(Collectors.toList());

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

    @PostMapping("/mostSimilarFragmentsOfGivenFragment")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<Map.Entry<FragmentDto, Double>> getMostSimilarFragmentsOfGivenFragment(@RequestBody MostSimilarFragments wrapper) {
        logger.debug("getMostSimilarFragmentsOfGivenFragment");
        FragmentDto toReadFragment = wrapper.getToReadFragment();
        Set<FragmentDto> toBeRecommended = wrapper.getToBeRecommended();
        WeightsDto weightsDto = wrapper.getWeightsDto();

        VSMFragmentRecommender recommender = new VSMFragmentRecommender();
        List<Property> properties = weightsDto.getProperties().stream().map(property -> {
            if (property instanceof TaxonomyProperty) {
                property.setCached(Property.PropertyCache.ON);
                return property;
            } else {
                return property;
            }
        }).collect(Collectors.toList());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(recommender.getMostSimilarItems(toReadFragment, toBeRecommended,
                    properties));
            logger.debug(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return recommender.getMostSimilarItems(toReadFragment, toBeRecommended,
                properties);
    }

    @PostMapping("/initializeRecommendationModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void initializeRecommendationModule() {
        logger.debug("initializeRecommendationModule");
        RecommendationBootstrap.initializeRecommendationModule();
    }

    @PostMapping("/loadRecommendationCache")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void loadRecommendationCache() {
        logger.debug("loadRecommendationCache");
        RecommendationBootstrap.loadRecommendationCache();
    }

    @PostMapping("/removeRecommendationModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeRecommendationModule() {
        RecommendationModule recommendationModule = RecommendationModule.getInstance();
        if (recommendationModule != null) {
            recommendationModule.remove();
        }
    }
}
