package pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.ArrayList;
import java.util.List;

public class HeteronymProperty extends Property {
    private final List<HeteronymDto> heteronymList = this.recommendationRequiresInterface.getSortedHeteronymsList();

    public HeteronymProperty(double weight) {
        super(weight, PropertyCache.ON);
    }

    public HeteronymProperty(@JsonProperty("weight") String weight) {
        this(Double.parseDouble(weight));
    }

    private double[] buildVector(List<String> foundHeteronyms) {
        double[] vector = getDefaultVector();
        int i = 0;
        for (HeteronymDto heteronym : this.heteronymList) {
            if (foundHeteronyms.contains(heteronym.getName())) {
                vector[i] = 1.0;
            } else {
                vector[i] = 0.0;
            }
            i++;
        }
        return vector;
    }

    @Override
    double[] extractVector(ScholarInterDto scholarInterDto) {
        List<String> foundHeteronyms = new ArrayList<>();

        foundHeteronyms.add(scholarInterDto.getHeteronym().getName());

        return buildVector(foundHeteronyms);
    }

    @Override
    public double[] extractVector(VirtualEditionInterDto virtualEditionInter) {
        List<String> foundHeteronyms = new ArrayList<>();

        foundHeteronyms.add(virtualEditionInter.getLastUsed().getHeteronym().getName());

        return buildVector(foundHeteronyms);
    }

    @Override
    public double[] extractVector(FragmentDto fragment) {
        List<String> foundHeteronyms = new ArrayList<>();
        for (ScholarInterDto inter : fragment.getScholarInterDtoSet()) {
            foundHeteronyms.add(inter.getHeteronym().getName());
        }
        for (SourceDto source : fragment.getSourcesSet()) {
            foundHeteronyms.add(source.getHeteronym().getName());
        }
        return buildVector(foundHeteronyms);
    }

    @Override
    protected double[] getDefaultVector() {
        return new double[this.heteronymList.size()];
    }

    @Override
    public void userWeight(RecommendationWeights recommendationWeights) {
        recommendationWeights.setHeteronymWeight(getWeight());
    }

}