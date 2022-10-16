package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

import java.util.List;
import java.util.stream.Collectors;

public class EditorialInterDto extends FragInterDto {

    private String heteronym;
    private String sourceType;
    private String acronym;
    private String editor;
    private String volume;
    private Integer startPage;
    private Integer endPage;
    private String notes;
    private List<String> annexNote;
    private String nextXmlId;
    private String prevXmlId;
    private String nextUrlId;
    private String prevUrlId;

    private String interTitle;


    public EditorialInterDto(FragInter inter) {
        super(inter);
    }

    public EditorialInterDto(ExpertEditionInter inter) {
        super(inter);
        setTitle(inter.getTitle());
        setShortName(inter.getShortName());
        setAcronym(inter.getEdition().getAcronym());
        setHeteronym(inter);
        setEditor(inter.getExpertEdition().getEditor());
        setVolume(inter.getVolume());
        setNumber(inter.getNumber());
        setStartPage(inter.getStartPage());
        setEndPage(inter.getEndPage());
        setNotes(inter.getNotes());
        setAnnexNote(inter);
        setSourceType(inter.getSourceType().getDesc());
    }


    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getNextXmlId() {
        return nextXmlId;
    }

    public void setNextXmlId(String nextXmlId) {
        this.nextXmlId = nextXmlId;
    }

    public String getPrevXmlId() {
        return prevXmlId;
    }

    public void setPrevXmlId(String prevXmlId) {
        this.prevXmlId = prevXmlId;
    }

    public String getNextUrlId() {
        return nextUrlId;
    }

    public void setNextUrlId(String nextUrlId) {
        this.nextUrlId = nextUrlId;
    }

    public String getPrevUrlId() {
        return prevUrlId;
    }

    public void setPrevUrlId(String prevUrlId) {
        this.prevUrlId = prevUrlId;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getHeteronym() {
        return heteronym;
    }

    public void setHeteronym(ExpertEditionInter inter) {
        this.heteronym = inter.getHeteronym().isNullHeteronym()
                ? null
                : inter.getHeteronym().getName();
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }


    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }

    public String getInterTitle() {
        return interTitle;
    }

    public void setInterTitle(String interTitle) {
        this.interTitle = interTitle;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getAnnexNote() {
        return annexNote;
    }

    public void setAnnexNote(ExpertEditionInter inter) {
        this.annexNote =
                inter.getSortedAnnexNote()
                        .stream()
                        .map(note -> note.getNoteText().generatePresentationText())
                        .collect(Collectors.toList());
    }


    public static final class EditorialInterDtoBuilder {
        private final EditorialInterDto editorialInterDto;

        private EditorialInterDtoBuilder(ExpertEditionInter inter) {
            editorialInterDto = new EditorialInterDto(inter);
        }

        private EditorialInterDtoBuilder(FragInter inter) {
            editorialInterDto = new EditorialInterDto(inter);
        }

        public static EditorialInterDtoBuilder anEditorialInterDto(ExpertEditionInter inter) {
            return new EditorialInterDtoBuilder(inter);
        }

        public static EditorialInterDtoBuilder anEditorialInterDto(FragInter inter) {
            return new EditorialInterDtoBuilder(inter);
        }

        public EditorialInterDtoBuilder shortName(String shortName) {
            editorialInterDto.setShortName(shortName);
            return this;
        }

        public EditorialInterDtoBuilder title(String title) {
            editorialInterDto.setTitle(title);
            return this;
        }

        public EditorialInterDtoBuilder interTitle(String title) {
            editorialInterDto.setInterTitle(title);
            return this;
        }

        public EditorialInterDtoBuilder nextXmlId(String nextXmlId) {
            editorialInterDto.setNextXmlId(nextXmlId);
            return this;
        }

        public EditorialInterDtoBuilder prevXmlId(String prevXmlId) {
            editorialInterDto.setPrevXmlId(prevXmlId);
            return this;
        }

        public EditorialInterDtoBuilder nextUrlId(String nextUrlId) {
            editorialInterDto.setNextUrlId(nextUrlId);
            return this;
        }

        public EditorialInterDtoBuilder prevUrlId(String prevUrlId) {
            editorialInterDto.setPrevUrlId(prevUrlId);
            return this;
        }

        public EditorialInterDtoBuilder acronym(String acronym) {
            editorialInterDto.setAcronym(acronym);
            return this;
        }

        public EditorialInterDto build() {
            return editorialInterDto;
        }
    }


}
