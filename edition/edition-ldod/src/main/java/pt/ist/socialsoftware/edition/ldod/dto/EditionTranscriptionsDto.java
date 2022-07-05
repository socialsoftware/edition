package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class EditionTranscriptionsDto {
	List<TranscriptionDto> transcriptions = new ArrayList<>();

	public EditionTranscriptionsDto() {
	}

	public EditionTranscriptionsDto(List<TranscriptionDto> transcriptions) {
		this.transcriptions = transcriptions;
	}

	public List<TranscriptionDto> getTranscriptions() {
		return this.transcriptions;
	}

	public void setTranscriptions(List<TranscriptionDto> transcriptions) {
		this.transcriptions = transcriptions;
	}
}
