package pt.ist.socialsoftware.edition.ldod.bff.search.dto;

public class AdvancedTextDto {

    private String keywords;

    public AdvancedTextDto() {
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "AdvancedTextDto{" +
                "keywords='" + keywords + '\'' +
                '}';
    }
}
