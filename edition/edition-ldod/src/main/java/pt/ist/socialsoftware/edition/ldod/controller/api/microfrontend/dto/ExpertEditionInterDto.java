package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;

public class ExpertEditionInterDto {
    private String acronym;
    private String sourceType;
    private String title;
	private boolean heteronymNull;
	private String heteronym;
	private String volume;
	private String completeNumber;
	private int startPage;
	private int endPage;
	private String notes;
	private List<AnnexNoteDto> annexNoteDtoList;
	private String desc;
	private String date;
	private String urlId;
	private int number;
	private String fragmentXmlId;
	private String externalID;

    public ExpertEditionInterDto(ExpertEditionInter expertEditionInter) {
        this.setAcronym(expertEditionInter.getEdition().getAcronym());
        this.sourceType = expertEditionInter.getSourceType().name();
        this.title = expertEditionInter.getTitle();
        this.setHeteronymNull(expertEditionInter.getHeteronym().isNullHeteronym());
        this.setHeteronym(expertEditionInter.getHeteronym().getName());
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
        this.setUrlId(expertEditionInter.getUrlId());
        this.setNumber(expertEditionInter.getNumber());
        this.setFragmentXmlId(expertEditionInter.getFragment().getXmlId());
        this.setExternalID(expertEditionInter.getExternalId());
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

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getFragmentXmlId() {
		return fragmentXmlId;
	}

	public void setFragmentXmlId(String fragmentXmlId) {
		this.fragmentXmlId = fragmentXmlId;
	}

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}


	public String getHeteronym() {
		return heteronym;
	}


	public void setHeteronym(String heteronym) {
		this.heteronym = heteronym;
	}


	public String getAcronym() {
		return acronym;
	}


	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
}
