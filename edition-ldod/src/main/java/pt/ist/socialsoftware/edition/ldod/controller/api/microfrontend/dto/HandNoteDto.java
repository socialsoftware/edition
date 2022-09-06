package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.HandNote;

public class HandNoteDto {

	private String desc;
	private String note;

	public HandNoteDto(HandNote handNote) {
		if(handNote.getMedium() != null) {
			this.setDesc(handNote.getMedium().getDesc());
		}
		this.setNote(handNote.getNote());
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}