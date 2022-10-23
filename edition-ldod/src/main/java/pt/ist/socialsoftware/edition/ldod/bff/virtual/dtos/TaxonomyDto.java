package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;

import java.util.List;
import java.util.stream.Collectors;

public class TaxonomyDto {

    private String externalId;
    private String veTitle;
    private String veAcronym;

    private List<CategoryDto> categories;

    public TaxonomyDto(Taxonomy taxonomy) {

        setExternalId(taxonomy.getExternalId());
        setVeTitle(StringEscapeUtils.unescapeHtml(taxonomy.getEdition().getTitle()));
        setVeAcronym(taxonomy.getEdition().getAcronym());
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

    public String getVeAcronym() {
        return veAcronym;
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
