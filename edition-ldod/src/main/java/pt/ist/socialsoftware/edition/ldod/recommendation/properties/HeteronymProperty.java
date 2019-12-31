package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeteronymProperty extends Property {
    private static final List<Heteronym> heteronymList = LdoD.getInstance().getSortedHeteronyms();

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
    double[] extractVector(ExpertEditionInter expertEditionInter) {
        List<Heteronym> foundHeteronyms = new ArrayList<>();

        foundHeteronyms.add(expertEditionInter.getHeteronym());

        return buildVector(foundHeteronyms);
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
        for (FragInter inter : fragment.getFragmentInterSet()) {
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

    @Override
    public String getTitle() {
        return "Heteronym";
    }

    @Override
    protected String getConcreteTitle(FragInter inter) {
        String title = "";
        Set<Heteronym> heteronyms = new HashSet<>();
        for (FragInter intr : inter.getFragment().getFragmentInterSet()) {
            heteronyms.add(intr.getHeteronym());
        }
        for (Source source : inter.getFragment().getSourcesSet()) {
            for (SourceInter intr : source.getSourceIntersSet()) {
                heteronyms.add(intr.getHeteronym());
            }
        }

        if (heteronyms.size() != 1 && heteronyms.contains(NullHeteronym.getNullHeteronym())) {
            heteronyms.remove(NullHeteronym.getNullHeteronym());
        }

        for (Heteronym heteronym : HeteronymProperty.heteronymList) {
            if (heteronyms.contains(heteronym)) {
                title += "," + heteronym.getName();
            }
        }

        if (title.length() > 0) {
            title = title.substring(1);
        }

        return title;
    }

}