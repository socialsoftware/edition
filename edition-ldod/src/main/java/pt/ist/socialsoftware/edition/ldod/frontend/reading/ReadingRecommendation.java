package pt.ist.socialsoftware.edition.ldod.frontend.reading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.*;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;

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

    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private final List<String> read = new ArrayList<>();
    private double heteronymWeight = 0.0;
    private double dateWeight = 0.0;
    private double textWeight = 1.0;
    private double taxonomyWeight = 0.0;

    public ReadingRecommendation() {
    }

    public void clean() {
        this.read.clear();
        this.heteronymWeight = 0.0;
        this.dateWeight = 0.0;
        this.textWeight = 0.0;
        this.taxonomyWeight = 0.0;
    }

    private List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();
        if (this.heteronymWeight > 0.0) {
            properties.add(new HeteronymProperty(this.heteronymWeight));
        }
        if (this.dateWeight > 0.0) {
            properties.add(new DateProperty(this.dateWeight));
        }
        if (this.textWeight > 0.0) {
            properties.add(new TextProperty(this.textWeight));
        }
        if (this.taxonomyWeight > 0.0) {
            properties.add(new TaxonomyProperty(this.taxonomyWeight, VirtualEdition.ARCHIVE_EDITION_ACRONYM,
                    Property.PropertyCache.ON));
        }
        return properties;
    }

    public Set<ScholarInterDto> getNextRecommendations(String expertEditionInterId) {
        // logger.debug("getNextRecommendations textWeight:{}, read size:{},
        // read:{}", this.textWeight, this.read.size(),
        // this.read);

        List<FragmentDto> readFragments = this.read.stream()
                .map(id -> this.textProvidesInterface.getScholarInterbyExternalId(id)).map(ScholarInterDto::getFragmentDto)
                .collect(Collectors.toList());

        ScholarInterDto toReadInter = this.textProvidesInterface.getScholarInterbyExternalId(expertEditionInterId);
        FragmentDto toReadFragment = toReadInter.getFragmentDto();

        // if the fragment that is going to be read was already read, return to
        // that position of recommendation
        int index = readFragments.indexOf(toReadFragment);
        if (index != -1) {
            readFragments.subList(index, readFragments.size()).clear();
            this.read.subList(index, this.read.size()).clear();
        }


        Set<FragmentDto> fragments = this.textProvidesInterface.getFragmentDtoSet();

        // if all fragments minus 50 were already suggested clear the first 50
        // recommendations
        if (readFragments.size() == fragments.size() - 50) {
            readFragments.subList(0, 50).clear();
            this.read.subList(0, 50).clear();
        }

        Set<FragmentDto> toBeRecommended = fragments.stream()
                .filter(f -> !readFragments.contains(f)).collect(Collectors.toSet());

        this.read.add(expertEditionInterId);

        List<Entry<FragmentDto, Double>> mostSimilars = getEntries(toReadFragment, toBeRecommended);

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

    private List<Entry<FragmentDto, Double>> getEntries(FragmentDto toReadFragment, Set<FragmentDto> toBeRecommended) {
        VSMFragmentRecommender recommender = new VSMFragmentRecommender();
        List<Property> properties = getProperties();
        return recommender.getMostSimilarItems(toReadFragment, toBeRecommended,
                properties);
    }

    public String prevRecommendation() {
        ExpertEditionInter result = getPrevRecommendation();
        if (result == null) {
            return null;
        }
        this.read.remove(this.read.size() - 1);
        this.read.remove(this.read.size() - 1);
        return result.getExternalId();
    }

    public ExpertEditionInter getPrevRecommendation() {
        if (this.read.size() < 2) {
            return null;
        }
        return FenixFramework.getDomainObject(this.read.get(this.read.size() - 2));
    }

    public void resetPrevRecommendations() {
        if (this.read.size() > 1) {
            this.read.subList(0, this.read.size() - 1).clear();
        }
    }

    public String getCurrentInterpretation() {
        return this.read.get(this.read.size() - 1);
    }

    public double getHeteronymWeight() {
        return this.heteronymWeight;
    }

    public void setHeteronymWeight(double heteronymWeight) {
        this.heteronymWeight = heteronymWeight;
    }

    public double getDateWeight() {
        return this.dateWeight;
    }

    public void setDateWeight(double dateWeight) {
        this.dateWeight = dateWeight;
    }

    public double getTextWeight() {
        return this.textWeight;

    }

    public void setTextWeight(double textWeight) {
        this.textWeight = textWeight;
    }

    public double getTaxonomyWeight() {
        return this.taxonomyWeight;
    }

    public void setTaxonomyWeight(double taxonomyWeight) {
        this.taxonomyWeight = taxonomyWeight;
    }

}
