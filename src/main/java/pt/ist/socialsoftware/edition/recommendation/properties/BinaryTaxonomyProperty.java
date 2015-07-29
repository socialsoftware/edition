package pt.ist.socialsoftware.edition.recommendation.properties;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;

public class BinaryTaxonomyProperty extends SpecificTaxonomyProperty {

	public BinaryTaxonomyProperty(Taxonomy taxonomy) {
		super(taxonomy);
	}

	public BinaryTaxonomyProperty(double weight, Taxonomy taxonomy) {
		super(weight, taxonomy);
	}

	public BinaryTaxonomyProperty(
			@JsonProperty("weight") String weight,
			@JsonProperty("acronym") String acronym,
			@JsonProperty("taxonomy") String taxonomy) {
		super(weight, acronym, taxonomy);
	}

	@Override
	protected double getTagWeight(Tag tag) {
		return tag.getWeight();
	}
}