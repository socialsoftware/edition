
package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;

import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;

public abstract class VSMRecommender<T> implements Recommender<T, Property> {
	public static final int NUMBEROFRECOMMENDATIONS = 5;

	protected static final Random RANDOM = new Random();
	protected abstract Collection<Double> acceptProperty(T item1, Property property);

	protected double calculateSimilarity(Collection<Double> item1, Collection<Double> item2) {
		System.out.println("item1 " + item1);
		System.out.println("item2 " + item2);
		double[] v1 = ArrayUtils.toPrimitive(item1.toArray(new Double[item1.size()]));
		double[] v2 = ArrayUtils.toPrimitive(item2.toArray(new Double[item2.size()]));
		return Vectors.calculateSimiliraty(v1, v2);
	}

	@Override
	public double calculateSimiliraty(T item1, T item2, Collection<Property> ps) {
		List<Double> vector1 = new ArrayList<Double>();
		List<Double> vector2 = new ArrayList<Double>();
		for(Property property : ps) {
			setFragmentsGroup(item1, item2, property);
			vector1.addAll(acceptProperty(item1, property));
			vector2.addAll(acceptProperty(item2, property));
		}
		return calculateSimilarity(vector1, vector2);
	}

	@Override
	public double calculateSimiliraty(T item1, T item2, Property property) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(property);
		return calculateSimiliraty(item1, item2, properties);
	}

	public Collection<Property> getDefaultProperties() {
		List<Property> properties = new ArrayList<Property>();
		properties.add(new EditionProperty());
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		properties.add(new HeteronymProperty());
		properties.add(new DateProperty());
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		properties.add(new PrintedProperty());
		properties.add(new TextProperty());
		return Collections.unmodifiableList(properties);
	}

	public abstract Collection<T> getDefaultSet();

	@Override
	public T getMostSimilarItem(T item, Collection<T> items, Collection<Property> properties) {
		List<T> newList = new ArrayList<>(items);
		newList.remove(item);
		T result = null;
		double max = Double.NEGATIVE_INFINITY;
		double similiraty;
		for(T otherItem : newList) {
			similiraty = calculateSimiliraty(item, otherItem, properties);
			if(similiraty > max) {
				result = otherItem;
				max = similiraty;
			}
		}
		return result;
	}

	@Override
	public T getMostSimilarItem(T item, Collection<T> items, Property property) {
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		return getMostSimilarItem(item, items, properties);
	}

	public T getMostSimilarItem(T item, List<Property> properties) {
		List<T> items = new ArrayList<>(getDefaultSet());
		items.remove(item);
		return getMostSimilarItem(item, items, properties);
	}

	public T getMostSimilarItem(T item, Property property) {
		List<T> items = new ArrayList<>(getDefaultSet());
		items.remove(item);
		return getMostSimilarItem(item, items, property);
	}

	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<Property> properties) {
		return getMostSimilarItems(item, properties, Double.NEGATIVE_INFINITY);
	}

	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<Property> properties, double treshold) {
		List<T> items = new ArrayList<>(getDefaultSet());
		items.remove(item);
		return getMostSimilarItems(item, items, properties, treshold);
	}

	public List<T> getMostSimilarItems(T item, Collection<Property> properties, int numberOfItems) {
		List<T> items = new ArrayList<>(getDefaultSet());
		items.remove(item);
		return getMostSimilarItems(item, items, properties, numberOfItems);
	}

	@Override
	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<T> items, Collection<Property> properties) {
		return getMostSimilarItems(item, items, properties, Double.NEGATIVE_INFINITY);
	}

	@Override
	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<T> items, Collection<Property> properties, double treshold) {
		List<T> itemSet = new ArrayList<>(items);
	//	itemSet.remove(item);
		double similarity;
		Map<T, Double> map = new HashMap<T, Double>();
		for(T it : itemSet) {
			similarity = calculateSimiliraty(item, it, properties);
			if(similarity >= treshold)
				map.put(it, similarity);
		}
		return SortBy.sortByValue(map);
	}

	public List<T> getMostSimilarItemsAsList(T item, Collection<T> items, List<Property> properties) {
		List<Entry<T, Double>> mostSimilarItems = new LinkedList<>(getMostSimilarItems(item, items, properties));
		return toList(mostSimilarItems);
	}

	private List<T> toList(List<Entry<T, Double>> mostSimilarItems) {
		List<T> inters = new ArrayList<T>();
		for(Entry<T, Double> entry : mostSimilarItems) {
			inters.add(entry.getKey());
		}
		return inters;
	}

	@Override
	public List<T> getMostSimilarItems(T item, Collection<T> items, Collection<Property> properties, int numberOfItems) {
		List<Entry<T, Double>> mostSimilarItems = new LinkedList<>(getMostSimilarItems(item, items, properties));
		List<T> inters = new ArrayList<T>();
		if(mostSimilarItems.size() < numberOfItems) {
			numberOfItems = mostSimilarItems.size();
		}
		for(Entry<T, Double> entry : mostSimilarItems.subList(0, numberOfItems)) {
			inters.add(entry.getKey());
		}
		return inters;
	}

	@Override
	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<T> items, Property property) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(property);
		return getMostSimilarItems(item, items, properties);
	}

	@Override
	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<T> items, Property property, double treshold) {
		List<Property> properties = new ArrayList<>();
		properties.add(property);
		return getMostSimilarItems(item, items, properties, treshold);
	}

	public List<Entry<T, Double>> getMostSimilarItems(T item, Property property) {
		Collection<Property> properties = new ArrayList<Property>();
		properties.add(property);
		return getMostSimilarItems(item, properties);
	}

	public List<Entry<T, Double>> getMostSimilarItems(T item, Property property, double treshold) {
		Collection<Property> properties = new ArrayList<Property>();
		properties.add(property);
		return getMostSimilarItems(item, properties, treshold);
	}

	public List<T> getRecommendedEdition(T item, Collection<Property> properties) {
		return getRecommendedEdition(item, getDefaultSet(), properties);
	}

	@Override
	public List<T> getRecommendedEdition(T item, Collection<T> ts, Collection<Property> properties) {
		List<T> items = new ArrayList<>(ts);
		List<T> resultSet = new ArrayList<T>();
		T current = item;
		resultSet.add(item);
		items.remove(item);
		while(!items.isEmpty()) {
			// System.out.println(resultSet.size());
			current = getMostSimilarItem(item, items, properties);
			items.remove(current);
			resultSet.add(current);
		}
		return resultSet;
	}

	@Override
	public List<T> getRecommendedEdition(T t, Collection<T> ts, Property property) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(property);
		return getRecommendedEdition(t, ts, properties);
	}

	public List<T> getRecommendedEdition(T t, Property property) {
		Collection<Property> properties = new ArrayList<Property>();
		properties.add(property);
		return getRecommendedEdition(t, properties);
	}

	public List<T> getRecommendedEditionWithRandomStart(Collection<Property> properties) {
		List<T> items = new ArrayList<T>(getDefaultSet());
		return getRecommendedEdition(items.get(RANDOM.nextInt(items.size())), properties);
	}

	public List<T> getRecommendedEditionWithRandomStart(Property property) {
		ArrayList<Property> properties = new ArrayList<>();
		properties.add(property);
		return getRecommendedEditionWithRandomStart(properties);
	}

	protected abstract void setFragmentsGroup(T item1, T item2, Property property);

}
