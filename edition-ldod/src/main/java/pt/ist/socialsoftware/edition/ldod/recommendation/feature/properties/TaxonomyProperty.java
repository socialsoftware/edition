package pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.List;

public class TaxonomyProperty extends Property {
    private static final Logger logger = LoggerFactory.getLogger(TaxonomyProperty.class);

    private final String acronym;
    private List<String> sortedCategories = null;

    public TaxonomyProperty(double weight, String acronym, PropertyCache cached) {
        super(weight, cached);
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
        for (String category : inter.getSortedCategories()) {
            vector[this.sortedCategories.indexOf(category)] = 1.0;
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

}