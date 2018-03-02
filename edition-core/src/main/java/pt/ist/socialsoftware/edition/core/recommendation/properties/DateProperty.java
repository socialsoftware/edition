package pt.ist.socialsoftware.edition.core.recommendation.properties;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.core.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.Source;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;

public class DateProperty extends Property {
	private static Logger logger = LoggerFactory.getLogger(DateProperty.class);

	private static Integer STARTYEAR = 1913;
	private static Integer ENDYEAR = 1934;

	private static int getNumberOfYears() {
		return ENDYEAR - STARTYEAR + 1;
	}

	public DateProperty(double weight) {
		super(weight, PropertyCache.ON);
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
	double[] extractVector(VirtualEditionInter virtualEditionInter) {
		return extractVector(virtualEditionInter.getFragment());
	}

	@Override
	double[] extractVector(Fragment fragment) {
		Set<Integer> dates = new HashSet<>();
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
	protected double[] getDefaultVector() {
		return new double[getNumberOfYears()];
	}

	@Override
	public void userWeight(RecommendationWeights recommendationWeights) {
		recommendationWeights.setDateWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Date";
	}

	@Override
	public String getConcreteTitle(FragInter fragInter) {
		Set<Integer> dates = new HashSet<>();
		for (FragInter inter : fragInter.getFragment().getFragmentInterSet()) {
			if (inter.getLdoDDate() != null) {
				dates.add(inter.getLdoDDate().getDate().getYear());
			}
		}
		for (Source source : fragInter.getFragment().getSourcesSet()) {
			if (source.getLdoDDate() != null) {
				dates.add(source.getLdoDDate().getDate().getYear());
			}
		}

		String title = "";
		for (int date : dates) {
			title += "," + date;
		}

		if (title.length() > 0)
			title = title.substring(1);

		return title;
	}

}