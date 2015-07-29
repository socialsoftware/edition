package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonProperty;

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
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class DateProperty extends StorableProperty {
	private static Integer STARTYEAR = 1910;
	private static Integer ENDYEAR = 1940;

	private static Integer getEndYear() {
		if(ENDYEAR == null) {
			getYears();
		}
		return ENDYEAR;
	}

	private static int getNumberOfYears() {
		if(getEndYear() != null && getStartYear() != null) {
			return 1 + getEndYear() - getStartYear();
		}
		else
			return 0;
	}

	private static Integer getStartYear() {
		if(STARTYEAR == null) {
			getYears();
		}
		return STARTYEAR;
	}

	private static void getYears() {
		TreeSet<Integer> years = new TreeSet<Integer>();
		for(Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			for(FragInter inter : fragment.getFragmentInterSet()) {
				if(inter.getDate() != null) {
					years.add(inter.getDate().getYear());
				}
			}
			for(Source source : fragment.getSourcesSet()) {
				if(source.getType().equals(SourceType.MANUSCRIPT)) {
					ManuscriptSource manu = (ManuscriptSource) source;
					if(manu.getDate() != null) {
						years.add(manu.getDate().getYear());
					}
					for(SourceInter inter : manu.getSourceIntersSet())
						if(inter.getDate() != null) {
							years.add(inter.getDate().getYear());
						}
				} else if(source.getType().equals(SourceType.PRINTED)) {
					PrintedSource printed = (PrintedSource) source;
					if(printed.getDate() != null) {
						years.add(printed.getDate().getYear());
					}
					for(SourceInter inter : printed.getSourceIntersSet())
						if(inter.getDate() != null) {
							years.add(inter.getDate().getYear());
						}
				}
			}
		}

		if(years.size() > 0) {
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
		double degree = 1 / vector.size();
		double j = 1.0;
		for(int i = start; i >= 0 && j > 0 && i < vector.size() && j >= vector.get(i); i--, j -= degree) {
			vector.set(i, j);
		}
		j = 1.0;
		for(int i = start; i < vector.size() && j > 0 && j >= vector.get(i); i++, j -= degree) {
			vector.set(i, j);
		}
		return vector;
	}

	private List<Double> buildVector(Set<Integer> dates, List<Double> vector) {
		for(int date : dates){
			vector = buildVector(date, vector);
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		if(expertEditionInter.getDate() != null) {
			vector = buildVector(expertEditionInter.getDate().getYear(), vector);
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		Set<Integer> dates = new TreeSet<Integer>();
		for(FragInter inter : fragment.getFragmentInterSet()) {
			if(inter.getDate() != null) {
				dates.add(inter.getDate().getYear());
			}
		}
		for(Source source : fragment.getSourcesSet()) {
			if(source.getType().equals(SourceType.MANUSCRIPT)){
				ManuscriptSource manu = (ManuscriptSource) source;
				if(manu.getDate() != null) {
					dates.add(manu.getDate().getYear());
				}
				for(SourceInter inter : manu.getSourceIntersSet()) {
					if(inter.getDate() != null) {
						dates.add(inter.getDate().getYear());
					}
				}
			} else if(source.getType().equals(SourceType.PRINTED)) {
				PrintedSource printed = (PrintedSource) source;
				if(printed.getDate() != null) {
					dates.add(printed.getDate().getYear());
				}
				for(SourceInter inter : printed.getSourceIntersSet()) {
					if(inter.getDate() != null) {
						dates.add(inter.getDate().getYear());
					}
				}
			}
		}
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		vector = buildVector(dates, vector);
		return vector;
	}

	@Override
	public Collection<Double> extractVector(Source source) {
		Set<Integer> dates = new TreeSet<Integer>();
		if(source.getType().equals(SourceType.MANUSCRIPT)) {
			ManuscriptSource manu = (ManuscriptSource) source;
			if(manu.getDate() != null) {
				dates.add(manu.getDate().getYear());
			}
			for(SourceInter inter : manu.getSourceIntersSet()) {
				if(inter.getDate() != null) {
					dates.add(inter.getDate().getYear());
				}
			}
		} else if(source.getType().equals(SourceType.PRINTED)) {
			PrintedSource printed = (PrintedSource) source;
			if(printed.getDate() != null) {
				dates.add(printed.getDate().getYear());
			}
			for(SourceInter inter : printed.getSourceIntersSet()) {
				if(inter.getDate() != null) {
					dates.add(inter.getDate().getYear());
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
		if(sourceInter.getDate() != null)
			vector = buildVector(sourceInter.getDate().getYear(), vector);
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter virtualEditionInter) {
		return virtualEditionInter.getLastUsed().accept(this);
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
}