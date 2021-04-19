package pt.ist.socialsoftware.edition.ldod.frontend.reading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;
import pt.ist.socialsoftware.edition.virtual.api.textdto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textdto.ScholarInterDto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class ReadingRecommendation implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ReadingRecommendation.class);

    private final FeReadingRequiresInterface FEReadingRequiresInterface = new FeReadingRequiresInterface();

    private final List<String> read = new ArrayList<>();
    private float heteronymWeight = 0;
    private float dateWeight = 0;
    private float textWeight = 1;
    private float taxonomyWeight = 0;

    public ReadingRecommendation() {
    }

    public void clean() {
        this.read.clear();
        this.heteronymWeight = 0;
        this.dateWeight = 0;
        this.textWeight = 0;
        this.taxonomyWeight = 0;
    }

    public Set<ScholarInterDto> getNextRecommendations(String expertEditionInterId) {
        // logger.debug("getNextRecommendations textWeight:{}, read size:{},
        // read:{}", this.textWeight, this.read.size(),
        // this.read);

        List<FragmentDto> readFragments = this.read.stream()
                .map(id -> this.FEReadingRequiresInterface.getScholarInterbyExternalId(id)).map(ScholarInterDto::getFragmentDto)
                .collect(Collectors.toList());

        ScholarInterDto toReadInter = this.FEReadingRequiresInterface.getScholarInterbyExternalId(expertEditionInterId);
        FragmentDto toReadFragment = toReadInter.getFragmentDto();

        // if the fragment that is going to be read was already read, return to
        // that position of recommendation
        int index = readFragments.indexOf(toReadFragment);
        if (index != -1) {
            readFragments.subList(index, readFragments.size()).clear();
            this.read.subList(index, this.read.size()).clear();
        }


        Set<FragmentDto> fragments = this.FEReadingRequiresInterface.getFragmentDtoSet();

        // if all fragments minus 50 were already suggested clear the first 50
        // recommendations
        if (readFragments.size() == fragments.size() - 50) {
            readFragments.subList(0, 50).clear();
            this.read.subList(0, 50).clear();
        }

        Set<FragmentDto> toBeRecommended = fragments.stream()
                .filter(f -> !readFragments.contains(f)).collect(Collectors.toSet());

        this.read.add(expertEditionInterId);

        List<Entry<FragmentDto, Double>> mostSimilars = this.FEReadingRequiresInterface.
                getMostSimilarFragmentsOfGivenFragment(toReadFragment, toBeRecommended,
                        new WeightsDto(getHeteronymWeight(), getDateWeight(), getTextWeight(), getTaxonomyWeight()));

        Set<ScholarInterDto> result = new HashSet<>();
        Double value = mostSimilars.get(0).getValue();
        for (Entry<FragmentDto, Double> entry : mostSimilars) {
            // logger.debug("ReadingRecommendation value1:{}, value2:{}", value,
            // entry.getValue());
            // add all interpretations that are similar
            if (Math.abs(value - entry.getValue()) < 0.001 && result.size() < 5) {
                result.addAll(entry.getKey().getScholarInterDtoSetForExpertEdtion(toReadInter.getExpertEditionAcronym()));
                // if the most similar fragment does not have an interpretation
                // in this edition, use the next most similar fragment
            } else if (result.size() == 0) {
                value = entry.getValue();
                result.addAll(entry.getKey().getScholarInterDtoSetForExpertEdtion(toReadInter.getExpertEditionAcronym()));
            } else {
                break;
            }
        }

        return result;
    }

    public String prevRecommendation() {
        ScholarInterDto result = getPrevRecommendation();
        if (result == null) {
            return null;
        }
        this.read.remove(this.read.size() - 1);
        this.read.remove(this.read.size() - 1);
        return result.getExternalId();
    }

    public ScholarInterDto getPrevRecommendation() {
        if (this.read.size() < 2) {
            return null;
        }
        return this.FEReadingRequiresInterface.getScholarInterbyExternalId(this.read.get(this.read.size() - 2));
    }

    public void resetPrevRecommendations() {
        if (this.read.size() > 1) {
            this.read.subList(0, this.read.size() - 1).clear();
        }
    }

    public String getCurrentInterpretation() {
        return this.read.get(this.read.size() - 1);
    }

    public float getHeteronymWeight() {
        return this.heteronymWeight;
    }

    public void setHeteronymWeight(float heteronymWeight) {
        this.heteronymWeight = heteronymWeight;
    }

    public float getDateWeight() {
        return this.dateWeight;
    }

    public void setDateWeight(float dateWeight) {
        this.dateWeight = dateWeight;
    }

    public float getTextWeight() {
        return this.textWeight;
    }

    public void setTextWeight(float textWeight) {
        this.textWeight = textWeight;
    }

    public float getTaxonomyWeight() {
        return this.taxonomyWeight;
    }

    public void setTaxonomyWeight(float taxonomyWeight) {
        this.taxonomyWeight = taxonomyWeight;
    }

}
