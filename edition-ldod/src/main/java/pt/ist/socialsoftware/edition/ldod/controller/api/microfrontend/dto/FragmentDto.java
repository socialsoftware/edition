package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FragmentDto {
    private String fragmentXmlId;
    private String title;
    private Map<String, ExpertEditionInterDto> expertEditionInterDtoMap;
    private List<SourceInterDto> sourceInterDtoList;
	private String externalId;
	private String exportString;

    public FragmentDto(Fragment fragment) {
        setFragmentXmlId(fragment.getXmlId());
        setTitle(fragment.getTitle());

        setExpertEditionInterDtoMap(fragment.getExpertEditionInterSet().stream()
                .map(ExpertEditionInterDto::new)
                .collect(Collectors.toMap(ExpertEditionInterDto::getAcronym, Function.identity(), (existing, replacement) -> existing)));

        setSourceInterDtoList(fragment.getSortedSourceInter().stream()
                .map(SourceInterDto::new)
                .collect(Collectors.toList()));
        setExternalId(fragment.getExternalId());
    }

    public FragmentDto(Fragment fragment, String s) {
        this.setFragmentXmlId(fragment.getXmlId());
        this.setTitle(fragment.getTitle());
        this.setExternalId(fragment.getExternalId());
        this.setExportString(s);
    }

    public String getFragmentXmlId() {
        return fragmentXmlId;
    }

    public void setFragmentXmlId(String fragmentXmlId) {
        this.fragmentXmlId = fragmentXmlId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, ExpertEditionInterDto> getExpertEditionInterDtoMap() {
        return expertEditionInterDtoMap;
    }

    public void setExpertEditionInterDtoMap(Map<String, ExpertEditionInterDto> expertEditionInterDtoMap) {
        this.expertEditionInterDtoMap = expertEditionInterDtoMap;
    }

    public List<SourceInterDto> getSourceInterDtoList() {
        return sourceInterDtoList;
    }

    public void setSourceInterDtoList(List<SourceInterDto> sourceInterDtoList) {
        this.sourceInterDtoList = sourceInterDtoList;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExportString() {
        return exportString;
    }

    public void setExportString(String exportString) {
        this.exportString = exportString;
    }
}
