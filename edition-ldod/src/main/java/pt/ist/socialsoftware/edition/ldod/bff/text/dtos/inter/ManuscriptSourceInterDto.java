package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.HandNoteDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.SurfaceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.TypeNoteDto;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

import java.util.List;
import java.util.stream.Collectors;

public class ManuscriptSourceInterDto extends SourceInterDto {
    private String identification;
    private String form;
    private List<String> dimensionList;
    private String material;
    private int columns;
    private Boolean hadLdoDLabel;
    private List<HandNoteDto> handNoteList;
    private List<TypeNoteDto> typeNoteList;
    private String notes;

    public ManuscriptSourceInterDto(SourceInter inter) {
        super(inter);
    }

    public ManuscriptSourceInterDto(SourceInter inter, List<SurfaceDto> surfaceDtoList) {
        super(inter, surfaceDtoList);
        ManuscriptSource source = (ManuscriptSource) inter.getSource();
        setIdentification(source.getIdno());
        setForm(source.getForm().name());
        setMaterial(source.getMaterial().name());
        setColumns(source.getColumns());
        setHandNoteList(source);
        setTypeNoteList(source);
        setHadLdoDLabel(source.getHasLdoDLabel());
        setDimensionList(source);
        setNotes(source.getNotes());

    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public List<String> getDimensionList() {
        return dimensionList;
    }


    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Boolean getHadLdoDLabel() {
        return hadLdoDLabel;
    }

    public void setHadLdoDLabel(Boolean hadLdoDLabel) {
        this.hadLdoDLabel = hadLdoDLabel;
    }

    public List<HandNoteDto> getHandNoteList() {
        return handNoteList;
    }


    public List<TypeNoteDto> getTypeNoteList() {
        return typeNoteList;
    }


    public void setHandNoteList(ManuscriptSource source) {
        this.handNoteList = source.getHandNoteSet()
                .stream()
                .map(HandNoteDto::new)
                .collect(Collectors.toList());
    }

    public void setTypeNoteList(ManuscriptSource source) {
        this.typeNoteList = source.getTypeNoteSet()
                .stream()
                .map(TypeNoteDto::new)
                .collect(Collectors.toList());
    }

    public void setDimensionList(ManuscriptSource source) {
        this.dimensionList = source.getSortedDimensions().stream().map(dimensions -> String.format("%.1fcm X %.1fcm", dimensions.getHeight(), dimensions.getWidth())).collect(Collectors.toList());
    }

}
