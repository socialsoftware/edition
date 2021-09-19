package pt.ist.socialsoftware.edition.recommendation.feature.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationWeights;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextProperty extends Property {
    private static final Logger logger = LoggerFactory.getLogger(TextProperty.class);

    public static final int NUMBER_OF_TERMS = 100;
    public static final int NUMBER_OF_TERMS_TO_SHOW = 3;

    private static final Map<String, Map<String, double[]>> vectorsCache = new ConcurrentHashMap<>();
    private static final Map<String, FragmentDto> fragmentsCache = new HashMap<>();
    private static final Map<String, List<String>> commonTermsTFIDFCache = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Double>> TFIDFCache = new ConcurrentHashMap<>();


    private List<String> commonTerms;

    private FragmentDto fragment1;
    private FragmentDto fragment2;

    public TextProperty(double weigth) {
        super(weigth, PropertyCache.OFF);
    }

    @Override
    public void prepareToLoadProperty(ScholarInterDto inter1, ScholarInterDto inter2) {
        prepareToLoadProperty(getFragmentFromCache(inter1), getFragmentFromCache(inter2));
    }

    @Override
    public void prepareToLoadProperty(VirtualEditionInterDto inter1, VirtualEditionInterDto inter2) {
        prepareToLoadProperty(inter1.getLastUsed(), inter2.getLastUsed());
    }

    @Override
    public void prepareToLoadProperty(FragmentDto fragment1, FragmentDto fragment2) {
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
    protected double[] extractVector(ScholarInterDto scholarInter) {
        return extractVector(getFragmentFromCache(scholarInter));
        //        return extractVector(scholarInter.getFragmentDto());
    }

    @Override
    protected double[] extractVector(VirtualEditionInterDto virtualEditionInter) {
        return extractVector(virtualEditionInter.getLastUsed());
    }

    @Override
    protected double[] extractVector(FragmentDto fragment) {
        return getFromVectorsCache(fragment);
    }

    private double[] getFromVectorsCache(FragmentDto fragment) {
        FragmentDto fragmentOther = fragment.getXmlId().equals(this.fragment1.getXmlId()) ? this.fragment2 : this.fragment1;
        Map<String, double[]> map = vectorsCache.get(fragment.getXmlId());
        if (map == null) {
            return null;
        }
        double[] tmp = map.get(fragmentOther.getXmlId());
        return tmp;
    }

    private void putIntoVectorsCache(FragmentDto fragment, double[] vector) {
        FragmentDto fragmentOther = fragment.getXmlId().equals(this.fragment1.getXmlId()) ? this.fragment2 : this.fragment1;
        Map<String, double[]> map = vectorsCache.get(fragment.getXmlId());
        if (map == null) {
            map = new ConcurrentHashMap<>();
            vectorsCache.put(fragment.getXmlId(), map);
        }
        map.put(fragmentOther.getXmlId(), vector);
    }

    private double[] generateFragmentVector(FragmentDto fragment) {
        this.commonTerms = getFragmentsCommonTerms(this.fragment1, this.fragment2);

//        Map<String, Double> tfidf = this.recommendationRequiresInterface.getFragmentTFIDF(fragment.getXmlId(), this.commonTerms);
        Map<String, Double> tfidf = getFragmentTFIDF(fragment, this.commonTerms);

        return buildVector(tfidf);
    }

    private Map<String, Double> getFragmentTFIDF(FragmentDto fragment, List<String> commonTerms) {
        String id = fragment.getExternalId();
        if (TFIDFCache.containsKey(id)) {
            Map<String, Double> TFIDFMap = new HashMap<>(TFIDFCache.get(id));
            TFIDFMap.keySet().retainAll(commonTerms);
            return TFIDFMap;
        }
        Map<String, Double> tfidf = this.recommendationRequiresInterface.getFragmentTFIDF(fragment.getXmlId(), commonTerms);
        TFIDFCache.put(id, tfidf);
        return tfidf;
    }

    private List<String> getFragmentsCommonTerms(FragmentDto fragment1, FragmentDto fragment2) {
        List<String> result = new ArrayList<>();
        result.addAll(getCommonTermsFromCache(fragment1.getXmlId()));
        result.addAll(getCommonTermsFromCache(fragment2.getXmlId()));

        return result;
    }

    private FragmentDto getFragmentFromCache(ScholarInterDto scholarInterDto) {
        FragmentDto fragmentDto = fragmentsCache.get(scholarInterDto.getXmlId());
        if (fragmentDto == null) {
            fragmentDto = scholarInterDto.getFragmentDto();
            fragmentsCache.put(scholarInterDto.getXmlId(), fragmentDto);
        }
        return fragmentDto;
    }

    private List<String> getCommonTermsFromCache(String xmlId) {
        List<String> commonTerms = commonTermsTFIDFCache.get(xmlId);
        if (commonTerms == null) {
            commonTerms = this.recommendationRequiresInterface.getFragmentTFIDF(xmlId, NUMBER_OF_TERMS);
            commonTermsTFIDFCache.put(xmlId, commonTerms);
        }
        return commonTerms;
    }

    @Override
    protected double[] getDefaultVector() {
        return new double[this.commonTerms.size()];
    }

    @Override
    public void userWeight(RecommendationWeights recommendationWeights) {
        recommendationWeights.setTextWeight(getWeight());
    }

}