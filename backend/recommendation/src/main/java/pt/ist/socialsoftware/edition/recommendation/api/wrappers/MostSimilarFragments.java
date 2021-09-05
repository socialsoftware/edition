package pt.ist.socialsoftware.edition.recommendation.api.wrappers;

import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.recommendation.api.dto.WeightsDto;

import java.util.Set;

public class MostSimilarFragments {

    private FragmentDto toReadFragment;
    private Set<FragmentDto> toBeRecommended;
    private WeightsDto weightsDto;

    public MostSimilarFragments(FragmentDto fragmentDto, Set<FragmentDto> fragmentDtos, WeightsDto weightsDto) {
        this.toReadFragment = fragmentDto;
        this.toBeRecommended = fragmentDtos;
        this.weightsDto = weightsDto;
    }

    public MostSimilarFragments() {}

    public FragmentDto getToReadFragment() {
        return toReadFragment;
    }

    public void setToReadFragment(FragmentDto toReadFragment) {
        this.toReadFragment = toReadFragment;
    }

    public Set<FragmentDto> getToBeRecommended() {
        return toBeRecommended;
    }

    public void setToBeRecommended(Set<FragmentDto> toBeRecommended) {
        this.toBeRecommended = toBeRecommended;
    }

    public WeightsDto getWeightsDto() {
        return weightsDto;
    }

    public void setWeightsDto(WeightsDto weightsDto) {
        this.weightsDto = weightsDto;
    }
}
