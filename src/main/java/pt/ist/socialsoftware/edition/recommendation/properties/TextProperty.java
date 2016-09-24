package pt.ist.socialsoftware.edition.recommendation.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.search.Indexer;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class TextProperty extends Property {
	public static final int NUMBER_OF_TERMS = 100;

	private static Map<String, Map<String, double[]>> vectors = new HashMap<String, Map<String, double[]>>();

	private List<String> commonTerms;

	private FragInter fragInter1;
	private FragInter fragInter2;
	private Fragment fragment1;
	private Fragment fragment2;

	public TextProperty(double weigth) {
		super(weigth);
	}

	public TextProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	@Override
	public void prepareToLoadProperty(FragInter inter1, FragInter inter2) {
		this.fragInter1 = inter1.getLastUsed();
		this.fragInter2 = inter2.getLastUsed();
		double[] vector = getFromVectors(this.fragInter1);
		if (vector == null) {
			vector = getFragInterVector(this.fragInter1);
			putIntoVectors(this.fragInter1, vector);
		}
		vector = getFromVectors(this.fragInter2);
		if (vector == null) {
			vector = getFragInterVector(this.fragInter2);
			putIntoVectors(this.fragInter2, vector);
		}
	}

	@Override
	public void prepareToLoadProperty(Fragment frag1, Fragment frag2) {
		this.fragment1 = frag1;
		this.fragment2 = frag2;
		double[] vector = getFromVector(fragment1);
		if (vector == null) {
			vector = getFragmentVector(fragment1);
			putIntoVectors(fragment1, vector);
		}
		vector = getFromVector(fragment2);
		if (vector == null) {
			vector = getFragmentVector(fragment2);
			putIntoVectors(fragment2, vector);
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
			result[i] = vector[i] + getWeight();
		}
		return result;

	}

	@Override
	protected double[] extractVector(ExpertEditionInter expertEditionInter) {
		return applyWeight(getFromVectors(expertEditionInter));
	}

	@Override
	protected double[] extractVector(SourceInter sourceInter) {
		return applyWeight(getFromVectors(sourceInter));
	}

	@Override
	protected double[] extractVector(Fragment fragment) {
		return applyWeight(getFromVector(fragment));
	}

	private double[] getFromVector(Fragment fragment) {
		Fragment fragmentOther = fragment1 == fragment ? fragment2 : fragment1;
		Map<String, double[]> map = vectors.get(fragment.getExternalId());
		if (map == null) {
			return null;
		}
		return map.get(fragmentOther.getExternalId());
	}

	private double[] getFromVectors(FragInter fragInter) {
		FragInter fragInterOther = fragInter1 == fragInter ? fragInter2 : fragInter1;
		Map<String, double[]> map = vectors.get(fragInter.getExternalId());
		if (map == null) {
			return null;
		}
		return map.get(fragInterOther.getExternalId());
	}

	private void putIntoVectors(FragInter fragInter, double[] vector) {
		FragInter fragInterOther = fragInter1 == fragInter ? fragInter2 : fragInter1;
		Map<String, double[]> map = vectors.get(fragInter.getExternalId());
		if (map == null) {
			map = new HashMap<String, double[]>();
			vectors.put(fragInter.getExternalId(), map);
		}
		map.put(fragInterOther.getExternalId(), vector);
	}

	private void putIntoVectors(Fragment fragment, double[] vector) {
		Fragment fragmentOther = fragment1 == fragment ? fragment2 : fragment1;
		Map<String, double[]> map = vectors.get(fragment.getExternalId());
		if (map == null) {
			map = new HashMap<String, double[]>();
			vectors.put(fragment.getExternalId(), map);
		}
		map.put(fragmentOther.getExternalId(), vector);
	}

	private double[] getFragmentVector(Fragment fragment) {
		double[] vector;
		Map<String, Double> tfidf;
		try {
			if (commonTerms == null) {
				commonTerms = getFragmentsCommonTerms(this.fragment1, this.fragment2);
			}
			tfidf = Indexer.getIndexer().getTFIDF(fragment, commonTerms);
		} catch (IOException | ParseException e) {
			throw new LdoDException("Indexer error when extractVector in TextProperty");
		}
		vector = buildVector(tfidf);
		return vector;
	}

	private double[] getFragInterVector(FragInter fragInter) {
		double[] vector;
		Map<String, Double> tfidf;
		try {
			if (commonTerms == null) {
				commonTerms = getFragIntersCommonTerms(this.fragInter1, this.fragInter2);
			}
			tfidf = Indexer.getIndexer().getTFIDF(fragInter, commonTerms);
		} catch (IOException | ParseException e) {
			throw new LdoDException("Indexer error when extractVector in TextProperty");
		}
		vector = buildVector(tfidf);
		return vector;
	}

	private List<String> getFragmentsCommonTerms(Fragment frag1, Fragment frag2) {
		Indexer indexer = Indexer.getIndexer();
		List<String> result = new ArrayList<String>();
		try {
			result.addAll(indexer.getTFIDFTerms(frag1, NUMBER_OF_TERMS));
			result.addAll(indexer.getTFIDFTerms(frag2, NUMBER_OF_TERMS));
		} catch (ParseException | IOException e) {
			throw new LdoDException("prepareToLoadProperty in class TextProperty failed when invoking indexer");
		}
		return result;
	}

	private List<String> getFragIntersCommonTerms(FragInter inter1, FragInter inter2) {
		Indexer indexer = Indexer.getIndexer();
		List<String> result = new ArrayList<String>();
		try {
			result.addAll(indexer.getTFIDFTerms(inter1, NUMBER_OF_TERMS));
			result.addAll(indexer.getTFIDFTerms(inter2, NUMBER_OF_TERMS));
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