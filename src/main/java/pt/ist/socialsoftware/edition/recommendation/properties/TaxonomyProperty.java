package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class TaxonomyProperty extends Property {
	private static Logger logger = LoggerFactory.getLogger(TaxonomyProperty.class);

	private final Taxonomy taxonomy;
	private List<Category> sortedCategories = null;

	public TaxonomyProperty(double weight, Taxonomy taxonomy) {
		super(weight);
		this.taxonomy = taxonomy;
		this.sortedCategories = taxonomy.getSortedCategories();
	}

	public TaxonomyProperty(Double weight, String acronym) {
		this(weight, ((VirtualEdition) LdoD.getInstance().getEdition(acronym)).getTaxonomy());
	}

	public TaxonomyProperty(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
		this(Double.parseDouble(weight), acronym);
	}

	@Override
	public List<Double> extractVector(Fragment fragment) {
		List<Double> vector = getDefaultVector();
		for (FragInter inter : fragment.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter
					&& ((VirtualEditionInter) inter).getVirtualEdition().getTaxonomy() == taxonomy) {
				for (Category category : ((VirtualEditionInter) inter).getCategories()) {
					if (sortedCategories.contains(category)) {
						vector.set(sortedCategories.indexOf(category), 1.0 * getWeight());
					}
				}
			}
		}
		return vector;
	}

	@Override
	protected List<Double> extractVector(VirtualEditionInter inter) {
		List<Double> vector = getDefaultVector();
		for (Category category : inter.getCategories()) {
			vector.set(sortedCategories.indexOf(category), 1.0 * getWeight());
		}
		return vector;
	}

	@Override
	protected List<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(sortedCategories.size(), 0.0));
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setTaxonomyWeight(getWeight());
	}

	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	@Override
	public String getTitle() {
		return getTaxonomy().getName();
	}

}