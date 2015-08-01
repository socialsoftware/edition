package pt.ist.socialsoftware.edition.recommendation.properties;

import pt.ist.socialsoftware.edition.domain.RecommendationWeights;

public class TypescriptProperty extends AuthoralProperty {
	public static final String TYPESCRIPT = "datil";

	public TypescriptProperty() {
		super();
	}

	public TypescriptProperty(double weight) {
		super(weight);
	}

	@Override
	protected String getDocumentType() {
		return TYPESCRIPT;
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setTypescriptWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Typescript";
	}
}
