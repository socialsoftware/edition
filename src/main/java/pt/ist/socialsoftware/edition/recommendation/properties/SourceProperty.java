package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;

public class SourceProperty extends StorableProperty {
	public static final String TYPESCRIPT = "dactil";
	public static final String TYPESCRIPT_2 = "datil";
	public static final String MANUSCRIPTID = "manus";

	public SourceProperty(double weight) {
		super(weight);
	}

	public SourceProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	@Override
	protected Collection<Double> extractVector(Fragment fragment) {
		List<List<Double>> manuscriptVectors = new ArrayList<List<Double>>();
		List<List<Double>> typescriptsVectors = new ArrayList<List<Double>>();
		List<List<Double>> printedVectors = new ArrayList<List<Double>>();

		List<Double> vector;
		if (fragment.getSourcesSet().size() > 0) {
			for (Source source : fragment.getSourcesSet()) {
				vector = new ArrayList<Double>();
				if (source.getType().equals(SourceType.MANUSCRIPT)) {
					ManuscriptSource manuscriptSource = (ManuscriptSource) source;
					if (manuscriptSource.getNotes().toLowerCase().contains(MANUSCRIPTID)) {
						// Existence
						vector.add(getWeight());
						// LodDLabel
						if (manuscriptSource.getHasLdoDLabel()) {
							vector.add(1.);
						} else {
							vector.add(0.);
						}
					} else if (manuscriptSource.getNotes().toLowerCase().contains(TYPESCRIPT)
							|| manuscriptSource.getNotes().toLowerCase().contains(TYPESCRIPT_2)) {
						// Existance
						vector.add(getWeight());
						// LdoDLabel
						if (manuscriptSource.getHasLdoDLabel()) {
							vector.add(1.);
						} else {
							vector.add(0.);
						}
					}
				} else if (source.getType().equals(SourceType.PRINTED)) {
					PrintedSource printedSource = (PrintedSource) source;
					// Existance
					vector.add(getWeight());
				} else {

				}
			}

			vector = new ArrayList<Double>();
			if (manuscriptVectors.size() > 0) {
				List<Double> manuscriptVector = new ArrayList<Double>(
						Collections.nCopies(manuscriptVectors.get(0).size(), 0.));
				for (List<Double> v : manuscriptVectors) {
					int size = v.size();
					for (int i = 0; i < size; i++) {
						if (v.get(i) > manuscriptVector.get(i)) {
							manuscriptVector.set(i, v.get(i));
						}
					}
				}
				vector.addAll(manuscriptVector);
			} else {
				vector.addAll(getManuscriptDefaultVector());
			}

			if (typescriptsVectors.size() > 0) {
				List<Double> typescriptVector = new ArrayList<Double>(
						Collections.nCopies(typescriptsVectors.get(0).size(), 0.));
				for (List<Double> v : typescriptsVectors) {
					int size = v.size();
					for (int i = 0; i < size; i++) {
						if (v.get(i) > typescriptVector.get(i)) {
							typescriptVector.set(i, v.get(i));
						}
					}
				}
				vector.addAll(typescriptVector);
			} else {
				vector.addAll(getTypescriptDefaultVector());
			}

			if (printedVectors.size() > 0) {
				List<Double> printedVector = new ArrayList<Double>(
						Collections.nCopies(printedVectors.get(0).size(), 0.));
				for (List<Double> v : manuscriptVectors) {
					int size = v.size();
					for (int i = 0; i < size; i++) {
						if (v.get(i) > printedVector.get(i)) {
							printedVector.set(i, v.get(i));
						}
					}
				}
				vector.addAll(printedVector);
			} else {
				vector.addAll(getPrintedDefaultVector());
			}
		} else {
			vector = new ArrayList<Double>(getDefaultVector());
		}
		return vector;
	}

	// Manuscript
	// Typescript
	// Printed
	@Override
	protected Collection<Double> getDefaultVector() {
		Collection<Double> vector = new ArrayList<Double>();

		vector.addAll(getManuscriptDefaultVector());
		vector.addAll(getTypescriptDefaultVector());
		vector.addAll(getPrintedDefaultVector());

		return vector;
	}

	private List<Double> getPrintedDefaultVector() {
		List<Double> vector = new ArrayList<Double>();

		// Existance
		vector.add(.0);

		return vector;
	}

	private List<Double> getTypescriptDefaultVector() {
		List<Double> vector = new ArrayList<Double>();

		// Existance
		vector.add(.0);
		// LdoD Mark
		vector.add(.0);

		return vector;
	}

	private List<Double> getManuscriptDefaultVector() {
		List<Double> vector = new ArrayList<Double>();

		// Existance
		vector.add(.0);
		// LdoD Mark
		vector.add(.0);

		return vector;
	}

	@Override
	public String getTitle() {
		return "Source";
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setSourceWeight(getWeight());

	}

	@Override
	protected String getConcreteTitle(FragInter inter) {
		String title = "";
		boolean manu = false;
		boolean datil = false;
		boolean printed = false;
		for (Source source : inter.getFragment().getSourcesSet()) {
			if (source.getType().equals(SourceType.MANUSCRIPT)) {
				ManuscriptSource manuscriptSource = (ManuscriptSource) source;
				if (manuscriptSource.getNotes().toLowerCase().contains(MANUSCRIPTID)) {
					manu = true;
				} else if (manuscriptSource.getNotes().toLowerCase().contains(TYPESCRIPT)
						|| manuscriptSource.getNotes().toLowerCase().contains(TYPESCRIPT_2)) {
					datil = true;
				}
			} else if (source.getType().equals(SourceType.PRINTED)) {
				printed = true;
			}
		}

		title += manu ? ":Manuscript" : "";
		title += datil ? ":Typescript" : "";
		title += printed ? ":Printed" : "";

		if (title.length() > 0)
			title = title.substring(1);

		return title;
	}

}
