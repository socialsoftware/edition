package pt.ist.socialsoftware.edition.ldod.bff.virtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.annotations.AnnotationDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.*;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment.VirtualFragmentInterCompareDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment.VirtualFragmentInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment.VirtualFragmentNavBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment.VirtualFragmentNavDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VirtualFragmentService {

    @Autowired
    private VirtualTaxonomyService taxonomyService;

    public VirtualFragmentInterDto dissociate(String fragInterId, String categoryId) {
        VirtualEditionInter inter = taxonomyService.checkVeInterNotNull(fragInterId);
        Category category = taxonomyService.checkCatNotNull(categoryId);
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        inter.dissociate(user, category);
        return getFragmentInter(inter.getFragment().getXmlId(), inter.getUrlId());
    }

    public VirtualFragmentInterDto associate(String fragInterId, Set<String> categoriesNames) {
        VirtualEditionInter inter = taxonomyService.checkVeInterNotNull(fragInterId);
        if (!categoriesNames.isEmpty()) inter.associate(LdoDUser.getAuthenticatedUser(), categoriesNames);
        return getFragmentInter(inter.getFragment().getXmlId(), inter.getUrlId());
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

    public List<VirtualFragmentNavDto> getFragmentInters(String xmlId, VirtualFragmentNavBodyDto body) {
        Fragment frag = LdoD.getInstance().getFragmentByXmlId(xmlId);

        FragInter inter = body.getCurrentInterId() != null
                ? FenixFramework.getDomainObject(body.getCurrentInterId())
                : body.getUrlId() != null ? frag.getFragInterByUrlId(body.getUrlId())
                : null;

        Collection<String> acronyms = LdoDUser.getAuthenticatedUser() != null
                ? LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet().stream().map(Edition_Base::getAcronym).collect(Collectors.toSet())
                : new HashSet<>(body.getVeIds());
        acronyms.add(LdoD.getInstance().getArchiveEdition().getAcronym());
        if(inter != null) acronyms.add(inter.getEdition().getAcronym());

        return acronyms
                .stream()
                .sorted(Comparator.comparing(acr -> acr.equals(LdoD.getInstance().getArchiveEdition().getAcronym())))
                .sorted()
                .map(acr -> VirtualFragmentNavDto.VirtualFragmentNavDtoBuilder
                        .aVirtualFragmentNavDto(LdoD.getInstance().getVirtualEdition(acr), inter)
                        .inters(LdoD.getInstance().getVirtualEdition(acr).getSortedInter4VirtualFrag(frag)
                                .stream()
                                .map(this::getVeInterDtoWithPrevNext)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


    public VirtualFragmentInterDto getFragmentInter(String xmlId, String urlId) {
        VirtualEditionInter inter = (VirtualEditionInter) LdoD.getInstance().getFragmentByXmlId(xmlId).getFragInterByUrlId(urlId);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
        writer.write(false);
        Taxonomy taxonomy = inter.getVirtualEdition().getTaxonomy();

        return VirtualFragmentInterDto.VirtualFragmentInterDtoBuilder.aVirtualFragmentInterDto()
                .inter(VirtualEditionInterDto.VirtualEditionInterDtoBuilder.aVirtualEditionInterDto(inter)
                        .usesEditionReference(inter.getListUsed().get(0).getEdition().getReference())
                        .usesReference(inter.getListUsed().get(0).getReference())
                        .notAssignedCategories(inter.getNonAssignedCategories(LdoDUser.getAuthenticatedUser())
                                .stream()
                                .map(cat -> CategoryDto.CategoryDtoBuilder.aCategoryDto(cat)
                                        .name(cat.getNameInEditionContext(inter.getVirtualEdition()))
                                        .build())
                                .collect(Collectors.toList()))
                        .categories(inter.getAssignedCategories()
                                .stream()
                                .map(cat -> CategoryDto.CategoryDtoBuilder.aCategoryDto(cat)
                                        .name(cat.getNameInEditionContext(inter.getVirtualEdition()))
                                        .users(inter.getContributorSet(cat)
                                                .stream()
                                                .map(user -> VeUserDto.VeUserDtoBuilder.aVeUserDto()
                                                        .username(user.getUsername())
                                                        .firstname(user.getFirstName())
                                                        .lastname(user.getLastName())
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .canBeDissociated(inter.getContributorSet(cat).contains(LdoDUser.getAuthenticatedUser()))
                                        .build())
                                .collect(Collectors.toList()))
                        .transcription(writer.getTranscription())
                        .build())
                .taxonomy(TaxonomyDto.TaxonomyDtoBuilder.aTaxonomyDto()
                        .openVocab(taxonomy.getOpenVocabulary())
                        .canManipulateAnn(taxonomy.canManipulateAnnotation(LdoDUser.getAuthenticatedUser()))
                        .externalId(taxonomy.getExternalId())
                        .build())
                .build();
    }

    public List<VirtualFragmentInterCompareDto> getVirtualFragmentInters(String xmlId, List<String> ids) {
        return ids
                .stream()
                .map(id -> (VirtualEditionInter) FenixFramework.getDomainObject(id))
                .map(inter -> new VirtualFragmentInterCompareDto(
                        inter.getEdition().getAcronym(),
                        inter.getTagsCompleteInter().stream().map(TagDto::new).collect(Collectors.toList()),
                        inter.getAllDepthAnnotations().stream().map(ann -> new AnnotationDto(ann, LdoDUser.getAuthenticatedUser())).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    protected Object checkObjectNotNull(String externalId) {
        Object obj = FenixFramework.getDomainObject(externalId);
        if (obj == null)
            throw new LdoDException(String.format(Message.ELEMENT_NOT_FOUND.getLabel(), externalId));
        return obj;
    }

    public List<VirtualFragmentNavDto> addInterToVe(String xmlId, String veId, String interId, VirtualFragmentNavBodyDto body) {

        VirtualEdition ve = (VirtualEdition) checkObjectNotNull(veId);
        FragInter inter = (FragInter) checkObjectNotNull(interId);

        VirtualEditionInter addInter = ve.createVirtualEditionInter(inter,
                ve.getMaxFragNumber() + 1);

        if (addInter == null) throw new LdoDException(Message.OPERATION_NOT_AUTHORIZED.getLabel());

        return getFragmentInters(xmlId, body);

    }
}
