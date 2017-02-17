package pt.ist.socialsoftware.edition.dto;

import java.util.ArrayList;
import java.util.List;

public class EditionTranscriptionsDTO {
	List<TranscriptionDTO> transcriptions = new ArrayList<>();

	public EditionTranscriptionsDTO() {
	}

	public EditionTranscriptionsDTO(List<TranscriptionDTO> transcriptions) {
		this.transcriptions = transcriptions;
	}

	public List<TranscriptionDTO> getTranscriptions() {
		return this.transcriptions;
	}

	public void setTranscriptions(List<TranscriptionDTO> transcriptions) {
		this.transcriptions = transcriptions;
	}
}
