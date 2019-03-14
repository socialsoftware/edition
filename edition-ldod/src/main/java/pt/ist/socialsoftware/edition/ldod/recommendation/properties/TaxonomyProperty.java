package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.*;

public class TaxonomyProperty extends Property {
	private static Logger logger = LoggerFactory.getLogger(TaxonomyProperty.class);

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
	protected double[] extractVector(VirtualEditionInter inter) {
		double[] vector = getDefaultVector();
		for (Category category : inter.getCategories()) {
			vector[sortedCategories.indexOf(category)] = 1.0;
		}
		return vector;
	}

	@Override
	public double[] extractVector(Fragment fragment) {
		double[] vector = getDefaultVector();
		for (FragInter inter : fragment.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter
					&& ((VirtualEditionInter) inter).getVirtualEdition().getTaxonomy() == taxonomy) {
				for (Category category : ((VirtualEditionInter) inter).getCategories()) {
					if (sortedCategories.contains(category)) {
						vector[sortedCategories.indexOf(category)] = 1.0;
					}
				}
			}
		}
		return vector;
	}

	@Override
	protected double[] getDefaultVector() {
		return new double[sortedCategories.size()];
	}

	@Override
	public void userWeight(RecommendationWeights recommendationWeights) {
		recommendationWeights.setTaxonomyWeight(getWeight());
	}

	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	@Override
	public String getTitle() {
		return "Taxonomy";
	}

	@Override
	public String getConcreteTitle(FragInter inter) {
		return ((VirtualEditionInter) inter).getCategories().stream().sorted().map(c -> c.getName())
				.collect(Collectors.joining(","));
	}

}