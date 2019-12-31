package pt.ist.socialsoftware.edition.ldod.recommendation.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TextProperty extends Property {
    private static final Logger logger = LoggerFactory.getLogger(TextProperty.class);

    public static final int NUMBER_OF_TERMS = 100;
    public static final int NUMBER_OF_TERMS_TO_SHOW = 3;

    private static final Map<String, Map<String, double[]>> vectorsCache = new ConcurrentHashMap<>();

    private List<String> commonTerms;

    private Fragment fragment1;
    private Fragment fragment2;

    public TextProperty(double weigth) {
        super(weigth, PropertyCache.OFF);
    }

    public TextProperty(@JsonProperty("weight") String weight) {
        this(Double.parseDouble(weight));
    }

    @Override
    public void prepareToLoadProperty(ExpertEditionInter inter1, ExpertEditionInter inter2) {
        prepareToLoadProperty(inter1.getFragment(), inter2.getFragment());
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
        double[] vector = new double[this.commonTerms.size()];
        for (int i = 0; i < vector.length; i++) {
            String term = this.commonTerms.get(i);
            if (tfidf.containsKey(term)) {
                vector[i] = tfidf.get(term);
            }
        }
        return vector;
    }

    @Override
    protected double[] extractVector(ExpertEditionInter expertEditionInter) {
        return extractVector(expertEditionInter.getFragment());
    }


    @Override
    protected double[] extractVector(VirtualEditionInter virtualEditionInter) {
        return extractVector(virtualEditionInter.getFragment());
    }

    @Override
    protected double[] extractVector(Fragment fragment) {
        return getFromVectorsCache(fragment);
    }

    private double[] getFromVectorsCache(Fragment fragment) {
        Fragment fragmentOther = fragment == this.fragment1 ? this.fragment2 : this.fragment1;
        Map<String, double[]> map = vectorsCache.get(fragment.getExternalId());
        if (map == null) {
            return null;
        }
        double[] tmp = map.get(fragmentOther.getExternalId());
        return tmp;
    }

    private void putIntoVectorsCache(Fragment fragment, double[] vector) {
        Fragment fragmentOther = fragment == this.fragment1 ? this.fragment2 : this.fragment1;
        Map<String, double[]> map = vectorsCache.get(fragment.getExternalId());
        if (map == null) {
            map = new ConcurrentHashMap<>();
            vectorsCache.put(fragment.getExternalId(), map);
        }
        map.put(fragmentOther.getExternalId(), vector);
    }

    private double[] generateFragmentVector(Fragment fragment) {
        double[] vector;
        Map<String, Double> tfidf;
        try {
            this.commonTerms = getFragmentsCommonTerms(this.fragment1, this.fragment2);
            tfidf = Indexer.getIndexer().getTFIDF(fragment, this.commonTerms);
        } catch (IOException | ParseException e) {
            throw new LdoDException("Indexer error when extractVector in TextProperty");
        }
        vector = buildVector(tfidf);
        return vector;
    }

    private List<String> getFragmentsCommonTerms(Fragment fragment1, Fragment fragment2) {
        Indexer indexer = Indexer.getIndexer();
        List<String> result = new ArrayList<>();
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
        return new double[this.commonTerms.size()];
    }

    @Override
    public void userWeight(RecommendationWeights recommendationWeights) {
        recommendationWeights.setTextWeight(getWeight());
    }

    @Override
    public String getTitle() {
        return "Text";
    }

    @Override
    public String getConcreteTitle(FragInter inter) {
        Indexer indexer = Indexer.getIndexer();
        try {
            return indexer.getTFIDFTerms(inter.getFragment(), NUMBER_OF_TERMS_TO_SHOW).stream().sorted()
                    .collect(Collectors.joining(","));
        } catch (IOException | ParseException e) {
            throw new LdoDException("prepareToLoadProperty in class TextProperty failed when invoking indexer");

        }
    }

}