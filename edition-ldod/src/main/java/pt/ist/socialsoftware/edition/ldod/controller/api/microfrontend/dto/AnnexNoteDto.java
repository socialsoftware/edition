package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.AnnexNote;

public class AnnexNoteDto {
	private String presentationText;
	
	public AnnexNoteDto(AnnexNote annexNote) {
		this.setPresentationText(annexNote.getNoteText().generatePresentationText());
	}

	public String getPresentationText() {
		return presentationText;
	}

	public void setPresentationText(String presentationText) {
		this.presentationText = presentationText;
	}

	
}
