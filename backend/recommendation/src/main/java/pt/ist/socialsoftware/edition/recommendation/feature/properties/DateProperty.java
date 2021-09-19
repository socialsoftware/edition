package pt.ist.socialsoftware.edition.recommendation.feature.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.SourceDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;




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

    private double[] addDateToVector(int date, double[] vector) {
        int start = date - STARTYEAR;
        // double degree = 1.0 / vector.size();
        double degree = 0.1;
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
    double[] extractVector(ScholarInterDto scholarInterDto) {
        Set<Integer> dates = new HashSet<>();
        if (scholarInterDto.getLdoDDate() != null) {
            dates.add(scholarInterDto.getLdoDDate().getDate().getYear());
        }
        return buildVector(dates);
    }

    @Override
    double[] extractVector(VirtualEditionInterDto virtualEditionInter) {
        Set<Integer> dates = new HashSet<>();
        ScholarInterDto scholarInterDto = virtualEditionInter.getLastUsed();
        if (scholarInterDto.getLdoDDate() != null) {
            dates.add(scholarInterDto.getLdoDDate().getDate().getYear());
        }
        return buildVector(dates);
    }

    @Override
    double[] extractVector(FragmentDto fragment) {
        Set<Integer> dates = new HashSet<>();
        for (ScholarInterDto inter : fragment.getScholarInterDtoSet()) {
            if (inter.getLdoDDate() != null) {
                dates.add(inter.getLdoDDate().getDate().getYear());
            }
        }
        for (SourceDto source : fragment.getSourcesSet()) {
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

}