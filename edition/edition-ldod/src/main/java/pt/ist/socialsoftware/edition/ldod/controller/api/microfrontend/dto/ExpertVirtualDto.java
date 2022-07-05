package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class ExpertVirtualDto {
	private List<ExpertEditionDto> expertEditions;
	private String archiveEditionExternalId;
	private List<VirtualEditionDto> virtualEditions;

	public ExpertVirtualDto() {
		
	}

	public ExpertVirtualDto(Set<ExpertEdition> expertEditionsSet, Set<VirtualEdition> virtualEditionsSet,
			VirtualEdition archiveEdition, LdoDUser user) {
		this.setExpertEditions(expertEditionsSet.stream().map(ExpertEditionDto::new).collect(Collectors.toList()));
		this.setVirtualEditions(virtualEditionsSet.stream().map(vEdition -> new VirtualEditionDto(vEdition, user)).collect(Collectors.toList()));
		this.setArchiveEditionExternalId(archiveEdition.getExternalId());
	}

	public ExpertVirtualDto(Set<ExpertEdition> expertEditionsSet, Set<VirtualEdition> virtualEditionsSet,
			VirtualEdition archiveEdition) {
		this.setExpertEditions(expertEditionsSet.stream().map(ExpertEditionDto::new).collect(Collectors.toList()));
		this.setArchiveEditionExternalId(archiveEdition.getExternalId());
		this.setVirtualEditions(virtualEditionsSet.stream().map(vEdition -> new VirtualEditionDto(vEdition, true)).collect(Collectors.toList()));
	}

	public List<ExpertEditionDto> getExpertEditions() {
		return expertEditions;
	}

	public void setExpertEditions(List<ExpertEditionDto> expertEditions) {
		this.expertEditions = expertEditions;
	}

	public String getArchiveEditionExternalId() {
		return archiveEditionExternalId;
	}

	public void setArchiveEditionExternalId(String archiveEditionExternalId) {
		this.archiveEditionExternalId = archiveEditionExternalId;
	}

	public List<VirtualEditionDto> getVirtualEditions() {
		return virtualEditions;
	}

	public void setVirtualEditions(List<VirtualEditionDto> virtualEditions) {
		this.virtualEditions = virtualEditions;
	}
}
