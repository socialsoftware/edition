package pt.ist.socialsoftware.edition.recommendation.feature.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxonomyProperty extends Property {
    private static final Logger logger = LoggerFactory.getLogger(TaxonomyProperty.class);

    private final String acronym;
    private List<String> sortedCategories = null;

    private static final Map<String, List<String>> categoriesCache = new HashMap<>();

    public TaxonomyProperty(double weight, String acronym, PropertyCache cached) {
        super(weight, cached);
        this.acronym = acronym;
        this.sortedCategories = this.recommendationRequiresInterface.getVirtualEditionSortedCategoryList(acronym);
    }

    public TaxonomyProperty(double weight, String acronym) {
        super(weight, PropertyCache.OFF);
        this.acronym = acronym;
        this.sortedCategories = this.recommendationRequiresInterface.getVirtualEditionSortedCategoryList(acronym);
    }

    @Override
    double[] extractVector(ScholarInterDto scholarInterDto) {
        return new double[0];
    }

    @Override
    protected double[] extractVector(VirtualEditionInterDto inter) {
        double[] vector = getDefaultVector();
        for (String category : getCategoriesFromCache(inter)) {
            if (this.sortedCategories.contains(category)) {
                vector[this.sortedCategories.indexOf(category)] = 1.0;
            }
        }
        return vector;
    }

    @Override
    public double[] extractVector(FragmentDto fragment) {
        double[] vector = getDefaultVector();
        for (String category : this.recommendationRequiresInterface.getFragmentCategoriesInVirtualEditon(this.acronym, fragment.getXmlId())) {
            if (this.sortedCategories.contains(category)) {
                vector[this.sortedCategories.indexOf(category)] = 1.0;
            }
        }
        return vector;
    }

    @Override
    protected double[] getDefaultVector() {
        return new double[this.sortedCategories.size()];
    }

    @Override
    public void userWeight(RecommendationWeights recommendationWeights) {
        recommendationWeights.setTaxonomyWeight(getWeight());
    }

    private List<String> getCategoriesFromCache(VirtualEditionInterDto interDto) {
        List<String> categories = categoriesCache.get(interDto.getXmlId());
        if (categories == null) {
            categories = interDto.getSortedCategoriesName();
            categoriesCache.put(interDto.getXmlId(), categories);
        }
        return categories;
    }

}