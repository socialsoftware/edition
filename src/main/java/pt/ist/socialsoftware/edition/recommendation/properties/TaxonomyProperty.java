package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
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
		for(FragInter inter : fragment.getFragmentInterSet()) {
			if(inter instanceof VirtualEditionInter) {
				for(Tag tag : inter.getTagSet()) {
					if(!tag.getDeprecated()) {
						pair = new Pair(tag);
						if(tags.contains(pair)) {
							vector.set(tags.indexOf(pair), getWeight());
						}
					}
				}
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter inter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		for(Tag tag : inter.getTagSet()) {
			if(!tag.getDeprecated()) {
				Pair pair = new Pair(tag);
				if(tags.contains(pair)) {
					vector.set(tags.indexOf(pair), getWeight());
				}
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(tags.size(), 0.0));
	}

	public List<Pair> getTagSet(FragInter inter1, FragInter inter2) {
		List<Tag> tempTags = new ArrayList<Tag>();
		tempTags.addAll(inter1.getTagSet());
		tempTags.addAll(inter2.getTagSet());
		List<Pair> tags = new ArrayList<Pair>();
		Pair pair;
		for(Tag tag : tempTags) {
			if(!tag.getDeprecated()) {
				pair = new Pair(tag);
				if(!tags.contains(pair)) {
					tags.add(pair);
				}
			}
		}
		return tags;
	}

	private List<Pair> getTagSet(Fragment frag1, Fragment frag2) {
		List<Tag> tempTags = new ArrayList<Tag>();
		for(FragInter inter : frag1.getFragmentInterSet()) {
			if(inter instanceof VirtualEditionInter) {
				tempTags.addAll(inter.getTagSet());
			}
		}
		for(FragInter inter : frag2.getFragmentInterSet()) {
			if(inter instanceof VirtualEditionInter) {
				tempTags.addAll(inter.getTagSet());
			}
		}
		List<Pair> tags = new ArrayList<Pair>();
		for(Tag tag : tempTags) {
			if(!tag.getDeprecated()) {
				if(!tags.contains(tag)) {
					tags.add(new Pair(tag));
				}
			}
		}
		return tags;
	}

	@Override
	public void setFragmentsGroup(FragInter inter1, FragInter inter2) {
		if(tags == null)
			tags = getTagSet(inter1, inter2);
	}

	@Override
	public void setFragmentsGroup(Fragment frag1, Fragment frag2) {
		if(tags == null)
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
}