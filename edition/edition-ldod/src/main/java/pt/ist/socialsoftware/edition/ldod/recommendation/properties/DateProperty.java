package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.HashSet;
import java.util.Set;

public class DateProperty extends Property {
    private static final Logger logger = LoggerFactory.getLogger(DateProperty.class);

    private static final Integer STARTYEAR = 1913;
    private static final Integer ENDYEAR = 1934;

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
        final double degree = 0.1;
        double j = 1.0;
        for (int i = start; i >= 0 && j > 0 && i < vector.length - 1 && j >= vector[i]; i--, j -= degree) {
            vector[i] = j;
        }
        j = 1.0;
        for (int i = start; i < vector.length - 1 && j > 0 && j >= vector[i]; i++, j -= degree) {
            vector[i] = j;
        }
        return vector;
    }

    private double[] buildVector(Set<Integer> dates) {
        double[] vector = getDefaultVector();

        if (dates.isEmpty()) {
            vector[vector.length - 1] = 1.0;

            return vector;
        }

        for (int date : dates) {
            addDateToVector(date, vector);
        }
        return vector;
    }

    @Override
    double[] extractVector(ExpertEditionInter expertEditionInter) {
        Set<Integer> dates = new HashSet<>();
        if (expertEditionInter.getLdoDDate() != null) {
            dates.add(expertEditionInter.getLdoDDate().getDate().getYear());
        }
        return buildVector(dates);
    }

    @Override
    double[] extractVector(VirtualEditionInter virtualEditionInter) {
        Set<Integer> dates = new HashSet<>();
        FragInter inter = virtualEditionInter.getLastUsed();
        if (inter.getLdoDDate() != null) {
            dates.add(inter.getLdoDDate().getDate().getYear());
        }
        return buildVector(dates);
    }

    @Override
    double[] extractVector(Fragment fragment) {
        Set<Integer> dates = new HashSet<>();
        for (FragInter inter : fragment.getExpertEditionInterSet()) {
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
        return new double[getNumberOfYears() + 1];
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

        if (title.length() > 0) {
            title = title.substring(1);
        }

        return title;
    }

}