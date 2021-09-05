package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvancedSearchResultDto {
    private final boolean showEdition;
    private final boolean showHeteronym;
    private final boolean showDate;
    private final boolean showLdoD;
    private final boolean showSource;
    private final boolean showSourceType;
    private final boolean showTaxonomy;
    private final int fragCount;
    private final int interCount;
    private final int fragCountNotAdded;
    private final int interCountNotAdded;
    private Map<String, Map<String, List<String>>> results;

//    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AdvancedSearchResultDto(@JsonProperty("showEdition") boolean showEdition, @JsonProperty("showHeteronym") boolean showHeteronym, @JsonProperty("showDate") boolean showDate, @JsonProperty("showLdoD") boolean showLdoD,
                                   @JsonProperty("showSource") boolean showSource, @JsonProperty("showSourceType") boolean showSourceType, @JsonProperty("showTaxonomy") boolean showTaxonomy, @JsonProperty("fragCount") int fragCount,
                                   @JsonProperty("interCount") int interCount, @JsonProperty("fragCountNotAdded") int fragCountNotAdded,
                                   @JsonProperty("interCountNotAdded") int interCountNotAdded, @JsonProperty("results") Map<String, Map<String,  List<String>>> results) {
        this.showEdition = showEdition;
        this.showHeteronym = showHeteronym;
        this.showDate = showDate;
        this.showLdoD = showLdoD;
        this.showSource = showSource;
        this.showSourceType = showSourceType;
        this.showTaxonomy = showTaxonomy;
        this.fragCount = fragCount;
        this.interCount = interCount;
        this.fragCountNotAdded = fragCountNotAdded;
        this.interCountNotAdded = interCountNotAdded;
        this.results = results;
    }



    public boolean isShowEdition() {
        return this.showEdition;
    }

    public boolean isShowHeteronym() {
        return this.showHeteronym;
    }

    public boolean isShowDate() {
        return this.showDate;
    }

    public boolean isShowLdoD() {
        return this.showLdoD;
    }

    public boolean isShowSource() {
        return this.showSource;
    }

    public boolean isShowSourceType() {
        return this.showSourceType;
    }

    public boolean isShowTaxonomy() {
        return this.showTaxonomy;
    }

    public int getFragCount() {
        return this.fragCount;
    }

    public int getInterCount() {
        return this.interCount;
    }

    public int getFragCountNotAdded() {
        return this.fragCountNotAdded;
    }

    public int getInterCountNotAdded() {
        return this.interCountNotAdded;
    }

    public Map<String, Map<String, List<String>>> getResults() {
        return this.results;
    }

    public Map<String, Map<SearchableElementDto, List<String>>> convert() {
        ObjectReader objectMapper = new ObjectMapper().readerFor(SearchableElementDto.class);

        Map<String, Map<SearchableElementDto, List<String>>> test = results.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, ev -> ev.getValue().entrySet().stream()
                .collect(Collectors.toMap(stringListEntry -> {
                    try {
                        return objectMapper.readValue(stringListEntry.getKey());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }, ev2 -> ev2.getValue()))));
        return test;
    }

}
