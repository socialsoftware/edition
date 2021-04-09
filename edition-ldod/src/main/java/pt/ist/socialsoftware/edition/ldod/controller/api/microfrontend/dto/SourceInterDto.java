package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.Form;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource.Material;
import pt.ist.socialsoftware.edition.ldod.domain.PrintedSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

public class SourceInterDto {
    private SourceType sourceType;
    private String title;
    private Form form;
    private Integer dimensionSetSize;
    private Material material;
    private Integer columns;
	private Boolean hadLdoDLabel;
	private String journal;
	private String issue;
	private int startPage;
	private int endPage;
	private String pubPlace;
	private String notes;
	private String altIdentifier;
	private String desc;
	private List<DimensionsDto> dimensionDtoList;
	private List<HandNoteDto> handNoteDtoSet;
	private List<TypeNoteDto> typeNoteSet;
	private List<SurfaceDto> surfaceDto;
	private String date;
	private List<SourceInterSimpleDto> sourceInterSet;
	



    public SourceInterDto(SourceInter sourceInter) {
        this.sourceType = sourceInter.getSource().getType();
        if(sourceInter.getLdoDDate()!= null) {
        	this.setDate(sourceInter.getLdoDDate().print());
        	if(sourceInter.getLdoDDate().getPrecision() != null) {
        		this.setDesc(sourceInter.getLdoDDate().getPrecision().getDesc());
        	}	
        }

        if (sourceInter.getSource().getType().equals(Source.SourceType.MANUSCRIPT)) {
            ManuscriptSource source = (ManuscriptSource) sourceInter.getSource();
            this.title = "";
            this.setForm(source.getForm());
            this.setDimensionSetSize(source.getDimensionsSet().size());
            
            this.setDimensionDtoList(source.getSortedDimensions().stream()
            								.map(DimensionsDto::new)
            								.collect(Collectors.toList()));
            
            this.setHandNoteDtoSet(source.getHandNoteSet().stream()
            						.map(HandNoteDto::new)
            						.collect(Collectors.toList()));
            
            this.setTypeNoteSet(source.getTypeNoteSet().stream()
								.map(TypeNoteDto::new)
								.collect(Collectors.toList()));
            
            this.setMaterial(source.getMaterial());
            this.setColumns(source.getColumns());
            this.setHadLdoDLabel(source.getHasLdoDLabel());
            this.setNotes(source.getNotes());
            
            if(source.getFacsimile()!=null) {
            	if(source.getFacsimile().getSurfaces()!=null) {
                	this.setSurfaceDto(source.getFacsimile().getSurfaces().stream()
            						.map(SurfaceDto::new)
            						.collect(Collectors.toList()));
                }
            }
            
            
            this.setAltIdentifier(source.getAltIdentifier());
        } else {
            PrintedSource source = (PrintedSource) sourceInter.getSource();
            this.title = source.getTitle();
            this.setJournal(source.getJournal());
            this.setIssue(source.getIssue());
            this.setStartPage(source.getStartPage());
            this.setEndPage(source.getEndPage());
            this.setPubPlace(source.getPubPlace());
            this.setAltIdentifier(source.getAltIdentifier());
            
            
            if(source.getFacsimile()!=null) {
            	if(source.getFacsimile().getSurfaces()!=null) {
                	this.setSurfaceDto(source.getFacsimile().getSurfaces().stream()
            						.map(SurfaceDto::new)
            						.collect(Collectors.toList()));
                }
            }
    	}
       
        
    }
    
    public SourceInterDto(Source source) {
    	this.setSourceType(source.getType());
    	this.setTitle(source.getName());
    	if(source.getLdoDDate()!= null) {
        	this.setDate(source.getLdoDDate().print());
        }
    	this.setSourceInterSet(source.getSourceIntersSet().stream()
				.map(SourceInterSimpleDto::new)
				.collect(Collectors.toList()));
    	
    	if (source.getType().equals(Source.SourceType.MANUSCRIPT)) {
            ManuscriptSource sourceMan = (ManuscriptSource) source;
            this.setDimensionSetSize(sourceMan.getDimensionsSet().size());
            
            this.setDimensionDtoList(sourceMan.getSortedDimensions().stream()
            								.map(DimensionsDto::new)
            								.collect(Collectors.toList()));
            
            this.setHandNoteDtoSet(sourceMan.getHandNoteSet().stream()
            						.map(HandNoteDto::new)
            						.collect(Collectors.toList()));
            
            this.setTypeNoteSet(sourceMan.getTypeNoteSet().stream()
								.map(TypeNoteDto::new)
								.collect(Collectors.toList()));
            

            this.setHadLdoDLabel(sourceMan.getHasLdoDLabel());
            
            if(sourceMan.getFacsimile()!=null) {
            	if(sourceMan.getFacsimile().getSurfaces()!=null) {
                	this.setSurfaceDto(sourceMan.getFacsimile().getSurfaces().stream()
            						.map(SurfaceDto::new)
            						.collect(Collectors.toList()));
                }
            }
            
            
    	} else {
            PrintedSource sourcePri = (PrintedSource) source;
            if(sourcePri.getFacsimile()!=null) {
            	if(sourcePri.getFacsimile().getSurfaces()!=null) {
                	this.setSurfaceDto(sourcePri.getFacsimile().getSurfaces().stream()
            						.map(SurfaceDto::new)
            						.collect(Collectors.toList()));
                }
            }
    	}
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Integer getDimensionSetSize() {
		return dimensionSetSize;
	}

	public void setDimensionSetSize(Integer dimensionSetSize) {
		this.dimensionSetSize = dimensionSetSize;
	}


	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public Boolean getHadLdoDLabel() {
		return hadLdoDLabel;
	}

	public void setHadLdoDLabel(Boolean hadLdoDLabel) {
		this.hadLdoDLabel = hadLdoDLabel;
	}


	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
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

	public String getPubPlace() {
		return pubPlace;
	}

	public void setPubPlace(String pubPlace) {
		this.pubPlace = pubPlace;
	}


	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getAltIdentifier() {
		return altIdentifier;
	}

	public void setAltIdentifier(String altIdentifier) {
		this.altIdentifier = altIdentifier;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<DimensionsDto> getDimensionDtoList() {
		return dimensionDtoList;
	}

	public void setDimensionDtoList(List<DimensionsDto> dimensionDtoList) {
		this.dimensionDtoList = dimensionDtoList;
	}

	public List<HandNoteDto> getHandNoteDtoSet() {
		return handNoteDtoSet;
	}

	public void setHandNoteDtoSet(List<HandNoteDto> handNoteDtoSet) {
		this.handNoteDtoSet = handNoteDtoSet;
	}

	public List<TypeNoteDto> getTypeNoteSet() {
		return typeNoteSet;
	}

	public void setTypeNoteSet(List<TypeNoteDto> typeNoteSet) {
		this.typeNoteSet = typeNoteSet;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<SurfaceDto> getSurfaceDto() {
		return surfaceDto;
	}

	public void setSurfaceDto(List<SurfaceDto> surfaceDto) {
		this.surfaceDto = surfaceDto;
	}

	public List<SourceInterSimpleDto> getSourceInterSet() {
		return sourceInterSet;
	}

	public void setSourceInterSet(List<SourceInterSimpleDto> sourceInterSet) {
		this.sourceInterSet = sourceInterSet;
	}

}
