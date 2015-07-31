package pt.ist.socialsoftware.edition.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class TaxonomyWeight extends TaxonomyWeight_Base {
    
	public TaxonomyWeight(RecommendationWeights recommendationsWeights, Taxonomy taxonomy) {
        super();
		setRecommendationsWeights(recommendationsWeights);
		setTaxonomy(taxonomy);
		setWeight(1.);
    }
    
	public TaxonomyWeight(RecommendationWeights recommendationsWeights, Taxonomy taxonomy, double weight) {
		super();
		setRecommendationsWeights(recommendationsWeights);
		setTaxonomy(taxonomy);
		setWeight(weight);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setRecommendationsWeights(null);
		setTaxonomy(null);
		deleteDomainObject();
	}

}
