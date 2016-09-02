package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
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
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class TaxonomyProperty extends Property {
	private static Logger logger = LoggerFactory.getLogger(TaxonomyProperty.class);

	private final Taxonomy taxonomy;

	public TaxonomyProperty(Taxonomy taxonomy) {
		super();
		this.taxonomy = taxonomy;
	}

	public TaxonomyProperty(double weight, Taxonomy taxonomy) {
		super(weight);
		this.taxonomy = taxonomy;
	}

	public TaxonomyProperty(String acronym) {
		super();
		this.taxonomy = ((VirtualEdition) LdoD.getInstance().getEdition(acronym)).getTaxonomy();
	}

	public TaxonomyProperty(Double weight, String acronym) {
		super(weight);
		this.taxonomy = ((VirtualEdition) LdoD.getInstance().getEdition(acronym)).getTaxonomy();
	}

	public TaxonomyProperty(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym) {
		this(Double.parseDouble(weight), acronym);
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		List<Category> categories;
		for (FragInter inter : fragment.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter) {
				for (Tag tag : ((VirtualEditionInter) inter).getTagSet()) {
					categories = new ArrayList<Category>(getTaxonomy().getCategoriesSet());
					if (tag.getCategory().getTaxonomy() == getTaxonomy() && categories.contains(tag.getCategory())) {
						vector.set(categories.indexOf(tag.getCategory()), getWeight() * getTagWeight(tag));
					}
				}
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter inter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		List<Category> categories = new ArrayList<Category>(getTaxonomy().getSortedCategories());
		for (Tag tag : inter.getTagSet()) {
			vector.set(categories.indexOf(tag.getCategory()), getWeight() * getTagWeight(tag));
		}
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(getTaxonomy().getCategoriesSet().size(), 0.0));
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

	protected double getTagWeight(Tag tag) {
		return 1.;
	}
}