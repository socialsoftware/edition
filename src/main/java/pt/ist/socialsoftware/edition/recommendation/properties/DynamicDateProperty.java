package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public class DynamicDateProperty extends Property {
	private Integer startYear = null;
	private Integer endYear = null;

	public DynamicDateProperty() {
		super();
	}

	public DynamicDateProperty(double weight) {
		super(weight);
	}

	public DynamicDateProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	@Override
	public List<Double> extractVector(Fragment fragment) {
		Set<Integer> dates = new TreeSet<Integer>();
		for (FragInter inter : fragment.getFragmentInterSet()) {
			if (inter.getLdoDDate() != null) {
				dates.add(inter.getLdoDDate().getDate().getYear());
			}
		}
		for (Source source : fragment.getSourcesSet()) {
			if (source.getLdoDDate() != null) {
				dates.add(source.getLdoDDate().getDate().getYear());
			}
		}
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		vector = buildVector(dates, vector);
		return vector;
	}

	@Override
	public List<Double> extractVector(ExpertEditionInter expertEditionInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		if (expertEditionInter.getLdoDDate() != null) {
			vector = buildVector(expertEditionInter.getLdoDDate().getDate().getYear(), vector);
		}
		return vector;
	}

	@Override
	protected List<Double> extractVector(SourceInter sourceInter) {
		Set<Integer> dates = new TreeSet<Integer>();
		if (sourceInter.getLdoDDate() != null) {
			dates.add(sourceInter.getLdoDDate().getDate().getYear());
		}
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		vector = buildVector(dates, vector);
		return vector;
	}

	@Override
	public List<Double> extractVector(Source source) {
		Set<Integer> dates = new TreeSet<Integer>();
		if (source.getType().equals(SourceType.MANUSCRIPT)) {
			ManuscriptSource manu = (ManuscriptSource) source;
			if (manu.getLdoDDate() != null) {
				dates.add(manu.getLdoDDate().getDate().getYear());
			}
			for (SourceInter inter : manu.getSourceIntersSet()) {
				if (inter.getLdoDDate() != null) {
					dates.add(inter.getLdoDDate().getDate().getYear());
				}
			}
		} else if (source.getType().equals(SourceType.PRINTED)) {
			PrintedSource printed = (PrintedSource) source;
			if (printed.getLdoDDate() != null) {
				dates.add(printed.getLdoDDate().getDate().getYear());
			}
			for (SourceInter inter : printed.getSourceIntersSet()) {
				if (inter.getLdoDDate() != null) {
					dates.add(inter.getLdoDDate().getDate().getYear());
				}
			}
		}
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		vector = buildVector(dates, vector);
		return vector;
	}

	private List<Double> buildVector(int date, List<Double> vector) {
		int start = date - getStartYear();
		double degree = getWeight() / vector.size();
		double j = getWeight();
		for (int i = start; i >= 0 && j > 0 && i < vector.size() && j >= vector.get(i); i--, j -= degree) {
			vector.set(i, j);
		}

		j = getWeight();
		for (int i = start; i < vector.size() && j > 0 && j >= vector.get(i); i++, j -= degree) {
			vector.set(i, j);
		}
		return vector;
	}

	private List<Double> buildVector(Set<Integer> dates, List<Double> vector) {
		for (int date : dates) {
			vector = buildVector(date, vector);
		}
		return vector;
	}

	private int getNumberOfYears() {
		if (getEndYear() != null && getStartYear() != null)
			return 1 + getEndYear() - getStartYear();
		else
			return 0;
	}

	private Integer getEndYear() {
		if (endYear == null) {
			getYears();
		}
		return endYear;
	}

	private Integer getStartYear() {
		if (startYear == null) {
			getYears();
		}
		return startYear;
	}

	private void getYears() {
		TreeSet<Integer> years = new TreeSet<Integer>();
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			for (FragInter inter : fragment.getFragmentInterSet()) {
				if (inter.getLdoDDate() != null) {
					years.add(inter.getLdoDDate().getDate().getYear());
				}
			}
			for (Source source : fragment.getSourcesSet()) {
				if (source.getType().equals(SourceType.MANUSCRIPT)) {
					ManuscriptSource manu = (ManuscriptSource) source;
					if (manu.getLdoDDate() != null) {
						years.add(manu.getLdoDDate().getDate().getYear());
					}
					for (SourceInter inter : manu.getSourceIntersSet())
						if (inter.getLdoDDate() != null) {
							years.add(inter.getLdoDDate().getDate().getYear());
						}
				} else if (source.getType().equals(SourceType.PRINTED)) {
					PrintedSource printed = (PrintedSource) source;
					if (printed.getLdoDDate() != null) {
						years.add(printed.getLdoDDate().getDate().getYear());
					}
					for (SourceInter inter : printed.getSourceIntersSet())
						if (inter.getLdoDDate() != null) {
							years.add(inter.getLdoDDate().getDate().getYear());
						}
				}
			}
		}

		if (years.size() > 0) {
			startYear = years.first();
			endYear = years.last();
		}
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setDateWeight(getWeight());
	}

	@Override
	protected List<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(getNumberOfYears(), 0.));
	}

	@Override
	public String getTitle() {
		return "DyDate";
	}

}