package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class HeteronymProperty extends StorableProperty {

	public HeteronymProperty() {
		super();
	}

	public HeteronymProperty(double weight) {
		super(weight);
	}

	public HeteronymProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	private List<Double> buildVector(Collection<Heteronym> foundHeteronyms, List<Double> vector) {
		List<Heteronym> heteronymList = new ArrayList<Heteronym>(LdoD.getInstance().getHeteronymsSet());
		for(Heteronym heteronym : foundHeteronyms) {
			if(heteronymList.contains(heteronym))
				vector.set(heteronymList.indexOf(heteronym), 1.0);
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		Collection<Heteronym> foundHeteronyms = new ArrayList<Heteronym>();
		foundHeteronyms.add(expertEditionInter.getHeteronym());
		return buildVector(foundHeteronyms, vector);
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter virtualEditionInter) {
		return virtualEditionInter.getLastUsed().accept(this);
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Heteronym> heteronyms = new ArrayList<Heteronym>();
		for(FragInter inter : fragment.getFragmentInterSet()) {
			heteronyms.add(inter.getHeteronym());
		}
		for(Source source : fragment.getSourcesSet()) {
			for(SourceInter inter : source.getSourceIntersSet()) {
				heteronyms.add(inter.getHeteronym());
			}
		}
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		return buildVector(heteronyms, vector);
	}

	@Override
	public Collection<Double> extractVector(Source source) {
		List<Heteronym> foundHeteronyms = new ArrayList<Heteronym>();
		for(SourceInter inter : source.getSourceIntersSet()) {
			foundHeteronyms.add(inter.getHeteronym());
		}
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		return buildVector(foundHeteronyms, vector);
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(LdoD.getInstance().getHeteronymsSet().size(), 0.));
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setHeteronymWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Heteronym";
	}
}