package pt.ist.socialsoftware.edition.recommendation.feature;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public abstract class VSMRecommender<T> implements Recommender<T, Property> {
    private static final Logger logger = LoggerFactory.getLogger(VSMRecommender.class);

    public static final int NUMBER_OF_RECOMMENDATIONS = 5;

    protected static final Random RANDOM = new Random();

    protected abstract void prepareToLoadProperty(T item1, T item2, Property property);

    protected abstract double[] loadProperty(T item1, Property property);

    @Override
    public double calculateSimilarity(T item1, T item2, Collection<Property> ps) {
        double[] vector1 = null;
        double[] vector2 = null;
        for (Property property : ps) {
            prepareToLoadProperty(item1, item2, property);
            vector1 = ArrayUtils.addAll(vector1, loadProperty(item1, property));
            vector2 = ArrayUtils.addAll(vector2, loadProperty(item2, property));
        }
        return Vectors.calculateSimilarity(vector1, vector2);
    }

    @Override
    public T getMostSimilarItem(T item, Collection<T> items, Collection<Property> properties) {
        T result = null;
        double max = Double.NEGATIVE_INFINITY;
        double similarity;
        for (T otherItem : items) {
            if (!otherItem.equals(item)) {
                similarity = calculateSimilarity(item, otherItem, properties);
                if (similarity > max) {
                    result = otherItem;
                    max = similarity;
                }
            }
        }
        return result;
    }

    @Override
    public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<T> items, Collection<Property> properties) {
        double similarity;
        Map<T, Double> map = new HashMap<>();
        for (T otherItem : items) {
            if (!otherItem.equals(item)) {
                similarity = calculateSimilarity(item, otherItem, properties);
                if (similarity >= Double.NEGATIVE_INFINITY) {
                    map.put(otherItem, similarity);
                }
            }
        }

        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toList());
    }

    public List<T> getMostSimilarItemsAsList(T item, Collection<T> items, List<Property> properties) {
        List<T> result = new ArrayList<>();
        T nextItem = item;
        while (!items.isEmpty()) {
            nextItem = getMostSimilarItem(nextItem, items, properties);
            result.add(nextItem);
            items.remove(nextItem);
        }

        // for (Entry<T, Double> entry : getMostSimilarItems(item, items,
        // properties)) {
        // inters.add(entry.getKey());
        // }

        return result;
    }

}
