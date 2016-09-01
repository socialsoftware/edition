package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMVirtualEditionInterRecommender extends VSMRecommender<VirtualEditionInter> {
	private VirtualEdition virtualEdition;

	private static Collection<VirtualEditionInter> interSet = null;

	public VSMVirtualEditionInterRecommender() {
		this.virtualEdition = null;
	}

	public VSMVirtualEditionInterRecommender(VirtualEdition virtualEdition) {
		this.virtualEdition = virtualEdition;
	}

	@Override
	protected Collection<Double> acceptProperty(VirtualEditionInter t1, Property property) {
		return t1.accept(property);
	}

	public Collection<VirtualEditionInter> getClusteredEdition(VirtualEditionInter inter,
			Collection<VirtualEditionInter> inters, Map<Integer, Collection<Property>> propertiesMap) {
		Cluster cluster = getCluster(inter, inters, propertiesMap);
		List<VirtualEditionInter> items = new ArrayList<VirtualEditionInter>();
		items.add(inter);
		items.addAll(cluster.getItems());
		return items;
	}

	public Collection<VirtualEditionInter> getClusteredEdition(VirtualEditionInter inter,
			Map<Integer, Collection<Property>> properties) {
		return getClusteredEdition(inter, getDefaultSet(), properties);
	}

	@Override
	public Collection<VirtualEditionInter> getDefaultSet() {
		if (interSet == null) {
			interSet = Collections.unmodifiableCollection(virtualEdition.getVirtualEditionIntersSet());
		}
		return interSet;
	}

	public List<VirtualEditionInter> getRecommendedEditionWithoutDuplicatedFragments(VirtualEditionInter fragInter,
			Collection<VirtualEditionInter> inters, Collection<Property> properties) {
		List<Fragment> fragmentSet = new ArrayList<>(LdoD.getInstance().getFragmentsSet());
		List<VirtualEditionInter> fragInterSet = new ArrayList<VirtualEditionInter>(inters);
		List<VirtualEditionInter> resultSet = new ArrayList<VirtualEditionInter>();
		VirtualEditionInter current = fragInter;
		resultSet.add(fragInter);
		fragInterSet.remove(fragInter);
		fragmentSet.remove(fragInter.getFragment());
		while (!fragInterSet.isEmpty()) {
			current = getMostSimilarItem(fragInter, fragInterSet, properties);
			if (fragmentSet.contains(current.getFragment())) {
				resultSet.add(current);
				fragmentSet.remove(current.getFragment());
			}
			fragInterSet.remove(current);
		}
		return resultSet;
	}

	public List<VirtualEditionInter> getRecommendedEditionWithoutDuplicatedFragments(VirtualEditionInter fragInter,
			Collection<Property> properties) {
		return getRecommendedEdition(fragInter, getDefaultSet(), properties);
	}

	public List<VirtualEditionInter> getRecommendedEditionWithoutDuplicatedFragments(VirtualEditionInter fragInter,
			Property property) {
		ArrayList<Property> properties = new ArrayList<>();
		properties.add(property);
		return getRecommendedEditionWithoutDuplicatedFragments(fragInter, properties);
	}

	public List<VirtualEditionInter> getRecommendedEditionWithoutDuplicatedFragments(VirtualEditionInter fragInter,
			Collection<VirtualEditionInter> inters, Property property) {
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		return getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters, properties);
	}

	@Override
	protected void loadProperty(VirtualEditionInter t1, VirtualEditionInter t2, Property property) {
		property.loadProperty(t1, t2);
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