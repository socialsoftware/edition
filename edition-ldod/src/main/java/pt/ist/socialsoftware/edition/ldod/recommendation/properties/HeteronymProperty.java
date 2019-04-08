package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.ArrayList;
import java.util.List;

public class HeteronymProperty extends Property {
    private static final List<Heteronym> heteronymList = Text.getInstance().getSortedHeteronyms();

    public HeteronymProperty(double weight) {
        super(weight, PropertyCache.ON);
    }

    public HeteronymProperty(@JsonProperty("weight") String weight) {
        this(Double.parseDouble(weight));
    }

    private double[] buildVector(List<Heteronym> foundHeteronyms) {
        double[] vector = getDefaultVector();
        int i = 0;
        for (Heteronym heteronym : HeteronymProperty.heteronymList) {
            if (foundHeteronyms.contains(heteronym)) {
                vector[i] = 1.0;
            } else {
                vector[i] = 0.0;
            }
            i++;
        }
        return vector;
    }

    @Override
    public double[] extractVector(VirtualEditionInter virtualEditionInter) {
        List<Heteronym> foundHeteronyms = new ArrayList<>();

        foundHeteronyms.add(virtualEditionInter.getLastUsed().getHeteronym());

        return buildVector(foundHeteronyms);
    }

    @Override
    public double[] extractVector(Fragment fragment) {
        List<Heteronym> foundHeteronyms = new ArrayList<>();
        for (ScholarInter inter : fragment.getScholarInterSet()) {
            foundHeteronyms.add(inter.getHeteronym());
        }
        for (Source source : fragment.getSourcesSet()) {
            for (SourceInter inter : source.getSourceIntersSet()) {
                foundHeteronyms.add(inter.getHeteronym());
            }
        }
        return buildVector(foundHeteronyms);
    }

    @Override
    protected double[] getDefaultVector() {
        return new double[heteronymList.size()];
    }

    @Override
    public void userWeight(RecommendationWeights recommendationWeights) {
        recommendationWeights.setHeteronymWeight(getWeight());
    }

}