package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.TextProperty;

public class RecommendationWeights extends RecommendationWeights_Base {

	public RecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		super();
		setUser(user);
		setVirtualEdition(virtualEdition);
		setHeteronymWeight(0.);
		setDateWeight(0.);
		setTextWeight(0.);
		setTaxonomyWeight(0.);
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
			result.add(new TaxonomyProperty(getTaxonomyWeight(), getVirtualEdition().getTaxonomy(),
					Property.PropertyCache.OFF));
		}

		return result;
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setUser(null);
		setVirtualEdition(null);

		deleteDomainObject();
	}

}
