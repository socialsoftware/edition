package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.TypeNote;

public class TypeNoteDto {
	private String desc;
	private String note;

	public TypeNoteDto(TypeNote typeNote) {
		if(typeNote.getMedium() != null) {
			this.setDesc(typeNote.getMedium().getDesc());
		}
		this.setNote(typeNote.getNote());
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
