package pt.ist.socialsoftware.edition.search.api.dto;

import java.util.List;
import java.util.Map;

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
    private final Map<String, Map<SearchableElementDto, List<String>>> results;

    public AdvancedSearchResultDto(boolean showEdition, boolean showHeteronym, boolean showDate, boolean showLdoD,
                                   boolean showSource, boolean showSourceType, boolean showTaxonomy, int fragCount,
                                   int interCount, int fragCountNotAdded,
                                   int interCountNotAdded, Map<String, Map<SearchableElementDto, List<String>>> results) {
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

    public Map<String, Map<SearchableElementDto, List<String>>> getResults() {
        return this.results;
    }

}
