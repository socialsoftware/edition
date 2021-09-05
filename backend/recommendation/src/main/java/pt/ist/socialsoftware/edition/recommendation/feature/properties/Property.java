package pt.ist.socialsoftware.edition.recommendation.feature.properties;


import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.recommendation.feature.StoredVectors;




public abstract class Property {
    protected RecommendationRequiresInterface recommendationRequiresInterface = RecommendationRequiresInterface.getInstance();

    public enum PropertyCache {
        ON, OFF
    }

    private final double weight;

    private PropertyCache cached;

    public Property(double weight, PropertyCache cached) {
        this.weight = weight;
        this.cached = cached;
    }

    abstract double[] getDefaultVector();

    public abstract void userWeight(RecommendationWeights recommendationWeights);

    abstract double[] extractVector(ScholarInterDto scholarInterDto);

    abstract double[] extractVector(VirtualEditionInterDto virtualEditionInter);

    abstract double[] extractVector(FragmentDto fragmnet);

    public void prepareToLoadProperty(ScholarInterDto t1, ScholarInterDto t2) {
    }

    public void prepareToLoadProperty(VirtualEditionInterDto virtualEditionInter1,
                                      VirtualEditionInterDto virtualEditionInter2) {
    }

    public void prepareToLoadProperty(FragmentDto fragment1, FragmentDto fragment2) {
    }

    private double[] applyWeights(double[] vector, double weight) {
        double[] result = new double[vector.length];
        if (weight == 1.0) {
            return vector;
        } else if (weight == 0.0) {
            return result;
        } else {
            for (int i = 0; i < vector.length; i++) {
                result[i] = vector[i] * weight;
            }
            return result;
        }
    }

    public final double[] loadProperty(ScholarInterDto scholarInterDto) {
        if (this.cached.equals(PropertyCache.ON)) {
            String interXmlId = scholarInterDto.getXmlId();
            double[] vector = StoredVectors.getInstance().get(this, interXmlId);
            if (vector == null) {
                vector = extractVector(scholarInterDto);
                StoredVectors.getInstance().put(this, interXmlId, vector);
            }
            return applyWeights(vector, getWeight());
        } else {
            return applyWeights(extractVector(scholarInterDto), getWeight());
        }
    }

    public final double[] loadProperty(VirtualEditionInterDto virtualEditionInter) {
        if (this.cached.equals(PropertyCache.ON)) {
            String xmlId = virtualEditionInter.getUsesScholarInterId();
            double[] vector = StoredVectors.getInstance().get(this, xmlId);
            if (vector == null) {
                vector = extractVector(virtualEditionInter);
                StoredVectors.getInstance().put(this, xmlId, vector);
            }
            return applyWeights(vector, getWeight());
        } else {
            return applyWeights(extractVector(virtualEditionInter), getWeight());
        }
    }

    public final double[] loadProperty(FragmentDto fragment) {
        if (this.cached.equals(PropertyCache.ON)) {
            double[] vector = StoredVectors.getInstance().get(this, fragment.getXmlId());
            if (vector == null) {
                vector = extractVector(fragment);
                StoredVectors.getInstance().put(this, fragment.getExternalId(), vector);
            }
            return applyWeights(vector, getWeight());
        } else {
            return applyWeights(extractVector(fragment), getWeight());
        }
    }

    public double getWeight() {
        return this.weight;
    }

    public void setCached(PropertyCache cached) {
        this.cached = cached;
    }


}