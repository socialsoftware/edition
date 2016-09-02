package pt.ist.socialsoftware.edition.recommendation.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.queryparser.classic.ParseException;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.search.Indexer;

public class TextProperty extends Property {
	public static final int NUMBEROFTERMS = 100;
	private List<String> commonTerms;
	private final int numberOfTerms;

	public TextProperty() {
		super();
		numberOfTerms = NUMBEROFTERMS;
	}

	public TextProperty(double weigth) {
		super(weigth);
		numberOfTerms = NUMBEROFTERMS;
	}

	public TextProperty(Double weight, int numberOfTerms) {
		super(weight);
		this.numberOfTerms = numberOfTerms;
	}

	public TextProperty(int numberOfTerms) {
		super();
		this.numberOfTerms = numberOfTerms;
	}

	@Override
	protected Collection<Double> extractVector(ExpertEditionInter expertEditionInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		String term;
		int size;
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(expertEditionInter, commonTerms);
			size = commonTerms.size();
			for (int i = 0; i < size; i++) {
				term = commonTerms.get(i);
				if (tfidf.containsKey(term)) {
					vector.set(i, getWeight() * tfidf.get(term));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		String term;
		int size;
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(fragment, commonTerms);
			size = commonTerms.size();
			for (int i = 0; i < size; i++) {
				term = commonTerms.get(i);
				if (tfidf.containsKey(term)) {
					vector.set(i, getWeight() * tfidf.get(term));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(Source source) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		String term;
		int size;
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(source, commonTerms);
			size = commonTerms.size();
			for (int i = 0; i < size; i++) {
				term = commonTerms.get(i);
				if (tfidf.containsKey(term)) {
					vector.set(i, getWeight() * tfidf.get(term));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		List<Double> vector = new ArrayList<Double>(getDefaultVector());
		String term;
		int size;
		Map<String, Double> tfidf;
		try {
			tfidf = (new Indexer()).getTFIDF(sourceInter, commonTerms);
			size = commonTerms.size();
			for (int i = 0; i < size; i++) {
				term = commonTerms.get(i);
				if (tfidf.containsKey(term)) {
					vector.set(i, getWeight() * tfidf.get(term));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		return new ArrayList<Double>(Collections.nCopies(commonTerms.size(), 0.0));
	}

	@Override
	public void loadProperty(FragInter inter1, FragInter inter2) {
		try {
			Indexer indexer = new Indexer();
			Set<String> temp = new HashSet<String>();
			temp.addAll(indexer.getTerms(inter1, numberOfTerms));
			temp.addAll(indexer.getTerms(inter2, numberOfTerms));
			commonTerms = new ArrayList<String>(temp);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFragmentsGroup(Fragment frag1, Fragment frag2) {
		try {
			Indexer indexer = new Indexer();
			Set<String> temp = new HashSet<String>();
			temp.addAll(indexer.getTerms(frag1, numberOfTerms));
			temp.addAll(indexer.getTerms(frag2, numberOfTerms));
			commonTerms = new ArrayList<String>(temp);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
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