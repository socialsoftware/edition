package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

public class SimpleSearchDto {


    private String searchTerm;
    private String searchType;
    private String searchSource;

    public SimpleSearchDto() {
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchSource() {
        return searchSource;
    }

    public void setSearchSource(String searchSource) {
        this.searchSource = searchSource;
    }
}
