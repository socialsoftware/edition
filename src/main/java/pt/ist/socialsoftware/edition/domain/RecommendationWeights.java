package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.recommendation.dto.PropertyWithLevel;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.Property.PropertyCache;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;

public class RecommendationWeights extends RecommendationWeights_Base {

	public RecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		super();
		setUser(user);
		setVirtualEdition(virtualEdition);
		setHeteronymWeight(0.);
		setHeteronymLevel(0);
		setDateWeight(0.);
		setDateLevel(0);
		setTextWeight(0.);
		setTextLevel(0);
		setTaxonomyWeight(0.);
		setTaxonomyLevel(0);
	}

	@Atomic(mode = TxMode.WRITE)
	public void setWeights(List<Property> properties) {
		for (Property property : properties) {
			property.userWeightAndLevel(this, 0);
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public void setWeightsAndLevels(List<PropertyWithLevel> properties) {
		for (PropertyWithLevel property : properties) {
			property.userWeightAndLevel(this);
		}
	}

	public List<Property> getProperties() {
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
			result.add(new TaxonomyProperty(getTaxonomyWeight(), getVirtualEdition().getTaxonomy(), PropertyCache.OFF));
		}

		return result;
	}

	public List<PropertyWithLevel> getPropertiesWithLevel() {
		List<PropertyWithLevel> result = new ArrayList<>();

		result.add(new PropertyWithLevel(getHeteronymLevel(), new HeteronymProperty(getHeteronymWeight())));
		result.add(new PropertyWithLevel(getDateLevel(), new DateProperty(getDateWeight())));
		result.add(new PropertyWithLevel(getTextLevel(), new TextProperty(getTextWeight())));
		result.add(new PropertyWithLevel(getTaxonomyLevel(),
				new TaxonomyProperty(getTaxonomyWeight(), getVirtualEdition().getTaxonomy(), PropertyCache.OFF)));

		return result;
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setUser(null);
		setVirtualEdition(null);

		deleteDomainObject();
	}

}
