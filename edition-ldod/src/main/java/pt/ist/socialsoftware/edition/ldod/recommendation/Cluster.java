package pt.ist.socialsoftware.edition.ldod.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class Cluster {
	private static Logger logger = LoggerFactory.getLogger(Cluster.class);

	private Double value;
	private final Map<Double, Cluster> nodes = new TreeMap<>(Collections.reverseOrder());
	private final VSMVirtualEditionInterRecommender vsmFragInterRecommender;
	private final VirtualEditionInter inter;
	private final List<VirtualEditionInter> inters;
	private final Map<Integer, Collection<Property>> propertiesMap;
	private final int order;

	public Cluster(VSMVirtualEditionInterRecommender vsmFragInterRecommender, VirtualEditionInter inter,
			Collection<VirtualEditionInter> inters, Map<Integer, Collection<Property>> propertiesMap) {
		this.vsmFragInterRecommender = vsmFragInterRecommender;
		this.inter = inter;
		this.propertiesMap = propertiesMap;
		this.order = 0;
		this.inters = new ArrayList<>(inters);
	}

	public Cluster(VSMVirtualEditionInterRecommender vsmFragInterRecommender, Double value, VirtualEditionInter inter,
			Map<Integer, Collection<Property>> propertiesMap, int level) {
		this.vsmFragInterRecommender = vsmFragInterRecommender;
		this.value = value;
		this.inter = inter;
		this.propertiesMap = propertiesMap;
		this.order = level;
		this.inters = new ArrayList<>();
	}

	// THIS CLUSTERING STRATEGY NEEDS TO BE RETHOUGHT, SO IT IS NOT BEING USED
	public void buildCluster() {
		if (propertiesMap.containsKey(order) && inters.size() > 0) {
			Collection<Property> properties = propertiesMap.get(order);
			List<Entry<VirtualEditionInter, Double>> mostSimilarItems = vsmFragInterRecommender
					.getMostSimilarItems(inter, inters, properties);
			for (Entry<VirtualEditionInter, Double> entry : mostSimilarItems) {
				if (!nodes.containsKey(entry.getValue())) {
					nodes.put(entry.getValue(),
							new Cluster(vsmFragInterRecommender, entry.getValue(), inter, propertiesMap, order + 1));
				}
				nodes.get(entry.getValue()).addFragInter(entry.getKey());
			}
			for (Entry<Double, Cluster> entry : nodes.entrySet()) {
				entry.getValue().buildCluster();
			}
		} else {
			List<Integer> keys = new ArrayList<>(propertiesMap.keySet());
			if (keys.size() == 0) {
				nodes.put(1.0, new Cluster(vsmFragInterRecommender, 1.0, inter, propertiesMap, order + 1));
				nodes.get(1.0).addAllFragInter(inters);
				return;
			}
			Collections.sort(keys);
			int max = keys.get(keys.size() - 1);
			if (max > order) {
				nodes.put(1.0, new Cluster(vsmFragInterRecommender, 1.0, inter, propertiesMap, order + 1));
				nodes.get(1.0).addAllFragInter(inters);
				for (Entry<Double, Cluster> entry : nodes.entrySet()) {
					entry.getValue().buildCluster();
				}
			}
		}
	}

	private void addFragInter(VirtualEditionInter inter) {
		inters.add(inter);
	}

	private void addAllFragInter(List<VirtualEditionInter> ints) {
		inters.addAll(ints);
	}

	public void print() {
		for (Entry<Double, Cluster> entry : nodes.entrySet()) {
			String s = StringUtils.repeat("\t", order) + "(";
			List<Property> properties = new ArrayList<>(getProperties());
			for (int i = 0; i < properties.size() - 1; i++) {
				s += properties.get(i).getTitle() + ",";
			}
			if (properties.size() > 0) {
				s += properties.get(properties.size() - 1).getTitle();
			}
			s += ")";
			s += "(" + entry.getKey() + ")" + ":" + entry.getValue().size();

			logger.debug(s);

			entry.getValue().print();
		}
	}

	public int size() {
		if (nodes.size() == 0) {
			return inters.size();
		} else {
			int size = 0;
			for (Entry<Double, Cluster> entry : nodes.entrySet()) {
				size += entry.getValue().size();
			}
			return size;
		}
	}

	public List<VirtualEditionInter> getItems() {
		if (nodes.size() == 0) {
			return inters;
		} else {
			List<VirtualEditionInter> ints = new ArrayList<>();
			for (Entry<Double, Cluster> entry : nodes.entrySet()) {
				ints.addAll(entry.getValue().getItems());
			}
			return ints;
		}
	}

	public Collection<Property> getProperties() {
		if (propertiesMap.containsKey(order))
			return propertiesMap.get(order);
		else
			return new ArrayList<>();
	}

	public int getNumberOfIterations() {
		if (nodes.size() == 0) {
			if (getProperties().size() == 0)
				return order;
			else
				return order + 1;
		} else {
			int max = 1, it;
			for (Entry<Double, Cluster> entry : nodes.entrySet()) {
				it = entry.getValue().getNumberOfIterations();
				max = it > max ? it : max;
			}
			return max;
		}
	}

	public Map<Double, Cluster> getNodes() {
		return nodes;
	}

	public List<VirtualEditionInter> getInters() {
		return inters;
	}

	public int getOrder() {
		return order;
	}

	public VirtualEditionInter getFirst() {
		List<VirtualEditionInter> items = getItems();
		if (!items.isEmpty())
			return items.get(0);
		return null;
	}

	public String getTitle() {
		String title = "";

		List<VirtualEditionInter> items = getItems();

		if (items.isEmpty()) {
			for (Property property : propertiesMap.get(order)) {
				title += property.getTitle();
			}
			title += ":" + value;
			return title;

		} else {
			VirtualEditionInter intr = getItems().get(0);

			for (Property property : propertiesMap.get(order)) {
				title += property.getTitle(intr);
			}
			title += ":" + value;
		}

		return title;
	}

}