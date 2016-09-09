package pt.ist.socialsoftware.edition.recommendation.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private List<String> commonTerms;

	public TextProperty(double weigth) {
		super(weigth);
	}

	public TextProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	@Override
	public void prepareToLoadProperty(FragInter inter1, FragInter inter2) {
		try {
			Indexer indexer = new Indexer();
			Set<String> temp = new HashSet<String>();
			temp.addAll(indexer.getTerms(inter1, NUMBER_OF_TERMS));
			temp.addAll(indexer.getTerms(inter2, NUMBER_OF_TERMS));
			commonTerms = new ArrayList<String>(temp);
		} catch (ParseException | IOException e) {
			throw new LdoDException("prepareToLoadProperty in class TextProperty failed when invoking indexer");
		}
	}

	@Override
	public void prepareToLoadProperty(Fragment frag1, Fragment frag2) {
		try {
			Indexer indexer = new Indexer();
			Set<String> temp = new HashSet<String>();
			temp.addAll(indexer.getTerms(frag1, NUMBER_OF_TERMS));
			temp.addAll(indexer.getTerms(frag2, NUMBER_OF_TERMS));
			commonTerms = new ArrayList<String>(temp);
		} catch (ParseException | IOException e) {
			throw new LdoDException("prepareToLoadProperty in class TextProperty failed when invoking indexer");
		}
	}

	private List<Double> buildVector(Map<String, Double> tfidf) {
		List<Double> vector = getDefaultVector();
		for (int i = 0; i < commonTerms.size(); i++) {
			String term = commonTerms.get(i);
			if (tfidf.containsKey(term)) {
				vector.set(i, getWeight() * tfidf.get(term));
			}
		}
		return vector;
	}

	@Override
	protected List<Double> extractVector(ExpertEditionInter expertEditionInter) {
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(expertEditionInter, commonTerms);
		} catch (IOException | ParseException e) {
			throw new LdoDException("Indexer error when extractVector in TextProperty");
		}
		List<Double> vector = buildVector(tfidf);
		return vector;
	}

	@Override
	protected List<Double> extractVector(SourceInter sourceInter) {
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(sourceInter, commonTerms);
		} catch (IOException | ParseException e) {
			throw new LdoDException("Indexer error when extractVector in TextProperty");
		}
		List<Double> vector = buildVector(tfidf);
		return vector;
	}

	@Override
	public List<Double> extractVector(Fragment fragment) {
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(fragment, commonTerms);
		} catch (IOException | ParseException e) {
			throw new LdoDException("Indexer error when extractVector in TextProperty");
		}
		List<Double> vector = buildVector(tfidf);
		return vector;
	}

	@Override
	protected List<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(commonTerms.size(), 0.0));
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