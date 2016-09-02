package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public abstract class AuthoralProperty extends StorableProperty {

	private static Collection<Double> defaultVector;

	public AuthoralProperty() {
		super();
	}

	public AuthoralProperty(double weight) {
		super(weight);
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<List<Double>> allVector = new ArrayList<List<Double>>();
		List<Double> vector;
		if (fragment.getSourcesSet().size() > 0) {
			for (Source source : fragment.getSourcesSet()) {
				vector = new ArrayList<Double>();
				if (source.getType().equals(SourceType.MANUSCRIPT)) {
					ManuscriptSource manuscriptSource = (ManuscriptSource) source;
					if (manuscriptSource.getNotes().toLowerCase().contains(getDocumentType())) {
						vector.add(getWeight());
						if (manuscriptSource.getHasLdoDLabel()) {
							vector.add(1.);
							vector.add(0.);
						} else {
							vector.add(0.);
							vector.add(1.);
						}
					} else {
						vector.addAll(getDefaultVector());
					}
				} else {
					vector.addAll(getDefaultVector());
				}
				allVector.add(vector);
			}
			vector = new ArrayList<Double>(Collections.nCopies(allVector.get(0).size(), 0.));
			;
			for (List<Double> v : allVector) {
				int size = v.size();
				for (int i = 0; i < size; i++) {
					if (v.get(i) > vector.get(i)) {
						vector.set(i, v.get(i));
					}
				}
			}
			return vector;
		} else {
			vector = new ArrayList<Double>(getDefaultVector());
			return vector;
		}
	}

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		List<Double> vector = new ArrayList<>();
		if (sourceInter.getSource().getType().equals(SourceType.MANUSCRIPT)) {
			ManuscriptSource manuscriptSource = (ManuscriptSource) sourceInter.getSource();
			if (manuscriptSource.getNotes().toLowerCase().contains(getDocumentType())) {
				vector.add(getWeight());
				if (manuscriptSource.getHasLdoDLabel()) {
					vector.add(1.);
					vector.add(0.);
				} else {
					vector.add(0.);
					vector.add(1.);
				}
			} else {
				vector.addAll(getDefaultVector());
			}
		} else {
			vector.addAll(getDefaultVector());
		}
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		if (defaultVector == null) {
			defaultVector = new ArrayList<>();
			defaultVector.add(0.);
			defaultVector.add(0.);
			defaultVector.add(0.);
		}
		return Collections.unmodifiableCollection(defaultVector);
	}

	protected abstract String getDocumentType();

}
