package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public class DateProperty extends StorableProperty {
	private static Logger logger = LoggerFactory.getLogger(DateProperty.class);

	private static Integer STARTYEAR = 1913;
	private static Integer ENDYEAR = 1934;

	private static int getNumberOfYears() {
		return ENDYEAR - STARTYEAR + 1;
	}

	public DateProperty(double weight) {
		super(weight);
	}

	public DateProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	private double[] addDateToVector(int date, double[] vector) {
		int start = date - STARTYEAR;
		// double degree = 1.0 / vector.size();
		double degree = 0.1;
		double j = 1.0;
		for (int i = start; i >= 0 && j > 0 && i < vector.length && j >= vector[i]; i--, j -= degree) {
			vector[i] = j;
		}
		j = 1.0;
		for (int i = start; i < vector.length && j > 0 && j >= vector[i]; i++, j -= degree) {
			vector[i] = j;
		}
		return vector;
	}

	private double[] buildVector(Set<Integer> dates) {
		double vector[] = getDefaultVector();
		for (int date : dates) {
			addDateToVector(date, vector);
		}
		return vector;
	}

	@Override
	public double[] extractVector(Fragment fragment) {
		Set<Integer> dates = new HashSet<Integer>();
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
		return buildVector(dates);
	}

	@Override
	public double[] extractVector(Source source) {
		Set<Integer> dates = new HashSet<Integer>();
		if (source.getLdoDDate() != null) {
			dates.add(source.getLdoDDate().getDate().getYear());
		}

		for (SourceInter sourceInter : source.getSourceIntersSet()) {
			if (sourceInter.getLdoDDate() != null) {
				dates.add(sourceInter.getLdoDDate().getDate().getYear());
			}
		}
		return buildVector(dates);
	}

	@Override
	protected double[] extractVector(SourceInter sourceInter) {
		if (sourceInter.getLdoDDate() != null)
			return addDateToVector(sourceInter.getLdoDDate().getDate().getYear(), getDefaultVector());
		return getDefaultVector();
	}

	@Override
	protected double[] extractVector(ExpertEditionInter expertEditionInter) {
		if (expertEditionInter.getLdoDDate() != null)
			return addDateToVector(expertEditionInter.getLdoDDate().getDate().getYear(), getDefaultVector());
		return getDefaultVector();
	}

	@Override
	protected double[] getDefaultVector() {
		return new double[getNumberOfYears()];
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