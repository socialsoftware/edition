package pt.ist.socialsoftware.edition.recommendation.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.search.Indexer;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class TextProperty extends Property {
	private static Logger logger = LoggerFactory.getLogger(TextProperty.class);

	public static final int NUMBER_OF_TERMS = 100;

	private static Map<String, Map<String, double[]>> vectorsCache = new HashMap<String, Map<String, double[]>>();

	private List<String> commonTerms;

	private Fragment fragment1;
	private Fragment fragment2;

	public TextProperty(double weigth) {
		super(weigth);
	}

	public TextProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	@Override
	public void prepareToLoadProperty(VirtualEditionInter inter1, VirtualEditionInter inter2) {
		prepareToLoadProperty(inter1.getFragment(), inter2.getFragment());
	}

	@Override
	public void prepareToLoadProperty(Fragment fragment1, Fragment fragment2) {
		this.fragment1 = fragment1;
		this.fragment2 = fragment2;
		double[] vector = getFromVectorsCache(fragment1);
		if (vector == null) {
			vector = generateFragmentVector(fragment1);
			putIntoVectorsCache(fragment1, vector);
		}
		vector = getFromVectorsCache(fragment2);
		if (vector == null) {
			vector = generateFragmentVector(fragment2);
			putIntoVectorsCache(fragment2, vector);
		}
	}

	private double[] buildVector(Map<String, Double> tfidf) {
		double[] vector = new double[commonTerms.size()];
		for (int i = 0; i < vector.length; i++) {
			String term = commonTerms.get(i);
			if (tfidf.containsKey(term)) {
				vector[i] = tfidf.get(term);
			}
		}
		return vector;
	}

	private double[] applyWeight(double[] vector) {
		double result[] = new double[vector.length];
		for (int i = 0; i < vector.length; i++) {
			result[i] = vector[i] * getWeight();
		}
		return result;
	}

	@Override
	protected double[] extractVector(VirtualEditionInter virtualEditionInter) {
		return applyWeight(getFromVectorsCache(virtualEditionInter.getFragment()));
	}

	@Override
	protected double[] extractVector(Fragment fragment) {
		return applyWeight(getFromVectorsCache(fragment));
	}

	private double[] getFromVectorsCache(Fragment fragment) {
		Fragment fragmentOther = fragment == fragment1 ? fragment2 : fragment1;
		Map<String, double[]> map = vectorsCache.get(fragment.getExternalId());
		if (map == null) {
			return null;
		}
		double[] tmp = map.get(fragmentOther.getExternalId());
		return tmp;
	}

	private void putIntoVectorsCache(Fragment fragment, double[] vector) {
		Fragment fragmentOther = fragment == fragment1 ? fragment2 : fragment1;
		Map<String, double[]> map = vectorsCache.get(fragment.getExternalId());
		if (map == null) {
			map = new HashMap<String, double[]>();
			vectorsCache.put(fragment.getExternalId(), map);
		}
		map.put(fragmentOther.getExternalId(), vector);
	}

	private double[] generateFragmentVector(Fragment fragment) {
		double[] vector;
		Map<String, Double> tfidf;
		try {
			commonTerms = getFragmentsCommonTerms(this.fragment1, this.fragment2);
			tfidf = Indexer.getIndexer().getTFIDF(fragment, commonTerms);
		} catch (IOException | ParseException e) {
			throw new LdoDException("Indexer error when extractVector in TextProperty");
		}
		vector = buildVector(tfidf);
		return vector;
	}

	private List<String> getFragmentsCommonTerms(Fragment fragment1, Fragment fragment2) {
		Indexer indexer = Indexer.getIndexer();
		List<String> result = new ArrayList<String>();
		try {
			result.addAll(indexer.getTFIDFTerms(fragment1, NUMBER_OF_TERMS));
			result.addAll(indexer.getTFIDFTerms(fragment2, NUMBER_OF_TERMS));
		} catch (ParseException | IOException e) {
			throw new LdoDException("prepareToLoadProperty in class TextProperty failed when invoking indexer");
		}
		return result;
	}

	@Override
	protected double[] getDefaultVector() {
		return new double[commonTerms.size()];
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setTextWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Text";
	}

}