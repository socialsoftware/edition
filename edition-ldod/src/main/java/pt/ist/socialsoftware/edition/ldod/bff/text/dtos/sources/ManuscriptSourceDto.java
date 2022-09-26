package pt.ist.socialsoftware.edition.ldod.bff.text.dtos.sources;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.HandNoteDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.TypeNoteDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;

import java.util.List;
import java.util.stream.Collectors;

public class ManuscriptSourceDto extends SourceDto {

    private List<HandNoteDto> handNoteList;
    private List<TypeNoteDto> typeNoteList;
    private Boolean hadLdoDLabel;
    private List<String> dimensionList;

    public ManuscriptSourceDto(ManuscriptSource source, List<SourceInterDto> sourceIntersList) {
        super(source, sourceIntersList);
        setHandNoteList(source);
        setTypeNoteList(source);
        setHadLdoDLabel(source);
        setDimensionDtoList(source);
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

    public void setHadLdoDLabel(ManuscriptSource source) {
        this.hadLdoDLabel = source.getHasLdoDLabel();
    }

    public void setDimensionDtoList(ManuscriptSource source) {
        this.dimensionList = source.getSortedDimensions().stream().map(dimensions -> String.format("%.1fcm X %.1fcm", dimensions.getHeight(), dimensions.getWidth())).collect(Collectors.toList());
    }


    public List<HandNoteDto> getHandNoteList() {
        return handNoteList;
    }

    public List<TypeNoteDto> getTypeNoteList() {
        return typeNoteList;
    }

    public Boolean getHadLdoDLabel() {
        return hadLdoDLabel;
    }

    public List<String> getDimensionList() {
        return dimensionList;
    }


}
