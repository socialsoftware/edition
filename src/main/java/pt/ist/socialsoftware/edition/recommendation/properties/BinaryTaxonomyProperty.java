package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class BinaryTaxonomyProperty extends SpecificTaxonomyProperty {

	public BinaryTaxonomyProperty(Taxonomy taxonomy) {
		super(taxonomy);
	}

	public BinaryTaxonomyProperty(double weight, Taxonomy taxonomy) {
		super(weight, taxonomy);
	}

	public BinaryTaxonomyProperty(@JsonProperty("weight") String weight, @JsonProperty("acronym") String acronym,
			@JsonProperty("taxonomy") String taxonomy) {
		super(weight, acronym, taxonomy);
	}

	@Override
	protected double getTagWeight(Tag tag) {
		// TODO: remove tag weight from recommendations
		return 1;
	}

	@Override
	protected Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<Double> extractVector(Source source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Double> visit(ExpertEditionInter expertEditionInter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Double> visit(Fragment fragment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Double> visit(Source source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Double> visit(SourceInter sourceInter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Double> visit(VirtualEditionInter virtualEditionInter) {
		// TODO Auto-generated method stub
		return null;
	}

}