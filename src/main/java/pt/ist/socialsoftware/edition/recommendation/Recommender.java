package pt.ist.socialsoftware.edition.recommendation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Recommender<T, P> {

	double calculateSimilarity(T t1, T t2, Collection<P> ps);

	double calculateSimiliraty(T t1, T t2, P p);

	T getMostSimilarItem(T t, Collection<T> ts, Collection<P> ps);

	T getMostSimilarItem(T t, Collection<T> ts, P p);

	List<T> getMostSimilarItems(T t, Collection<T> ts, Collection<P> ps, int numberOfItems);

	List<Map.Entry<T, Double>> getMostSimilarItems(T t, Collection<T> ts, Collection<P> ps);

	List<Map.Entry<T, Double>> getMostSimilarItems(T t, Collection<T> ts, Collection<P> ps, double treshold);

	List<Map.Entry<T, Double>> getMostSimilarItems(T t, Collection<T> ts, P p);

	List<Map.Entry<T, Double>> getMostSimilarItems(T t, Collection<T> ts, P p, double treshold);

	List<T> getRecommendedEdition(T t, Collection<T> ts, Collection<P> ps);

	List<T> getRecommendedEdition(T t, Collection<T> ts, P p);

}