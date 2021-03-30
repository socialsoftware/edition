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

    public FragmentDto(Fragment fragment) {
        this.fragmentXmlId = fragment.getXmlId();
        this.title = fragment.getTitle();

        this.expertEditionInterDtoMap = fragment.getExpertEditionInterSet().stream()
                .map(ExpertEditionInterDto::new)
                .collect(Collectors.toMap(ExpertEditionInterDto::getEditionAcronym, Function.identity()));

        this.sourceInterDtoList = fragment.getSortedSourceInter().stream()
                .map(SourceInterDto::new)
                .collect(Collectors.toList());
    }
}
