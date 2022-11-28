package pt.ist.socialsoftware.edition.ldod.bff.search;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.search.dto.*;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.search.options.Search;
import pt.ist.socialsoftware.edition.ldod.search.options.SearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.TextSearchOption;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    private List<AdvancedEditionDto> getEditions() {
        return LdoD.getInstance().getSortedExpertEdition().stream().map(this::getEdition).collect(Collectors.toList());
    }

    private AdvancedEditionDto getEdition(ExpertEdition ed) {
        return new AdvancedEditionDto(ed, getEditionHeteronyms(ed), getEditionSortedYears(ed));
    }

    private List<String> getEditionSortedYears(ExpertEdition ed) {
        return ed.getIntersSet()
                .stream()
                .filter(fragInter -> fragInter.getLdoDDate() != null)
                .map(fragInter -> fragInter.getLdoDDate().getDate().getYear())
                .sorted().distinct().map(Object::toString)
                .collect(Collectors.toList());
    }

    private Set<AdvancedHeteronymDto> getEditionHeteronyms(ExpertEdition ed) {
        return ed.getIntersSet()
                .stream()
                .map(FragInter_Base::getHeteronym).distinct()
                .map(AdvancedHeteronymDto::new)
                .collect(Collectors.toSet());
    }

    private List<AdvancedHeteronymDto> getHeteronyms() {
        return LdoD.getInstance().getHeteronymsSet().stream().map(AdvancedHeteronymDto::new).collect(Collectors.toList());
    }


    private AdvancedDatesDto getDates() {
        List<Integer> dates = Stream.concat(
                        getSourcesStream().map(source -> source.getLdoDDate().getDate().getYear()),
                        getFragIntersStream().map(inter -> inter.getLdoDDate().getDate().getYear())
                ).sorted()
                .collect(Collectors.toList());
        return dates.isEmpty()
                ? null
                : new AdvancedDatesDto(dates.get(0).toString(), dates.get(dates.size() - 1).toString());
    }

    private AdvancedPublicationDto getPublicationsDate() {
        List<Integer> publicationsYears = this.getPublicationsYearsSorted();
        return publicationsYears.isEmpty()
                ? null
                : new AdvancedPublicationDto(
                publicationsYears.get(0).toString(),
                publicationsYears.get(publicationsYears.size() - 1).toString());
    }


    private List<Integer> getPublicationsYearsSorted() {
        return getSourcesStream()
                .filter(source -> source.getType().equals(Source.SourceType.PRINTED))
                .map(source -> source.getLdoDDate().getDate().getYear())
                .sorted()
                .collect(Collectors.toList());
    }


    private Stream<Source> getSourcesStream() {
        return LdoD.getInstance().getFragmentsSet()
                .stream()
                .flatMap(fragment -> fragment.getSourcesSet().stream())
                .filter(source -> source.getLdoDDate() != null);

    }

    private Stream<FragInter> getFragIntersStream() {
        return LdoD.getInstance().getFragmentsSet()
                .stream()
                .flatMap(fragment -> fragment.getFragmentInterSet().stream())
                .filter(inter -> inter.getLdoDDate() != null);

    }


    private Stream<ManuscriptSource> getAuthorialInters() {
        return
                LdoD.getInstance().getFragmentsSet()
                        .stream()
                        .flatMap(fragment -> fragment.getFragmentInterSet().stream())
                        .filter(fragInter -> fragInter.getSourceType().equals(Edition.EditionType.AUTHORIAL))
                        .map(fragInter -> ((SourceInter) fragInter).getSource())
                        .filter(source -> source.getType().equals(Source.SourceType.MANUSCRIPT))
                        .map(source -> ((ManuscriptSource) source))
                        .filter(source -> source.getLdoDDate() != null);
    }

    private AdvancedManDto getManuscriptDates() {
        List<Integer> manuscriptDates = getAuthorialInters()
                .filter(source -> !source.getHandNoteSet().isEmpty())
                .map(source -> source.getLdoDDate().getDate().getYear())
                .sorted()
                .collect(Collectors.toList());
        return manuscriptDates.isEmpty()
                ? null
                : new AdvancedManDto(manuscriptDates.get(0).toString(), manuscriptDates.get(manuscriptDates.size() - 1).toString());
    }

    private AdvancedTypeDto getTypescriptDates() {
        List<Integer> typescriptDates = getAuthorialInters()
                .filter(source -> !source.getTypeNoteSet().isEmpty())
                .map(source -> source.getLdoDDate().getDate().getYear())
                .sorted()
                .collect(Collectors.toList());
        return typescriptDates.isEmpty()
                ? null
                : new AdvancedTypeDto(typescriptDates.get(0).toString(), typescriptDates.get(typescriptDates.size() - 1).toString());
    }

    private Stream<AdvancedVeDto> getPubVirtualEditions() {
        return LdoD.getInstance().getVirtualEditionsSet()
                .stream()
                .filter(Edition_Base::getPub)
                .map(AdvancedVeDto::new);
    }

    private List<AdvancedVeDto> getVirtualEditions() {
        return LdoDUser.getAuthenticatedUser() != null
                ? Stream.concat(
                        LdoD.getInstance().getVirtualEditions4User(LdoDUser.getAuthenticatedUser().getUsername())
                                .stream()
                                .map(AdvancedVeDto::new),
                        getPubVirtualEditions())
                .collect(Collectors.toList())
                : getPubVirtualEditions().collect(Collectors.toList());

    }

    public AdvancedSearchDto getAdvancedSearch() {
        return new AdvancedSearchDto(
                this.getEditions(),
                this.getHeteronyms(),
                this.getPublicationsDate(),
                this.getDates(),
                this.getManuscriptDates(),
                this.getTypescriptDates(),
                this.getVirtualEditions()
        );
    }

    private Integer getIndexOfSearchOption(SearchOption[] searchOptions, SearchOption option) {
        return Arrays.asList(searchOptions).indexOf(option);

    }


    public List<SearchFragInterDto> performAdvSearch(Search search) {
        Map<Fragment, Map<FragInter, List<SearchOption>>> result = search.search();
        SearchOption[] options = search.getSearchOptions();

        return search.search()
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().entrySet().stream())
                .map(subEntry -> new SearchFragInterDto(subEntry.getKey(), subEntry.getValue().stream().map(opt -> getIndexOfSearchOption(options, opt)).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
