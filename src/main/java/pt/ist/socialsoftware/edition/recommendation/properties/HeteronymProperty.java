package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public class HeteronymProperty extends StorableProperty {
	private static List<Heteronym> heteronymList = LdoD.getInstance().getSortedHeteronyms();

	public HeteronymProperty() {
		super();
	}

	public HeteronymProperty(double weight) {
		super(weight);
	}

	public HeteronymProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	private List<Double> buildVector(Collection<Heteronym> foundHeteronyms) {
		List<Double> vector = new ArrayList<Double>();
		for (Heteronym heteronym : HeteronymProperty.heteronymList) {
			if (foundHeteronyms.contains(heteronym))
				vector.add(1.0);
			else
				vector.add(0.0);

		}
		return vector;
	}

	@Override
	public List<Double> extractVector(ExpertEditionInter expertEditionInter) {
		Collection<Heteronym> foundHeteronyms = new ArrayList<Heteronym>();
		foundHeteronyms.add(expertEditionInter.getHeteronym());
		return buildVector(foundHeteronyms);
	}

	@Override
	public List<Double> extractVector(SourceInter sourceInter) {
		Collection<Heteronym> foundHeteronyms = new ArrayList<Heteronym>();
		foundHeteronyms.add(sourceInter.getHeteronym());
		return buildVector(foundHeteronyms);
	}

	@Override
	public List<Double> extractVector(Fragment fragment) {
		List<Heteronym> heteronyms = new ArrayList<Heteronym>();
		for (FragInter inter : fragment.getFragmentInterSet()) {
			heteronyms.add(inter.getHeteronym());
		}
		for (Source source : fragment.getSourcesSet()) {
			for (SourceInter inter : source.getSourceIntersSet()) {
				heteronyms.add(inter.getHeteronym());
			}
		}
		return buildVector(heteronyms);
	}

	@Override
	public List<Double> extractVector(Source source) {
		List<Heteronym> foundHeteronyms = new ArrayList<Heteronym>();
		for (SourceInter inter : source.getSourceIntersSet()) {
			foundHeteronyms.add(inter.getHeteronym());
		}
		return buildVector(foundHeteronyms);
	}

	@Override
	protected List<Double> getDefaultVector() {
		return Collections.nCopies(heteronymList.size(), 0.);
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setHeteronymWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Heteronym";
	}

	@Override
	protected String getConcreteTitle(FragInter inter) {
		String title = "";
		List<Heteronym> heteronyms = new ArrayList<Heteronym>();
		for (FragInter intr : inter.getFragment().getFragmentInterSet()) {
			heteronyms.add(intr.getHeteronym());
		}
		for (Source source : inter.getFragment().getSourcesSet()) {
			for (SourceInter intr : source.getSourceIntersSet()) {
				heteronyms.add(intr.getHeteronym());
			}
		}

		for (Heteronym heteronym : HeteronymProperty.heteronymList) {
			if (heteronyms.contains(heteronym))
				title += ":" + heteronym.getName();
		}

		if (title.length() > 0)
			title = title.substring(1);

		return title;
	}

}