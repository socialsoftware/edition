package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.Property.PropertyCache;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;

public class ReadingRecommendation {
	private static Logger logger = LoggerFactory.getLogger(ReadingRecommendation.class);

	private List<String> read = new ArrayList<String>();
	private double heteronymWeight = 0.0;
	private double dateWeight = 0.0;
	private double textWeight = 0.0;
	private double taxonomyWeight = 0.0;

	public ReadingRecommendation() {
	}

	public void clean() {
		this.read.clear();
		this.heteronymWeight = 0.0;
		this.dateWeight = 0.0;
		this.textWeight = 0.0;
		this.taxonomyWeight = 0.0;
	}

	private List<Property> getProperties() {
		List<Property> properties = new ArrayList<Property>();
		if (this.heteronymWeight > 0.0) {
			properties.add(new HeteronymProperty(this.heteronymWeight));
		}
		if (this.dateWeight > 0.0) {
			properties.add(new DateProperty(this.dateWeight));
		}
		if (this.textWeight > 0.0) {
			properties.add(new TextProperty(this.textWeight));
		}
		if (this.taxonomyWeight > 0.0) {
			properties.add(new TaxonomyProperty(this.taxonomyWeight,
					LdoD.getInstance().getArchiveEdition().getTaxonomy(), PropertyCache.ON));
		}
		return properties;
	}

	public Set<ExpertEditionInter> getNextRecommendations(String expertEditionInterId) {
		// logger.debug("getNextRecommendations textWeight:{}, read size:{},
		// read:{}", this.textWeight, this.read.size(),
		// this.read);

		List<Fragment> readFragments = this.read.stream()
				.map(id -> (ExpertEditionInter) FenixFramework.getDomainObject(id)).map(inter -> inter.getFragment())
				.collect(Collectors.toList());

		ExpertEditionInter toReadInter = FenixFramework.getDomainObject(expertEditionInterId);
		Fragment toReadFragment = toReadInter.getFragment();

		// if the fragment that is going to be read was already read, return to
		// that position of recommendation
		int index = readFragments.indexOf(toReadFragment);
		if (index != -1) {
			readFragments.subList(index, readFragments.size()).clear();
			read.subList(index, read.size()).clear();
		}

		// if all fragments minus 50 were already suggested clear the first 50
		// recommendations
		if (readFragments.size() == LdoD.getInstance().getFragmentsSet().size() - 50) {
			readFragments.subList(0, 50).clear();
			read.subList(0, 50).clear();
		}

		Set<Fragment> toBeRecommended = LdoD.getInstance().getFragmentsSet().stream()
				.filter(f -> !readFragments.contains(f)).collect(Collectors.toSet());

		this.read.add(expertEditionInterId);

		VSMFragmentRecommender recommender = new VSMFragmentRecommender();
		List<Property> properties = getProperties();
		List<Entry<Fragment, Double>> mostSimilars = recommender.getMostSimilarItems(toReadFragment, toBeRecommended,
				properties);

		Set<ExpertEditionInter> result = new HashSet<ExpertEditionInter>();
		Double value = mostSimilars.get(0).getValue();
		for (Entry<Fragment, Double> entry : mostSimilars) {
			// logger.debug("ReadingRecommendation value1:{}, value2:{}", value,
			// entry.getValue());
			// add all interpretations that are similar
			if (Math.abs(value - entry.getValue()) < 0.001 && result.size() < 5) {
				result.addAll(entry.getKey().getExpertEditionInters(toReadInter.getExpertEdition()));
				// if the most similar fragment does not have an interpretation
				// in this edition, use the next most similar fragment
			} else if (result.size() == 0) {
				value = entry.getValue();
				result.addAll(entry.getKey().getExpertEditionInters(toReadInter.getExpertEdition()));
			} else {
				break;
			}
		}

		return result;
	}

	public String prevRecommendation() {
		ExpertEditionInter result = getPrevRecommendation();
		if (result == null) {
			return null;
		}
		this.read.remove(this.read.size() - 1);
		this.read.remove(this.read.size() - 1);
		return result.getExternalId();
	}

	public ExpertEditionInter getPrevRecommendation() {
		if (this.read.size() < 2)
			return null;
		return FenixFramework.getDomainObject(this.read.get(this.read.size() - 2));
	}

	public void resetPrevRecommendations() {
		if (this.read.size() > 1) {
			this.read.subList(0, this.read.size() - 1).clear();
		}
	}

	public String getCurrentInterpretation() {
		return this.read.get(this.read.size() - 1);
	}

	public double getHeteronymWeight() {
		return this.heteronymWeight;
	}

	public void setHeteronymWeight(double heteronymWeight) {
		this.heteronymWeight = heteronymWeight;
	}

	public double getDateWeight() {
		return this.dateWeight;
	}

	public void setDateWeight(double dateWeight) {
		this.dateWeight = dateWeight;
	}

	public double getTextWeight() {
		return this.textWeight;

	}

	public void setTextWeight(double textWeight) {
		this.textWeight = textWeight;
	}

	public double getTaxonomyWeight() {
		return this.taxonomyWeight;
	}

	public void setTaxonomyWeight(double taxonomyWeight) {
		this.taxonomyWeight = taxonomyWeight;
	}

}
