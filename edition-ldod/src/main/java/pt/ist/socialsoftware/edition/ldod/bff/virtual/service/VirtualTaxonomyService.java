package pt.ist.socialsoftware.edition.ldod.bff.virtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.CategoryRequestBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.GenerateTopicsDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.TaxonomyDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.ldod.utils.TopicDTO;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirtualTaxonomyService {

    @Autowired
    private VirtualService virtualService;

    public TaxonomyDto getVeTaxonomy(String externalId) {
        return getVeTaxonomy(virtualService.checkVENotNull(externalId).getTaxonomy());
    }

    private TaxonomyDto getVeTaxonomy(Taxonomy taxonomy) {
        return new TaxonomyDto(taxonomy);
    }


    public void createTaxonomyTopics(String externalId, List<TopicDTO> topicList) {
        VirtualEdition ve = virtualService.checkVENotNull(externalId);
        virtualService.checkUserNotNull();
        Taxonomy taxonomy = ve.getTaxonomy();
        taxonomy.createGeneratedCategories(topicList, LdoDUser.getAuthenticatedUser());

    }

    public TopicListDTO generateTaxonomyTopics(String externalId, GenerateTopicsDto generateTopicsDto) {
        VirtualEdition ve = virtualService.checkVENotNull(externalId);
        LdoDUser user = virtualService.checkUserNotNull();
        try {
            return new TopicModeler().generate(
                    user,
                    ve,
                    generateTopicsDto.getNumTopics(),
                    generateTopicsDto.getNumWords(),
                    generateTopicsDto.getThresholdCategories(),
                    generateTopicsDto.getNumIterations());
        } catch (LdoDException | IOException e) {
            throw new LdoDException(String.format(Message.FRAGMENT_NOT_ASSOCIATED_OR_CORPUS_NOT_LOADED.getLabel(), ve.getAcronym()));
        }
    }

    public TaxonomyDto deleteTaxonomy(String taxExternalId) {
        Taxonomy taxonomy = checkTaxNotNull(taxExternalId);
        VirtualEdition ve = taxonomy.getEdition();
        taxonomy.remove();
        ve.setTaxonomy(new Taxonomy());
        return getVeTaxonomy(ve.getExternalId());
    }

    private Taxonomy checkTaxNotNull(String externalId) {
        Taxonomy t = FenixFramework.getDomainObject(externalId);
        if (t == null)
            throw new LdoDException(Message.TAXONOMY_NOT_FOUND.getLabel());
        return t;
    }

    protected Category checkCatNotNull(String externalId) {
        Category c = FenixFramework.getDomainObject(externalId);
        if (c == null)
            throw new LdoDException(Message.TAXONOMY_NOT_FOUND.getLabel());
        return c;
    }

    protected VirtualEditionInter checkVeInterNotNull(String externalId) {
        VirtualEditionInter veInter = FenixFramework.getDomainObject(externalId);
        if (veInter == null)
            throw new LdoDException(Message.VIRTUAL_INTER_NOT_FOUND.getLabel());
        return veInter;
    }

    public TaxonomyDto createCategory(String veExternalId, CategoryRequestBodyDto categoryDto) {
        VirtualEdition ve = virtualService.checkVENotNull(veExternalId);
        Taxonomy taxonomy = ve.getTaxonomy();
        try {
            taxonomy.createCategory(categoryDto.getName());
        } catch (LdoDDuplicateNameException ex) {
            throw new LdoDException(String.format(Message.DUPLICATE_CATEGORY_NAME.getLabel(), categoryDto.getName()));
        }
        return getVeTaxonomy(taxonomy);
    }

    public TaxonomyDto updateCategory(String categoryId, CategoryDto categoryDto) {
        Category category = checkCatNotNull(categoryId);
        try {
            category.setName(categoryDto.getName());
        } catch (LdoDDuplicateNameException ex) {
            throw new LdoDException(String.format(Message.DUPLICATE_CATEGORY_NAME.getLabel(), categoryDto.getName()));
        }
        return getVeTaxonomy(category.getTaxonomy());
    }

    public TaxonomyDto mergeCategories(String taxonomyId, List<String> categories) {

        Taxonomy taxonomy = checkTaxNotNull(taxonomyId);
        if (categories.isEmpty()) return new TaxonomyDto(taxonomy);
        taxonomy.merge(categories.stream().map(this::checkCatNotNull).collect(Collectors.toList()));
        return new TaxonomyDto(taxonomy);
    }

    public TaxonomyDto deleteCategories(String taxonomyId, List<String> categories) {
        Taxonomy taxonomy = checkTaxNotNull(taxonomyId);
        categories.stream().map(this::checkCatNotNull).forEach(Category::remove);
        return getVeTaxonomy(taxonomy);
    }

    public TaxonomyDto extractCategories(String categoryId, List<String> interIds) {
        Category category = checkCatNotNull(categoryId);
        if (!interIds.isEmpty())
            category.getTaxonomy().extract(category, interIds.stream().map(this::checkVeInterNotNull).collect(Collectors.toSet()));
        return getVeTaxonomy(category.getTaxonomy());

    }
}
