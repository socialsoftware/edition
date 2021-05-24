package pt.ist.socialsoftware.edition.ldod.frontend.reading.recommendationDto;

public class InterIdDistancePairDto {
    private String interId;
    private double distance;

    public InterIdDistancePairDto(String interId, double distance) {
        this.interId = interId;
        this.distance = distance;
    }

    public InterIdDistancePairDto() {
    }

    public String getInterId() {
        return this.interId;
    }

    public void setInter(String interId) {
        this.interId = interId;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
