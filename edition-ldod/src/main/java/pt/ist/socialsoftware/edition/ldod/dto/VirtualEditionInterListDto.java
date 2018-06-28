package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class VirtualEditionInterListDto {
	private List<VirtualEditionInterDto> virtualEditionInterList = new ArrayList<>();
	private String virtualEditionTitle;

	public VirtualEditionInterListDto() {
	}

	public VirtualEditionInterListDto(VirtualEdition virtualEdition) {
		this.setVirtualEditionInterList(
				virtualEdition.getIntersSet().stream().sorted((i1, i2) -> i2.getNumber() - i1.getNumber())
						.map(i -> new VirtualEditionInterDto(i.getTitle(), i.getNumber(), i.getUrlId()))
						.collect(Collectors.toList()));
		this.setVirtualEditionTitle(virtualEdition.getTitle());
	}

	public List<VirtualEditionInterDto> getVirtualEditionInterList() {
		return this.virtualEditionInterList;
	}

	public void setVirtualEditionInterList(List<VirtualEditionInterDto> virtualEditionInterList) {
		this.virtualEditionInterList = virtualEditionInterList;
	}

	public String getVirtualEditionTitle() {
		return virtualEditionTitle;
	}

	public void setVirtualEditionTitle(String virtualEditionTitle) {
		this.virtualEditionTitle = virtualEditionTitle;
	}


}
