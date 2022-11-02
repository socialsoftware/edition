package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private String name;
    private String externalId;
    private String veAcronym;
    private String veTitle;
    private List<VeUserDto> users;
    private List<VirtualEditionDto> editions;
    private List<FragInterDto> veInters;

    private boolean canBeDissociated;

    public CategoryDto(){};

    public CategoryDto(Category category) {
        setName(category.getName());
        setExternalId(category.getExternalId());
        setVeTitle(StringEscapeUtils.unescapeHtml(category.getTaxonomy().getEdition().getTitle()));
        setVeAcronym(category.getTaxonomy().getEdition().getAcronym());

    }

    public boolean isCanBeDissociated() {
        return canBeDissociated;
    }

    public void setCanBeDissociated(boolean canBeDissociated) {
        this.canBeDissociated = canBeDissociated;
    }

    public List<VeUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<VeUserDto> users) {
        this.users = users;
    }

    public List<VirtualEditionDto> getEditions() {
        return editions;
    }

    public void setEditions(List<VirtualEditionDto> editions) {
        this.editions = editions;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getVeAcronym() {
        return veAcronym;
    }

    public void setVeAcronym(String veAcronym) {
        this.veAcronym = veAcronym;
    }

    public List<FragInterDto> getVeInters() {
        return veInters;
    }

    public void setVeInters(List<FragInterDto> veInters) {
        this.veInters = veInters;
    }


    public static final class CategoryDtoBuilder {
        private CategoryDto categoryDto;

        private CategoryDtoBuilder(Category category) {
            categoryDto = new CategoryDto(category);
        }

        public static CategoryDtoBuilder aCategoryDto(Category category) {
            return new CategoryDtoBuilder(category);
        }

        public CategoryDtoBuilder name(String name) {
            categoryDto.setName(name);
            return this;
        }

        public CategoryDtoBuilder veInters(List<FragInterDto> inters) {
            categoryDto.setVeInters(inters);
            return this;
        }

        public CategoryDtoBuilder users(List<VeUserDto> users) {
            categoryDto.setUsers(users);
            return this;
        }

        public CategoryDtoBuilder canBeDissociated(boolean canBeDissociated){
            categoryDto.setCanBeDissociated(canBeDissociated);
            return this;
        }

        public CategoryDtoBuilder editions(Category category) {
            categoryDto.setEditions(category.getSortedEditions()
                    .stream()
                    .map(VirtualEditionDto::new)
                    .collect(Collectors.toList()));
            return this;
        }

        public CategoryDto build() {
            return categoryDto;
        }
    }
}
