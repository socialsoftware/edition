package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;

public class ExpertEditionInterDto {
    private String editionAcronym;
    private String sourceType;
    private String title;
	private boolean heteronymNull;
	private String heteronymName;
	private String volume;
	private String completeNumber;
	private int startPage;
	private int endPage;
	private String notes;
	private List<AnnexNoteDto> annexNoteDtoList;
	private String desc;
	private String date;

    public ExpertEditionInterDto(ExpertEditionInter expertEditionInter) {
        this.editionAcronym = expertEditionInter.getEdition().getAcronym();
        this.sourceType = expertEditionInter.getSourceType().name();
        this.title = expertEditionInter.getTitle();
        this.setHeteronymNull(expertEditionInter.getHeteronym().isNullHeteronym());
        this.setHeteronymName(expertEditionInter.getHeteronym().getName());
        this.setVolume(expertEditionInter.getVolume());
        this.setCompleteNumber(expertEditionInter.getCompleteNumber());
        this.setStartPage(expertEditionInter.getStartPage());
        this.setEndPage(expertEditionInter.getEndPage());
        this.setNotes(expertEditionInter.getNotes());
        
        this.setAnnexNoteDtoList(expertEditionInter.getSortedAnnexNote().stream()
        							.map(AnnexNoteDto::new)
        								.collect(Collectors.toList()));
        if(expertEditionInter.getLdoDDate()!= null) {
        	this.setDate(expertEditionInter.getLdoDDate().print());
        	if(expertEditionInter.getLdoDDate().getPrecision() != null) {
        		this.setDesc(expertEditionInter.getLdoDDate().getPrecision().getDesc());
        	}	
        }
    }

    public String getEditionAcronym() {
        return editionAcronym;
    }

    public void setEditionAcronym(String editionAcronym) {
        this.editionAcronym = editionAcronym;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public boolean isHeteronymNull() {
		return heteronymNull;
	}

	public void setHeteronymNull(boolean heteronymNull) {
		this.heteronymNull = heteronymNull;
	}

	public String getHeteronymName() {
		return heteronymName;
	}

	public void setHeteronymName(String heteronymName) {
		this.heteronymName = heteronymName;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getCompleteNumber() {
		return completeNumber;
	}

	public void setCompleteNumber(String completeNumber) {
		this.completeNumber = completeNumber;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<AnnexNoteDto> getAnnexNoteDtoList() {
		return annexNoteDtoList;
	}

	public void setAnnexNoteDtoList(List<AnnexNoteDto> annexNoteDtoList) {
		this.annexNoteDtoList = annexNoteDtoList;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
