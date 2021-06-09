package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class VirtualEditionDto {

	private boolean participantSetContains;
	private List<VirtualEditionInterDto> sortedInter4Frag;
	private String acronym;
	private String externalId;
	

	public VirtualEditionDto(VirtualEdition vEdition, Fragment fragment, LdoDUser user) {
		this.setSortedInter4Frag(vEdition.getSortedInter4Frag(fragment).stream().map(VirtualEditionInterDto::new).collect(Collectors.toList()));
		this.setAcronym(vEdition.getAcronym());
		this.setParticipantSetContains(vEdition.getParticipantSet().contains(user));
		this.setExternalId(vEdition.getExternalId());
	}

	public boolean isParticipantSetContains() {
		return participantSetContains;
	}

	public void setParticipantSetContains(boolean participantSetContains) {
		this.participantSetContains = participantSetContains;
	}

	public List<VirtualEditionInterDto> getSortedInter4Frag() {
		return sortedInter4Frag;
	}

	public void setSortedInter4Frag(List<VirtualEditionInterDto> sortedInter4Frag) {
		this.sortedInter4Frag = sortedInter4Frag;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
