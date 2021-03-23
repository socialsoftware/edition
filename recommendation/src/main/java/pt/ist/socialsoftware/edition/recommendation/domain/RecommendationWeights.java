package pt.ist.socialsoftware.edition.recommendation.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;

import java.util.ArrayList;
import java.util.List;

public class RecommendationWeights extends RecommendationWeights_Base {

    public RecommendationWeights(String user, String virtualEditionAcronym) {
        setUser(user);
        setVirtualEditionAcronym(virtualEditionAcronym);
        setHeteronymWeight(0.);
        setDateWeight(0.);
        setTextWeight(0.);
        setTaxonomyWeight(0.);

        setRecommendationModule(RecommendationModule.getInstance());
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        setRecommendationModule(null);

        deleteDomainObject();
    }

    @Atomic(mode = TxMode.WRITE)
    public void setWeightsZero() {
        setHeteronymWeight(0.);
        setDateWeight(0.);
        setTextWeight(0.);
        setTaxonomyWeight(0.);
    }

    @Atomic(mode = TxMode.WRITE)
    public void setWeights(List<Property> properties) {
        for (Property property : properties) {
            property.userWeight(this);
        }
    }

    public List<Property> getPropertiesWithStoredWeights() {
        List<Property> result = new ArrayList<>();
        if (getHeteronymWeight() > 0.0) {
            result.add(new HeteronymProperty(getHeteronymWeight()));
        }
        if (getDateWeight() > 0.0) {
            result.add(new DateProperty(getDateWeight()));
        }
        if (getTextWeight() > 0.0) {
            result.add(new TextProperty(getTextWeight()));
        }
        if (getTaxonomyWeight() > 0.0) {
            result.add(new TaxonomyProperty(getTaxonomyWeight(), getVirtualEditionAcronym(),
                    Property.PropertyCache.OFF));
        }

        return result;
    }

}
