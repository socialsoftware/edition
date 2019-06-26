package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.List;

public class TaxonomyProperty extends Property {
    private static final Logger logger = LoggerFactory.getLogger(TaxonomyProperty.class);

    private final Taxonomy taxonomy;
    private List<Category> sortedCategories = null;

    public TaxonomyProperty(double weight, Taxonomy taxonomy, PropertyCache cached) {
        super(weight, cached);
        this.taxonomy = taxonomy;
        this.sortedCategories = taxonomy.getEdition().getAllDepthSortedCategories();
    }

    public TaxonomyProperty(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
        this(Double.parseDouble(weight), (LdoD.getInstance().getVirtualEdition(acronym)).getTaxonomy(),
                PropertyCache.OFF);
    }

    @Override
    double[] extractVector(ExpertEditionInter expertEditionInter) {
        return new double[0];
    }

    @Override
    protected double[] extractVector(VirtualEditionInter inter) {
        double[] vector = getDefaultVector();
        for (Category category : inter.getCategories()) {
            vector[this.sortedCategories.indexOf(category)] = 1.0;
        }
        return vector;
    }

    @Override
    public double[] extractVector(Fragment fragment) {
        double[] vector = getDefaultVector();
        for (VirtualEditionInter inter : this.taxonomy.getEdition().getAllDepthVirtualEditionInters()) {
            if (getFragment(inter) == fragment) {
                for (Category category : inter.getCategories()) {
                    if (this.sortedCategories.contains(category)) {
                        vector[this.sortedCategories.indexOf(category)] = 1.0;
                    }
                }
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