package pt.ist.socialsoftware.edition.ldod.bff.virtual.service;

import org.springframework.stereotype.Service;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.*;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VirtualFragmentService {

    private VirtualTaxonomyService taxonomyService;

    public VirtualEditionInterDto dissociate(String fragInterId, String categoryId) {
        VirtualEditionInter inter = taxonomyService.checkVeInterNotNull(fragInterId);
        Category category = taxonomyService.checkCatNotNull(categoryId);
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        inter.dissociate(user, category);
        return new VirtualEditionInterDto(inter);
    }

    public VirtualEditionInterDto associate(String fragInterId, Set<String> categoriesNames) {
        VirtualEditionInter inter = taxonomyService.checkVeInterNotNull(fragInterId);
        if (categoriesNames.isEmpty()) return new VirtualEditionInterDto(inter);
        inter.associate(LdoDUser.getAuthenticatedUser(), categoriesNames);
        return new VirtualEditionInterDto(inter);
    }

    private VirtualEditionInterDto getVeInterDtoWithPrevNext(VirtualEditionInter inter) {
        VirtualEditionInter prev = (VirtualEditionInter) inter.getEdition().getPrevNumberInter(inter, inter.getNumber());
        VirtualEditionInter next = (VirtualEditionInter) inter.getEdition().getNextNumberInter(inter, inter.getNumber());
        return VirtualEditionInterDto.VirtualEditionInterDtoBuilder.aVirtualEditionInterDto(inter)
                .prevXmlId(prev.getFragment().getXmlId())
                .nextXmlId(next.getFragment().getXmlId())
                .prevUrlId(prev.getUrlId())
                .nextUrlId(next.getUrlId())
                .build();
    }

    public Map<String, List<VirtualEditionInterDto>> getFragmentInters(String xmlId, List<String> veAcronyms) {
        Fragment frag = LdoD.getInstance().getFragmentByXmlId(xmlId);
        return veAcronyms
                .stream()
                .map(acr -> LdoD.getInstance().getVirtualEdition(acr))
                .flatMap(ve -> ve.getSortedInter4Frag(frag).stream())
                .collect(Collectors
                        .groupingBy(
                                veInter -> veInter.getEdition().getAcronym(),
                                Collectors.mapping(this::getVeInterDtoWithPrevNext, Collectors.toList()))
                );


    }


    public VirtualFragmentInterDto getFragmentInter(String xmlId, String urlId) {
        VirtualEditionInter inter = (VirtualEditionInter) LdoD.getInstance().getFragmentByXmlId(xmlId).getFragInterByUrlId(urlId);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(false);


        return VirtualFragmentInterDto.VirtualFragmentInterDtoBuilder.aVirtualFragmentInterDto()
                .inters(Collections.singletonList(VirtualEditionInterDto.VirtualEditionInterDtoBuilder.aVirtualEditionInterDto(inter)
                        .usesEditionReference(inter.getListUsed().get(0).getEdition().getReference())
                        .usesReference(inter.getListUsed().get(0).getReference())
                        .categories(inter.getAssignedCategories()
                                .stream()
                                .map(cat -> CategoryDto.CategoryDtoBuilder.aCategoryDto(cat)
                                        .users(inter.getContributorSet(cat)
                                                .stream()
                                                .map(VeUserDto::new)
                                                .collect(Collectors.toList()))
                                        .build())
                                .collect(Collectors.toList()))
                        .transcription(writer.getTranscription())
                        .build()))
                .taxonomies(Stream.of(inter
                                .getVirtualEdition()
                                .getTaxonomy())
                        .map(taxonomy -> TaxonomyDto.TaxonomyDtoBuilder.aTaxonomyDto()
                                .externalId(taxonomy.getExternalId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public List<VirtualInterCompareDto> getVirtualFragmentInters(String xmlId, List<String> ids) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        return ids
                .stream()
                .map(id -> (VirtualEditionInter) FenixFramework.getDomainObject(id))
                .map(inter -> new VirtualInterCompareDto(
                        inter.getEdition().getAcronym(),
                        inter.getTagsCompleteInter().stream().map(TagDto::new).collect(Collectors.toList()),
                        inter.getAllDepthAnnotations().stream().map(AnnotationDto::new).collect(Collectors.toList())))
                .collect(Collectors.toList());


    }
}
