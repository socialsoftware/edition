package pt.ist.socialsoftware.edition.ldod.bff.search;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.search.options.TextSearchOption;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    public List<FragInterDto> getSimpleSearch(SimpleSearchDto simpleSearchDto) {
        TextSearchOption searchOption = new TextSearchOption(TextSearchOption.purgeSearchText(simpleSearchDto.getSearchTerm()));
        return searchOption.search()
                .stream()
                .filter(inter -> simpleSearchDto.getSearchSource().equals("")
                        || inter.getShortName().toLowerCase().contains(simpleSearchDto.getSearchSource().toLowerCase()))
                .filter(inter -> simpleSearchDto.getSearchType().equals("")
                        || inter.getTitle().toLowerCase().contains(simpleSearchDto.getSearchTerm().toLowerCase()))
                .map(fragInter -> fragInter.getSourceType().equals(Edition.EditionType.EDITORIAL)
                        ? EditorialInterDto.EditorialInterDtoBuilder.anEditorialInterDto(fragInter)
                        .shortName(fragInter.getShortName())
                        .title(fragInter.getFragment().getTitle())
                        .interTitle(fragInter.getTitle())
                        .acronym(((ExpertEdition) fragInter.getEdition()).getEditor())
                        .build()
                        : SourceInterDto.SourceInterDtoBuilder.aSourceInterDto(fragInter)
                        .shortName(fragInter.getShortName())
                        .interTitle(fragInter.getTitle())
                        .title(fragInter.getFragment().getTitle())
                        .build())
                .collect(Collectors.toList());

    }
}
