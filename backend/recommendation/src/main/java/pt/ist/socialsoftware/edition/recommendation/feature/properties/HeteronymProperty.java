package pt.ist.socialsoftware.edition.recommendation.feature.properties;


import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.HeteronymDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.SourceDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;




import java.util.ArrayList;
import java.util.List;

public class HeteronymProperty extends Property {
    private final List<HeteronymDto> heteronymList = this.recommendationRequiresInterface.getSortedHeteronymsList();

    public HeteronymProperty(double weight) {
        super(weight, PropertyCache.ON);
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