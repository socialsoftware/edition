package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class PrintedProperty extends StorableProperty {
	private static List<String> titles = null;

	public PrintedProperty() {
		super();
	}

	public PrintedProperty(double weight) {
		super(weight);
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		for (Source source : fragment.getSourcesSet()) {
			if (source.getType().equals(SourceType.PRINTED)) {
				vector.set(0, 1.0);
				String title = ((PrintedSource) source).getTitle();
				if (titles.contains(title)) {
					vector.set(titles.indexOf(title) + 1, 1.0);
				}
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		if (sourceInter.getSource().getType().equals(SourceType.PRINTED)) {
			vector.set(0, 1.0);
			String title = ((PrintedSource) sourceInter.getSource()).getTitle();
			if (titles.contains(title)) {
				vector.set(titles.indexOf(title) + 1, 1.0);
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter virtualEditionInter) {
		return virtualEditionInter.getLastUsed().accept(this);
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<>(Collections.nCopies(getNumberOfPublications() + 1, 0.));
	}

	private int getNumberOfPublications() {
		return getTitles().size();
	}

	private Collection<String> getTitles() {
		if (titles == null) {
			Set<String> tempTitles = new TreeSet<>();
			PrintedSource printedSource;
			for (Fragment frag : LdoD.getInstance().getFragmentsSet()) {
				for (Source source : frag.getSourcesSet()) {
					if (source.getType().equals(SourceType.PRINTED)) {
						printedSource = (PrintedSource) source;
						tempTitles.add(printedSource.getTitle());
					}
				}
			}
			titles = Collections.unmodifiableList(new ArrayList<>(tempTitles));
		}
		return titles;
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setPublicationWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Printed";
	}
}