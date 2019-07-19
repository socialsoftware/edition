package pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.StoredVectors;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = HeteronymProperty.class, name = Property.HETERONYM),
        @JsonSubTypes.Type(value = DateProperty.class, name = Property.DATE),
        @JsonSubTypes.Type(value = TextProperty.class, name = Property.TEXT),
        @JsonSubTypes.Type(value = TaxonomyProperty.class, name = Property.TAXONOMY)})
public abstract class Property {
    protected RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();

    public static final String HETERONYM = "heteronym";
    public static final String DATE = "date";
    public static final String TEXT = "text";
    public static final String TAXONOMY = "taxonomy";

    public enum PropertyCache {
        ON, OFF
    }

    private final double weight;
    private final PropertyCache cached;

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

}