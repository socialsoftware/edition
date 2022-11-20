package pt.ist.socialsoftware.edition.ldod.bff.reading.dto;

import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;

import java.util.List;

public class ReadingExpertEditionDto {
    private List<ExpertEditionDto> editions;

    private List<String> read;

    private RecommendedInterDto currentInter;
    private RecommendedInterDto prevInter;
    private List<RecommendedInterDto> recommendedInters;

    public ReadingExpertEditionDto(List<ExpertEditionDto> editions,
                                   List<RecommendedInterDto> recommendedInters,
                                   ReadingRecommendation recommendation,
                                   RecommendedInterDto currentInter,
                                   RecommendedInterDto prevInter) {
        setEditions(editions);
        setRecommendedInters(recommendedInters);
        setRead(recommendation.getRead());
        setCurrentInter(currentInter);
        setPrevInter(prevInter);
    }

    public RecommendedInterDto getCurrentInter() {
        return currentInter;
    }

    public void setCurrentInter(RecommendedInterDto currentInter) {
        this.currentInter = currentInter;
    }

    public RecommendedInterDto getPrevInter() {
        return prevInter;
    }

    public void setPrevInter(RecommendedInterDto prevInter) {
        this.prevInter = prevInter;
    }

    public List<String> getRead() {
        return read;
    }

    public void setRead(List<String> read) {
        this.read = read;
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
