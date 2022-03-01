package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import lombok.Getter;
import lombok.Setter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class FragmentDto {
    private String fragmentXmlId;
    private String title;
    private Map<String, ExpertEditionInterDto> expertEditionInterDtoMap;
    private List<SourceInterDto> sourceInterDtoList;
	private String externalId;
	private String exportString;

    public FragmentDto(Fragment fragment) {
        this.setFragmentXmlId(fragment.getXmlId());
        this.setTitle(fragment.getTitle());

        this.setExpertEditionInterDtoMap(fragment.getExpertEditionInterSet().stream()
                .map(ExpertEditionInterDto::new)
                .collect(Collectors.toMap(ExpertEditionInterDto::getAcronym, Function.identity(), (existing, replacement) -> existing)));

        this.setSourceInterDtoList(fragment.getSortedSourceInter().stream()
                .map(SourceInterDto::new)
                .collect(Collectors.toList()));
        this.setExternalId(fragment.getExternalId());
    }

    public FragmentDto(Fragment fragment, String s) {
        this.setFragmentXmlId(fragment.getXmlId());
        this.setTitle(fragment.getTitle());
        this.setExternalId(fragment.getExternalId());
        this.setExportString(s);
    }

}
