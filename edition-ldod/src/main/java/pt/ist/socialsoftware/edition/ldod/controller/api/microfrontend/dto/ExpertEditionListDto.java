package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;

public class ExpertEditionListDto {
	
	private EditionType sourceType;
	private int sortedSize;
	private String acronym;
	private List<FragInterDto> sortedInterpsList;
	private String editor;

	public ExpertEditionListDto(ExpertEdition edition) {
		this.setSourceType(edition.getSourceType());
		this.setSortedSize(edition.getSortedInterps().size());
		this.setAcronym(edition.getAcronym());
		this.setSortedInterpsList(edition.getSortedInterps().stream()
								.map(FragInterDto::new)
								.collect(Collectors.toList()));
		this.setEditor(edition.getEditor());
	}

	public EditionType getSourceType() {
		return sourceType;
	}

	public void setSourceType(EditionType sourceType) {
		this.sourceType = sourceType;
	}

	public int getSortedSize() {
		return sortedSize;
	}

	public void setSortedSize(int sortedSize) {
		this.sortedSize = sortedSize;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public List<FragInterDto> getSortedInterpsList() {
		return sortedInterpsList;
	}

	public void setSortedInterpsList(List<FragInterDto> sortedInterpsList) {
		this.sortedInterpsList = sortedInterpsList;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

}
