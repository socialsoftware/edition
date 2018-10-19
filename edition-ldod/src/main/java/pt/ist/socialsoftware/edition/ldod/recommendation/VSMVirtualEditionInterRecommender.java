package pt.ist.socialsoftware.edition.ldod.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.dto.InterDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

public class VSMVirtualEditionInterRecommender extends VSMRecommender<VirtualEditionInter> {
	@Override
	protected void prepareToLoadProperty(VirtualEditionInter t1, VirtualEditionInter t2, Property property) {
		property.prepareToLoadProperty(t1, t2);
	}

	@Override
	protected double[] loadProperty(VirtualEditionInter t1, Property property) {
		return property.loadProperty(t1);
	}

	public Cluster getCluster(VirtualEditionInter inter, Collection<VirtualEditionInter> inters,
			Map<Integer, Collection<Property>> propertiesMap) {
		List<VirtualEditionInter> iters = new ArrayList<>(inters);
		Cluster cluster = new Cluster(this, inter, iters, propertiesMap);
		cluster.buildCluster();
		cluster.print();
		return cluster;
	}

	public List<InterDistancePairDto> getMostSimilarItemsAsListOfPairs(VirtualEditionInter virtualEditionInter,
			List<VirtualEditionInter> inters, List<Property> properties) {
		List<InterDistancePairDto> result = new ArrayList<>();
		VirtualEditionInter nextItem = virtualEditionInter;
		while (!inters.isEmpty()) {
			InterDistancePairDto pair = getMostSimilarInterPairDto(nextItem, inters, properties);
			result.add(pair);
			nextItem = pair.getInter();
			inters.remove(nextItem);
		}

		return result;
	}

	private InterDistancePairDto getMostSimilarInterPairDto(VirtualEditionInter item, List<VirtualEditionInter> inters,
			List<Property> properties) {
		VirtualEditionInter nextInter = null;
		double max = Double.NEGATIVE_INFINITY;
		double similarity;
		for (VirtualEditionInter otherItem : inters) {
			if (otherItem != item) {
				similarity = calculateSimilarity(item, otherItem, properties);
				if (similarity > max) {
					nextInter = otherItem;
					max = similarity;
				}
			}
		}

		return new InterDistancePairDto(nextInter, max);
	}

}