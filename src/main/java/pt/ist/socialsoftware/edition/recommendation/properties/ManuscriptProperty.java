package pt.ist.socialsoftware.edition.recommendation.properties;

import pt.ist.socialsoftware.edition.domain.RecommendationWeights;

public class ManuscriptProperty extends AuthoralProperty {

	public static final String MANUSCRIPTID = "manus";

	public ManuscriptProperty() {
		super();
	}

	public ManuscriptProperty(double weight) {
		super(weight);
	}

	@Override
	protected String getDocumentType() {
		return MANUSCRIPTID;
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setManuscriptWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Manuscript";
	}

}