package pt.ist.socialsoftware.edition.recommendation.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;

public class RecommendationModule extends RecommendationModule_Base {
    private static final Logger log = LoggerFactory.getLogger(RecommendationModule.class);

    public static RecommendationModule getInstance() {
        return FenixFramework.getDomainRoot().getRecommendationModule();
    }

    public RecommendationModule() {
        FenixFramework.getDomainRoot().setRecommendationModule(this);
        RecommendationRequiresInterface.getInstance();
    }

    public void remove() {
        getRecommendationWeightsSet().forEach(RecommendationWeights::remove);

        setRoot(null);

        deleteDomainObject();
    }

    public RecommendationWeights getRecommendationWeightsForUserInVirtualEdition(String user, String virtualEditionAcronym) {
        for (RecommendationWeights recommendationWeights : getRecommendationWeightsSet()) {
            if (recommendationWeights.getUser().equals(user)
                    && recommendationWeights.getVirtualEditionAcronym().equals(virtualEditionAcronym)) {
                return recommendationWeights;
            }
        }
        return createRecommendationWeightsForUser(user, virtualEditionAcronym);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public RecommendationWeights createRecommendationWeightsForUser(String user, String virtualEditionAcronym) {
        return new RecommendationWeights(user, virtualEditionAcronym);
    }


}
