package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;

import java.util.List;
import java.util.stream.Collectors;

public class ManuscriptSourceDto extends SourceInterDto {
    private String identification;
    private String form;
    private String material;
    private Integer columns;

    private final String sourceType = SourceType.MANUSCRIPT.name();
    private List<String> handNoteMediumList;
    private List<String> typeNoteMediumList;
    private Boolean hadLdoDLabel;
    private List<String> dimensionList;
    private String notes;


    public ManuscriptSourceDto(ManuscriptSource source) {
        super(source);
        setIdentification(source.getIdno());
        setForm(source.getForm().name());
        setMaterial(source.getMaterial().name());
        setColumns(source.getColumns());
        setHandNoteMediumList(source);
        setTypeNoteMediumList(source);
        setHadLdoDLabel(source);
        setDimensionDtoList(source);
        setNotes(source.getNotes());
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }


    public void setForm(String form) {
        this.form = form;
    }


    public void setMaterial(String material) {
        this.material = material;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public void setHandNoteMediumList(ManuscriptSource source) {
        this.handNoteMediumList = source.getHandNoteSet().stream().map(handNote -> handNote.getMedium().name()).collect(Collectors.toList());
    }

    public void setTypeNoteMediumList(ManuscriptSource source) {
        this.typeNoteMediumList = source.getTypeNoteSet().stream().map(typeNote -> typeNote.getMedium().name()).collect(Collectors.toList());
    }

    public void setHadLdoDLabel(ManuscriptSource source) {
        this.hadLdoDLabel = source.getHasLdoDLabel();
    }

    public void setDimensionDtoList(ManuscriptSource source) {
        this.dimensionList = source.getSortedDimensions().stream().map(dimensions -> String.format("%.1fcm X %.1fcm", dimensions.getHeight(), dimensions.getWidth())).collect(Collectors.toList());
    }

    public String getIdentification() {
        return this.identification;
    }

    public String getForm() {
        return form;
    }

    public String getMaterial() {
        return material;
    }

    public Integer getColumns() {
        return columns;
    }

    public String getSourceType() {
        return sourceType;
    }

    public List<String> getHandNoteMediumList() {
        return handNoteMediumList;
    }

    public List<String> getTypeNoteMediumList() {
        return typeNoteMediumList;
    }

    public Boolean getHadLdoDLabel() {
        return hadLdoDLabel;
    }

    public List<String> getDimensionList() {
        return dimensionList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
