package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class VirtualRecommendationDto {

	private VirtualEditionDto virtualEdition;
	private List<VirtualEditionInterDto> recommendedEditionInter;
	private String interId;

	public VirtualRecommendationDto(VirtualEdition virtualEdition, List<VirtualEditionInter> recommendedEdition) {
		this.setVirtualEdition(new VirtualEditionDto(virtualEdition));
		this.setRecommendedEditionInter(recommendedEdition.stream().map(VirtualEditionInterDto::new).collect(Collectors.toList()));
	}

	public VirtualRecommendationDto(VirtualEdition virtualEdition, List<VirtualEditionInter> recommendedEdition,
			String id) {
		this.setVirtualEdition(new VirtualEditionDto(virtualEdition));
		this.setRecommendedEditionInter(recommendedEdition.stream().map(VirtualEditionInterDto::new).collect(Collectors.toList()));
		this.setInterId(id);
	}
	
	public VirtualRecommendationDto(VirtualEdition virtualEdition) {
		this.setVirtualEdition(new VirtualEditionDto(virtualEdition));
	}


	public VirtualEditionDto getVirtualEdition() {
		return virtualEdition;
	}

	public void setVirtualEdition(VirtualEditionDto virtualEdition) {
		this.virtualEdition = virtualEdition;
	}

	public List<VirtualEditionInterDto> getRecommendedEditionInter() {
		return recommendedEditionInter;
	}

	public void setRecommendedEditionInter(List<VirtualEditionInterDto> recommendedEditionInter) {
		this.recommendedEditionInter = recommendedEditionInter;
	}

	public String getInterId() {
		return interId;
	}

	public void setInterId(String interId) {
		this.interId = interId;
	}




}
