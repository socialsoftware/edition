package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class TaxonomyProperty extends Property {
	private List<Pair> tags = null;

	public TaxonomyProperty() {
		super();
	}

	public TaxonomyProperty(double weight) {
		super(weight);
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		Pair pair;
		for (FragInter inter : fragment.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter) {
				for (Tag tag : ((VirtualEditionInter) inter).getTagSet()) {
					pair = new Pair(tag);
					if (tags.contains(pair)) {
						vector.set(tags.indexOf(pair), getWeight());
					}
				}
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter inter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		for (Tag tag : inter.getTagSet()) {
			Pair pair = new Pair(tag);
			if (tags.contains(pair)) {
				vector.set(tags.indexOf(pair), getWeight());
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(tags.size(), 0.0));
	}

	public List<Pair> getTagSet(VirtualEditionInter inter1, VirtualEditionInter inter2) {
		List<Tag> tempTags = new ArrayList<Tag>();
		tempTags.addAll(inter1.getTagSet());
		tempTags.addAll(inter2.getTagSet());
		List<Pair> tags = new ArrayList<Pair>();
		Pair pair;
		for (Tag tag : tempTags) {
			pair = new Pair(tag);
			if (!tags.contains(pair)) {
				tags.add(pair);
			}
		}
		return tags;
	}

	private List<Pair> getTagSet(Fragment frag1, Fragment frag2) {
		List<Tag> tempTags = new ArrayList<Tag>();
		for (FragInter inter : frag1.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter) {
				tempTags.addAll(((VirtualEditionInter) inter).getTagSet());
			}
		}
		for (FragInter inter : frag2.getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter) {
				tempTags.addAll(((VirtualEditionInter) inter).getTagSet());
			}
		}
		List<Pair> tags = new ArrayList<Pair>();
		for (Tag tag : tempTags) {
			if (!tags.contains(tag)) {
				tags.add(new Pair(tag));
			}
		}
		return tags;
	}

	@Override
	public void setFragmentsGroup(FragInter inter1, FragInter inter2) {
		if (tags == null)
			tags = getTagSet((VirtualEditionInter) inter1, (VirtualEditionInter) inter2);
	}

	@Override
	public void setFragmentsGroup(Fragment frag1, Fragment frag2) {
		if (tags == null)
			tags = getTagSet(frag1, frag2);
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		// recommendationWeights.setTaxonomyWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Taxonomy";
	}

	@Override
	public Collection<Double> visit(ExpertEditionInter expertEditionInter) {
		return extractVector(expertEditionInter);
	}

	@Override
	public Collection<Double> visit(Fragment fragment) {
		return extractVector(fragment);
	}

	@Override
	protected Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		return getDefaultVector();
	}

	@Override
	protected Collection<Double> extractVector(Source source) {
		return getDefaultVector();
	}

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		return getDefaultVector();
	}

	@Override
	public Collection<Double> visit(Source source) {
		return extractVector(source);
	}

	@Override
	public Collection<Double> visit(SourceInter sourceInter) {
		return extractVector(sourceInter);
	}

	@Override
	public Collection<Double> visit(VirtualEditionInter virtualEditionInter) {
		return extractVector(virtualEditionInter);
	}
}