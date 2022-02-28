package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import lombok.Getter;
import lombok.Setter;
import pt.ist.socialsoftware.edition.ldod.domain.HandNote;

@Getter
@Setter
public class HandNoteDto {

	private String desc;
	private String note;

	public HandNoteDto(HandNote handNote) {
		if(handNote.getMedium() != null) {
			this.setDesc(handNote.getMedium().getDesc());
		}
		this.setNote(handNote.getNote());
	}
}
