package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

public class SpecificTaxonomyProperty extends Property {

	private final Taxonomy taxonomy;

	public SpecificTaxonomyProperty(Taxonomy taxonomy) {
		super();
		this.taxonomy = taxonomy;
	}

	public SpecificTaxonomyProperty(double weight, Taxonomy taxonomy) {
		super(weight);
		this.taxonomy = taxonomy;
	}

	public SpecificTaxonomyProperty(String acronym, String taxonomy) {
		super();
		this.taxonomy = ((VirtualEdition) LdoD.getInstance().getEdition(acronym)).getTaxonomy();
	}

	public SpecificTaxonomyProperty(Double weight, String acronym, String taxonomy) {
		super(weight);
		this.taxonomy = ((VirtualEdition) LdoD.getInstance().getEdition(acronym)).getTaxonomy();
	}

	public SpecificTaxonomyProperty(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym,
			@JsonProperty("taxonomy") String taxonomy) {
		this(Double.parseDouble(weight), acronym, taxonomy);
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		List<Category> categories;
		for (FragInter inter : fragment.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter) {
				for (Tag tag : ((VirtualEditionInter) inter).getTagSet()) {
					categories = new ArrayList<>(getTaxonomy().getCategoriesSet());
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
		List<Category> categories;
		for (Tag tag : inter.getTagSet()) {
			categories = new ArrayList<>(getTaxonomy().getCategoriesSet());
			if (tag.getCategory().getTaxonomy() == getTaxonomy() && categories.contains(tag.getCategory())) {
				vector.set(categories.indexOf(tag.getCategory()), getWeight() * getTagWeight(tag));
			}
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