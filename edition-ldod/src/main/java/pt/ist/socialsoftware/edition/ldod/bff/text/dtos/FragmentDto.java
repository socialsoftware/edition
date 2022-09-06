package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.ManuscriptSourceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.PrintedSourceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FragmentDto {

    private String externalId;
    private String title;
    private String xmlId;
    private Map<String, List<EditorialInterDto>> expertsInterMap;
    private List<SourceInterDto> sortedSourceInterList;


    public FragmentDto(Fragment fragment) {
        setExternalId(fragment.getExternalId());
        setTitle(fragment.getTitle());
        setXmlId(fragment.getXmlId());
        setExpertsInterMap(fragment);
        setSortedSourceInterList(fragment);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public Map<String, List<EditorialInterDto>> getExpertsInterMap() {
        return expertsInterMap;
    }

    public void setExpertsInterMap(Fragment fragment) {

        this.expertsInterMap = fragment.getExpertEditionInterSet()
                .stream()
                .collect(Collectors.groupingBy(
                        inter -> inter.getEdition().getAcronym(),
                        Collectors.mapping(EditorialInterDto::new, Collectors.toList())));
    }

    public List<SourceInterDto> getSortedSourceInterList() {
        return sortedSourceInterList;
    }

    public void setSortedSourceInterList(Fragment fragment) {
        this.sortedSourceInterList = fragment.getSortedSourceInter()
                .stream()
                .map(sourceInter -> isManuscript(sourceInter)
                        ? new ManuscriptSourceDto(((ManuscriptSource) sourceInter.getSource()))
                        : new PrintedSourceDto((PrintedSource) sourceInter.getSource())).collect(Collectors.toList());
    }

    private boolean isManuscript(SourceInter sourceInter) {
        return sourceInter.getSource().getType().equals(Source.SourceType.MANUSCRIPT);
    }
}
