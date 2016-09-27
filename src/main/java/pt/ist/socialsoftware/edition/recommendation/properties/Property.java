package pt.ist.socialsoftware.edition.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = HeteronymProperty.class, name = Property.HETERONYM),
		@JsonSubTypes.Type(value = DateProperty.class, name = Property.DATE),
		@JsonSubTypes.Type(value = TextProperty.class, name = Property.TEXT),
		@JsonSubTypes.Type(value = TaxonomyProperty.class, name = Property.SPECIFICTAXONOMY) })
public abstract class Property {
	public static final String HETERONYM = "heteronym";
	public static final String DATE = "date";
	public static final String TEXT = "text";
	public static final String TAXONOMY = "taxonomy";
	public static final String SPECIFICTAXONOMY = "specific-taxonomy";

	private final double weight;

	public Property(double weight) {
		this.weight = weight;
	}

	abstract double[] getDefaultVector();

	public abstract void userWeights(RecommendationWeights recommendationWeights);

	abstract double[] extractVector(VirtualEditionInter virtualEditionInter);

	abstract double[] extractVector(Fragment fragmnet);

	public void prepareToLoadProperty(VirtualEditionInter virtualEditionInter1,
			VirtualEditionInter virtualEditionInter2) {
	}

	public void prepareToLoadProperty(Fragment fragment1, Fragment fragment2) {
	}

	public double[] loadProperty(VirtualEditionInter virtualEditionInter) {
		return extractVector(virtualEditionInter);
	}

	public double[] loadProperty(Fragment fragment) {
		return extractVector(fragment);
	}

	public double getWeight() {
		return weight;
	}

	public abstract String getTitle();

	public final String getTitle(FragInter inter) {
		return inter != null ? getConcreteTitle(inter) : getTitle();
	}

	protected String getConcreteTitle(FragInter inter) {
		return getTitle();
	};

}