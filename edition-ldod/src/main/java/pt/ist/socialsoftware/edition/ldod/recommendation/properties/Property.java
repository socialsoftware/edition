package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.StoredVectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = HeteronymProperty.class, name = Property.HETERONYM),
		@JsonSubTypes.Type(value = DateProperty.class, name = Property.DATE),
		@JsonSubTypes.Type(value = TextProperty.class, name = Property.TEXT),
		@JsonSubTypes.Type(value = TaxonomyProperty.class, name = Property.TAXONOMY) })
public abstract class Property {
	public static final String HETERONYM = "heteronym";
	public static final String DATE = "date";
	public static final String TEXT = "text";
	public static final String TAXONOMY = "taxonomy";

	public enum PropertyCache {
		ON, OFF
	};

	private final double weight;
	private final PropertyCache cached;

	public Property(double weight, PropertyCache cached) {
		this.weight = weight;
		this.cached = cached;
	}

	abstract double[] getDefaultVector();

	public abstract void userWeight(RecommendationWeights recommendationWeights);

	abstract double[] extractVector(VirtualEditionInter virtualEditionInter);

	abstract double[] extractVector(Fragment fragmnet);

	public void prepareToLoadProperty(VirtualEditionInter virtualEditionInter1,
			VirtualEditionInter virtualEditionInter2) {
	}

	public void prepareToLoadProperty(Fragment fragment1, Fragment fragment2) {
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

	public final double[] loadProperty(VirtualEditionInter virtualEditionInter) {
		if (cached.equals(PropertyCache.ON)) {
			String externalId = virtualEditionInter.getFragment().getExternalId();
			double[] vector = StoredVectors.getInstance().get(this, externalId);
			if (vector == null) {
				vector = extractVector(virtualEditionInter);
				StoredVectors.getInstance().put(this, externalId, vector);
			}
			return applyWeights(vector, getWeight());
		} else {
			return applyWeights(extractVector(virtualEditionInter), getWeight());
		}
	}

	public final double[] loadProperty(Fragment fragment) {
		if (cached.equals(PropertyCache.ON)) {
			double[] vector = StoredVectors.getInstance().get(this, fragment.getExternalId());
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
		return weight;
	}

	public abstract String getTitle();

	public final String getTitle(FragInter inter) {
		return inter != null ? getTitle() + "(" + getConcreteTitle(inter) + ")" : getTitle();
	}

	protected String getConcreteTitle(FragInter inter) {
		return getTitle();
	}

}