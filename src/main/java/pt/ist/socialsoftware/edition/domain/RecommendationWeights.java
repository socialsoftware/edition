package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.WeightTaxonomyProperty;

public class RecommendationWeights extends RecommendationWeights_Base {

	public RecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		super();
		setUser(user);
		setVirtualEdition(virtualEdition);
		setEditionWeight(0.);
		setHeteronymWeight(0.);
		setDateWeight(0.);
		setManuscriptWeight(0.);
		setTypescriptWeight(0.);
		setPublicationWeight(0.);
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
		if (getEditionWeight() > 0.0) {
			properties.add(new EditionProperty(getEditionWeight()));
		}
		if (getHeteronymWeight() > 0.0) {
			properties.add(new HeteronymProperty(getHeteronymWeight()));
		}
		if (getDateWeight() > 0.0) {
			properties.add(new DateProperty(getDateWeight()));
		}
		if (getManuscriptWeight() > 0.0) {
			properties.add(new ManuscriptProperty(getManuscriptWeight()));
		}
		if (getTypescriptWeight() > 0.0) {
			properties.add(new TypescriptProperty(getTypescriptWeight()));
		}
		if (getPublicationWeight() > 0.0) {
			properties.add(new PrintedProperty(getTypescriptWeight()));
		}
		if (getTextWeight() > 0.0) {
			properties.add(new TextProperty(getTextWeight()));
		}

		for (TaxonomyWeight taxonomyWeight : getTaxonomyWeightSet()) {
			if (taxonomyWeight.getWeight() > 0.) {
				properties.add(new WeightTaxonomyProperty(taxonomyWeight.getWeight(), taxonomyWeight.getTaxonomy()));
			}
		}

		return properties;
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setUser(null);
		setVirtualEdition(null);

		for (TaxonomyWeight taxonomyWeight : getTaxonomyWeightSet()) {
			taxonomyWeight.remove();
		}

		deleteDomainObject();
	}

	public TaxonomyWeight getTaxonomyWeight(Taxonomy taxonomy) {
		for (TaxonomyWeight weight : getTaxonomyWeightSet()) {
			if (weight.getTaxonomy() == taxonomy) {
				return weight;
			}
		}
		return LdoD.getInstance().createTaxonomyWeight(getUser(), getVirtualEdition(), taxonomy);
	}

}
