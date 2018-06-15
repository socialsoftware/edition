package pt.ist.socialsoftware.edition.ldod.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

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

}