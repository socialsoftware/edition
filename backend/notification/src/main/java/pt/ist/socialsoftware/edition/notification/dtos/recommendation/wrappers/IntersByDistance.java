package pt.ist.socialsoftware.edition.notification.dtos.recommendation.wrappers;


import pt.ist.socialsoftware.edition.notification.dtos.recommendation.WeightsDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;

public class IntersByDistance {

    private ScholarInterDto scholarInterDto;
    private WeightsDto weightsDto;
    private VirtualEditionInterDto virtualEditionInterDto;

    public IntersByDistance(ScholarInterDto scholarInterDto, WeightsDto weightsDto) {
        this.scholarInterDto = scholarInterDto;
        this.weightsDto = weightsDto;
    }

    public IntersByDistance(VirtualEditionInterDto virtualEditionInterDto, WeightsDto weightsDto) {
        this.virtualEditionInterDto = virtualEditionInterDto;
        this.weightsDto = weightsDto;
    }

    public ScholarInterDto getScholarInterDto() {
        return scholarInterDto;
    }

    public void setScholarInterDto(ScholarInterDto scholarInterDto) {
        this.scholarInterDto = scholarInterDto;
    }

    public WeightsDto getWeightsDto() {
        return weightsDto;
    }

    public void setWeightsDto(WeightsDto weightsDto) {
        this.weightsDto = weightsDto;
    }

    public VirtualEditionInterDto getVirtualEditionInterDto() {
        return virtualEditionInterDto;
    }

    public void setVirtualEditionInterDto(VirtualEditionInterDto virtualEditionInterDto) {
        this.virtualEditionInterDto = virtualEditionInterDto;
    }
}
