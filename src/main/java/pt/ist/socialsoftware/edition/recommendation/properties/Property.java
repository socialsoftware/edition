package pt.ist.socialsoftware.edition.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

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

	protected abstract double[] getDefaultVector();

	public abstract void userWeights(RecommendationWeights recommendationWeights);

	protected double[] extractVector(ExpertEditionInter expertEditionInter) {
		return getDefaultVector();
	}

	protected double[] extractVector(Fragment fragmnet) {
		return getDefaultVector();
	}

	protected double[] extractVector(Source source) {
		return getDefaultVector();
	}

	protected double[] extractVector(SourceInter sourceInter) {
		return getDefaultVector();
	}

	protected double[] extractVector(VirtualEditionInter virtualEditionInter) {
		FragInter inter = virtualEditionInter.getLastUsed();
		switch (inter.getSourceType()) {
		case AUTHORIAL:
			return extractVector((SourceInter) inter);
		case EDITORIAL:
			return extractVector((ExpertEditionInter) inter);
		default:
			throw new LdoDException(this.getClass().getName() + ": " + virtualEditionInter.getTitle()
					+ " virtual inter cannot be used as source of a virtual edition interpretation in virtual edition "
					+ virtualEditionInter.getVirtualEdition().getAcronym());
		}
	}

	public void prepareToLoadProperty(Fragment frag1, Fragment frag2) {
	}

	public void prepareToLoadProperty(FragInter frag1, FragInter frag2) {
	}

	public double[] loadProperty(Fragment fragment) {
		return extractVector(fragment);
	}

	public double[] loadProperty(VirtualEditionInter virtualEditionInter) {
		return extractVector(virtualEditionInter);
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