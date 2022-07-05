package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class InterDistancePairDto {
	private VirtualEditionInter inter;
	private double distance;

	public InterDistancePairDto(VirtualEditionInter inter, double distance) {
		this.inter = inter;
		this.distance = distance;
	}

	public VirtualEditionInter getInter() {
		return this.inter;
	}

	public void setInter(VirtualEditionInter inter) {
		this.inter = inter;
	}

	public double getDistance() {
		return this.distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
