package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class SourcesDto {

	private List<SourceInterDto> sourceInterDtoList;
	
	public SourcesDto(Fragment fragment) {
		this.setSourceInterDtoList(fragment.getSourcesSet().stream()
				.map(SourceInterDto::new)
				.collect(Collectors.toList()));
	}

	public List<SourceInterDto> getSourceInterDtoList() {
		return sourceInterDtoList;
	}

	public void setSourceInterDtoList(List<SourceInterDto> sourceInterDtoList) {
		this.sourceInterDtoList = sourceInterDtoList;
	}
}
