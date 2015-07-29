package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragInterRecommender extends VSMRecommender<FragInter> {

	private static Collection<FragInter> interSet = null;

	@Override
	protected Collection<Double> acceptProperty(FragInter t1, Property property) {
		return t1.accept(property);
	}

	public Collection<FragInter> getClusteredEdition(FragInter inter, Collection<FragInter> inters, Map<Integer, Collection<Property>> propertiesMap) {
		Cluster cluster = getCluster(inter, inters, propertiesMap);
		List<FragInter> items = new ArrayList<FragInter>();
		items.add(inter);
		items.addAll(cluster.getItems());
		return items;
	}

	public Collection<FragInter> getClusteredEdition(FragInter inter, Map<Integer, Collection<Property>> properties) {
		return getClusteredEdition(inter, getDefaultSet(), properties);
	}

	@Override
	public Collection<FragInter> getDefaultSet() {
		if(interSet == null) {
			Set<FragInter> set = new HashSet<FragInter>();
			for(Fragment frag : LdoD.getInstance().getFragmentsSet()) {
				for(FragInter inter : frag.getFragmentInterSet()) {
					set.add(inter);
				}
			}
			interSet = Collections.unmodifiableCollection(set);
		}
		return interSet;
	}

	public List<FragInter> getRecommendedEditionWithoutDuplicatedFragments(FragInter fragInter, Collection<FragInter> inters,
			Collection<Property> properties) {
		List<Fragment> fragmentSet = new ArrayList<>(LdoD.getInstance().getFragmentsSet());
		List<FragInter> fragInterSet = new ArrayList<FragInter>(inters);
		List<FragInter> resultSet = new ArrayList<FragInter>();
		FragInter current = fragInter;
		resultSet.add(fragInter);
		fragInterSet.remove(fragInter);
		fragmentSet.remove(fragInter.getFragment());
		while(!fragInterSet.isEmpty()) {
			current = getMostSimilarItem(fragInter, fragInterSet, properties);
			if(fragmentSet.contains(current.getFragment())) {
				resultSet.add(current);
				fragmentSet.remove(current.getFragment());
			}
			fragInterSet.remove(current);
		}
		return resultSet;
	}

	public List<FragInter> getRecommendedEditionWithoutDuplicatedFragments(FragInter fragInter, Collection<Property> properties) {
		return getRecommendedEdition(fragInter, getDefaultSet(), properties);
	}

	public List<FragInter> getRecommendedEditionWithoutDuplicatedFragments(FragInter fragInter, Property property) {
		ArrayList<Property> properties = new ArrayList<>();
		properties.add(property);
		return getRecommendedEditionWithoutDuplicatedFragments(fragInter, properties);
	}

	public List<FragInter> getRecommendedEditionWithoutDuplicatedFragments(FragInter fragInter, Collection<FragInter> inters, Property property) {
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		return getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters, properties);
	}

	@Override
	protected void setFragmentsGroup(FragInter t1, FragInter t2, Property property) {
		property.setFragmentsGroup(t1, t2);
	}

	public Cluster getCluster(FragInter inter, Collection<FragInter> inters, Map<Integer, Collection<Property>> propertiesMap) {
		List<FragInter> iters = new ArrayList<>(inters);
		Cluster cluster = new Cluster(this, inter, iters, propertiesMap);
		cluster.buildCluster();
		cluster.print();
		return cluster;
	}


}