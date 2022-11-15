package pt.ist.socialsoftware.edition.ldod.bff.reading.dto;

import java.util.List;

public class ReadingExpertEditionDto {
    private List<ExpertEditionDto> editions;

    private List<RecommendedInterDto> recommendedInters;

    public ReadingExpertEditionDto(List<ExpertEditionDto> editions, List<RecommendedInterDto> recommendedInters) {
        setEditions(editions);
        setRecommendedInters(recommendedInters);
    }

    public List<RecommendedInterDto> getRecommendedInters() {
        return recommendedInters;
    }

    public void setRecommendedInters(List<RecommendedInterDto> recommendedInters) {
        this.recommendedInters = recommendedInters;
    }

    public List<ExpertEditionDto> getEditions() {
        return editions;
    }

    public void setEditions(List<ExpertEditionDto> editions) {
        this.editions = editions;
    }
}
