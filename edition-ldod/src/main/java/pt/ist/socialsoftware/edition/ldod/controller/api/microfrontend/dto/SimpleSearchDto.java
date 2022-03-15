package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SimpleSearchDto {

    private int fragCount;
    private int interCount;
    private List<FragInterDto> listFragments;
    private List<InterDto> fragments;
    private String searchTerm;
    private String searchType;
    private String searchSource;

    public SimpleSearchDto() {
    }

    public SimpleSearchDto(Map<Fragment, List<FragInter>> results) {
        setFragCount(results.size());
        setInterCount(results.values().stream().reduce(0, (prev, curr) -> prev + curr.size(), Integer::sum));
        setFragments(results.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream().distinct()
                        .map(inter -> new InterDto.InterDtoBuilder()
                                .xmlId(entry.getKey().getXmlId())
                                .urlId(inter.getUrlId())
                                .title(inter.getTitle())
                                .interExternalId(inter.getExternalId())
                                .shortName(inter.getShortName())
                                .sourceType(inter)
                                .editor(inter)
                                .fragTitle(entry.getKey().getTitle())
                                .fragExternalId(entry.getKey().getExternalId())
                                .build()))
                .collect(Collectors.toList()));
    }

    public SimpleSearchDto(Map<Fragment, List<FragInter>> results, int interCount) {
        this.setFragCount(results.size());
        this.setInterCount(interCount);
        List<FragInterDto> listFragmentAux = new ArrayList<FragInterDto>();
        for (Map.Entry<Fragment, List<FragInter>> entry : results.entrySet()) {
            for (FragInter frag : entry.getValue()) {
                listFragmentAux.add(new FragInterDto(frag, entry.getKey()));
            }
        }
        this.setListFragments(listFragmentAux);
    }

    public int getFragCount() {
        return fragCount;
    }

    public void setFragCount(int fragCount) {
        this.fragCount = fragCount;
    }

    public int getInterCount() {
        return interCount;
    }

    public void setInterCount(int interCount) {
        this.interCount = interCount;
    }


    public List<FragInterDto> getListFragments() {
        return listFragments;
    }

    public void setListFragments(List<FragInterDto> listFragments) {
        this.listFragments = listFragments;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getSearchSource() {
        return searchSource;
    }

    public List<InterDto> getFragments() {
        return fragments;
    }

    public void setFragments(List<InterDto> fragments) {
        this.fragments = fragments;
    }
}
