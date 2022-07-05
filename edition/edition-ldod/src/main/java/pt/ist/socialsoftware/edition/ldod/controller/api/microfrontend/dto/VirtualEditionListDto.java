package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

public class VirtualEditionListDto {

	private String title;
	private List<ParticipantDto> participantList;
	private String synopsis;
	private String acronym;
	private int interpsSize;
	private List<FragInterDto> sortedInterpsList;
	

	public VirtualEditionListDto(VirtualEdition edition, String type) {	
		if(type.equals("shallow")) {
			if(edition.getTitle()!=null) {
				this.setTitle(edition.getTitle());
			}
		}
		else {
			if(edition.getTitle()!=null) {
				this.setTitle(edition.getTitle());
			}
			this.setParticipantList(edition.getParticipantList().stream()
					.map(ParticipantDto::new)
					.collect(Collectors.toList()));
			this.setSynopsis(edition.getSynopsis());
			if(edition.getTaxonomy().getCategoriesSet().size() > 0) {
				this.setAcronym(edition.getAcronym());
				}
			this.setInterpsSize(edition.getSortedInterps().size());
			this.setSortedInterpsList(edition.getSortedInterps().stream()
					.map(FragInter -> new FragInterDto(FragInter, edition))
					.collect(Collectors.toList()));
		}
		
	}

	private void setSortedInterpsList(List<FragInterDto> sortedInterpsList) {
		this.sortedInterpsList = sortedInterpsList;
	}
	
	public List<FragInterDto> getSortedInterpsList() {
		return sortedInterpsList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ParticipantDto> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(List<ParticipantDto> participantList) {
		this.participantList = participantList;
	}

	public int getInterpsSize() {
		return interpsSize;
	}

	public void setInterpsSize(int interpsSize) {
		this.interpsSize = interpsSize;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
}
