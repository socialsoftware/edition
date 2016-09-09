package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;

public class RecommendationWeights extends RecommendationWeights_Base {

	public RecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		super();
		setUser(user);
		setVirtualEdition(virtualEdition);
		setHeteronymWeight(0.);
		setDateWeight(0.);
		setTextWeight(0.);
	}

	@Atomic(mode = TxMode.WRITE)
	public void setWeights(List<Property> properties) {
		for (Property property : properties) {
			property.userWeights(this);
		}
	}

	public List<Property> getProperties() {
		List<Property> properties = new ArrayList<Property>();
		if (getHeteronymWeight() > 0.0) {
			properties.add(new HeteronymProperty(getHeteronymWeight()));
		}
		if (getDateWeight() > 0.0) {
			properties.add(new DateProperty(getDateWeight()));
		}
		if (getTextWeight() > 0.0) {
			properties.add(new TextProperty(getTextWeight()));
		}
		if (getTaxonomyWeight() > 0.0) {
			properties.add(new TaxonomyProperty(getTaxonomyWeight(), getVirtualEdition().getTaxonomy()));
		}

		return properties;
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setUser(null);
		setVirtualEdition(null);

		deleteDomainObject();
	}

}
