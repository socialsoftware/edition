package pt.ist.socialsoftware.edition.recommendation.feature;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Recommender<T, P> {

    double calculateSimilarity(T t1, T t2, Collection<P> ps);

    T getMostSimilarItem(T t, Collection<T> ts, Collection<P> ps);

    List<Map.Entry<T, Double>> getMostSimilarItems(T t, Collection<T> ts, Collection<P> ps);

}