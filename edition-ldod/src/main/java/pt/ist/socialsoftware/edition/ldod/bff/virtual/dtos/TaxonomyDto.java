package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.domain.Edition_Base;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;

import java.util.List;
import java.util.stream.Collectors;

public class TaxonomyDto {

    private String externalId;
    private String veExternalId;
    private String veTitle;
    private String veAcronym;

    private List<String> usedIn;
    private List<CategoryDto> categories;

    public TaxonomyDto(Taxonomy taxonomy) {
        setExternalId(taxonomy.getExternalId());
        setVeExternalId(taxonomy.getEdition().getExternalId());
        setVeTitle(StringEscapeUtils.unescapeHtml(taxonomy.getEdition().getTitle()));
        setVeAcronym(taxonomy.getEdition().getAcronym());
        setUsedIn(taxonomy.getUsedIn().stream().map(Edition_Base::getAcronym).collect(Collectors.toList()));
        setCategories(taxonomy.getCategoriesSet().stream().map(cat -> CategoryDto
                        .CategoryDtoBuilder
                        .aCategoryDto(cat)
                        .veInters(cat.getSortedInters()
                                .stream()
                                .map(inter -> VirtualEditionInterDto.VirtualEditionInterDtoBuilder.aVirtualEditionInterDto(inter).build())
                                .collect(Collectors.toList()))
                        .users(cat.getSortedUsers()
                                .stream()
                                .map(user -> VeUserDto.VeUserDtoBuilder.aVeUserDto()
                                        .username(user.getUsername())
                                        .firstname(user.getFirstName())
                                        .lastname(user.getLastName())
                                        .build())
                                .collect(Collectors.toList()))
                        .editions(cat)
                        .build())
                .collect(Collectors.toList()));
    }

    public List<String> getUsedIn() {
        return usedIn;
    }

    public void setUsedIn(List<String> usedIn) {
        this.usedIn = usedIn;
    }

    public String getVeAcronym() {
        return veAcronym;
    }

    public String getVeExternalId() {
        return veExternalId;
    }

    public void setVeExternalId(String veExternalId) {
        this.veExternalId = veExternalId;
    }

    public void setVeAcronym(String veAcronym) {
        this.veAcronym = veAcronym;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getVeTitle() {
        return veTitle;
    }

    public void setVeTitle(String veTitle) {
        this.veTitle = veTitle;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
