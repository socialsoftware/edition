package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
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

public class DateProperty extends StorableProperty {
	private static Integer STARTYEAR = 1900;
	private static Integer ENDYEAR = 1940;

	private static Integer getEndYear() {
		if (ENDYEAR == null) {
			getYears();
		}
		return ENDYEAR;
	}

	private static int getNumberOfYears() {
		if (getEndYear() != null && getStartYear() != null) {
			return 1 + getEndYear() - getStartYear();
		} else
			return 0;
	}

	private static Integer getStartYear() {
		if (STARTYEAR == null) {
			getYears();
		}
		return STARTYEAR;
	}

	private static void getYears() {
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
			STARTYEAR = years.first();
			ENDYEAR = years.last();
		}
	}

	public DateProperty() {
		super();
	}

	public DateProperty(double weight) {
		super(weight);
	}

	public DateProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	private List<Double> buildVector(int date, List<Double> vector) {
		int start = date - getStartYear();
		// double degree = 1.0 / vector.size();
		double degree = 0.1;
		double j = 1.0;
		for (int i = start; i >= 0 && j > 0 && i < vector.size() && j >= vector.get(i); i--, j -= degree) {
			vector.set(i, j);
		}
		j = 1.0;
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

	@Override
	public Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		if (expertEditionInter.getLdoDDate() != null) {
			vector = buildVector(expertEditionInter.getLdoDDate().getDate().getYear(), vector);
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
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
	public Collection<Double> extractVector(Source source) {
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

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		if (sourceInter.getLdoDDate() != null)
			vector = buildVector(sourceInter.getLdoDDate().getDate().getYear(), vector);
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(getNumberOfYears(), 0.));
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setDateWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Date";
	}

	@Override
	public String getConcreteTitle(FragInter intr) {

		Set<Integer> dates = new TreeSet<Integer>();
		for (FragInter inter : intr.getFragment().getFragmentInterSet()) {
			if (inter.getLdoDDate() != null) {
				dates.add(inter.getLdoDDate().getDate().getYear());
			}
		}
		for (Source source : intr.getFragment().getSourcesSet()) {
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
		}

		String title = "";
		for (int date : dates) {
			title += ":" + date;
		}

		if (title.length() > 0)
			title = title.substring(1);

		return title;
	}

}