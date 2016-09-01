package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = EditionProperty.class, name = Property.EDITION),
		@JsonSubTypes.Type(value = SourceProperty.class, name = Property.SOURCE),
		@JsonSubTypes.Type(value = ManuscriptProperty.class, name = Property.MANUSCRIPT),
		@JsonSubTypes.Type(value = TypescriptProperty.class, name = Property.TYPESCRIPT),
		@JsonSubTypes.Type(value = PrintedProperty.class, name = Property.PUBLICATION),
		@JsonSubTypes.Type(value = HeteronymProperty.class, name = Property.HETERONYM),
		@JsonSubTypes.Type(value = DateProperty.class, name = Property.DATE),
		@JsonSubTypes.Type(value = TextProperty.class, name = Property.TEXT),
		@JsonSubTypes.Type(value = TaxonomyProperty.class, name = Property.SPECIFICTAXONOMY) })
public abstract class Property {
	public static final String EDITION = "edition";
	public static final String SOURCE = "source";
	public static final String MANUSCRIPT = "manuscript";
	public static final String TYPESCRIPT = "typescript";
	public static final String PUBLICATION = "publication";
	public static final String HETERONYM = "heteronym";
	public static final String DATE = "date";
	public static final String TEXT = "text";
	public static final String TAXONOMY = "taxonomy";
	public static final String SPECIFICTAXONOMY = "specific-taxonomy";

	private final double weight;

	public Property() {
		weight = 1.;
	}

	public Property(double weight) {
		this.weight = weight;
	}

	protected abstract Collection<Double> getDefaultVector();

	public abstract void userWeights(RecommendationWeights recommendationWeights);

	protected Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		return new ArrayList<Double>(getDefaultVector());
	}

	protected Collection<Double> extractVector(Fragment fragmnet) {
		return new ArrayList<Double>(getDefaultVector());
	}

	protected Collection<Double> extractVector(Source source) {
		return new ArrayList<Double>(getDefaultVector());
	}

	protected Collection<Double> extractVector(SourceInter sourceInter) {
		return new ArrayList<Double>(getDefaultVector());
	}

	protected Collection<Double> extractVector(VirtualEditionInter virtualEditionInter) {
		return new ArrayList<Double>(getDefaultVector());
	}

	public void loadProperty(FragInter frag1, FragInter frag2) {
	}

	public void setFragmentsGroup(Fragment frag1, Fragment frag2) {
	}

	public Collection<Double> visit(ExpertEditionInter expertEditionInter) {
		return extractVector(expertEditionInter);
	}

	public Collection<Double> visit(FragInter fragInter) {
		return new ArrayList<Double>(getDefaultVector());
	}

	public Collection<Double> visit(Fragment fragment) {
		return extractVector(fragment);
	}

	public Collection<Double> visit(Source source) {
		return extractVector(source);
	}

	public Collection<Double> visit(SourceInter sourceInter) {
		return extractVector(sourceInter);
	}

	public Collection<Double> visit(VirtualEditionInter virtualEditionInter) {
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