package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

import java.util.List;

public class VirtualEditionInterDto extends FragInterDto {

    private List<FragInterDto> usedList;

    private List<String> categories;

    private List<VeUserDto> users;

    private VirtualEditionInterDto(){};

    public VirtualEditionInterDto(FragInter inter) {
        super(inter);
        setShortName(inter.getShortName());
        setTitle(inter.getTitle());
    }

    public List<VeUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<VeUserDto> users) {
        this.users = users;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<FragInterDto> getUsedList() {
        return usedList;
    }


    public void setUsedList(List<FragInterDto> usedList) {
        this.usedList = usedList;
    }


    public static final class VirtualEditionInterDtoBuilder {
        private final VirtualEditionInterDto virtualEditionInterDto;

        private VirtualEditionInterDtoBuilder(FragInter inter) {
            virtualEditionInterDto = new VirtualEditionInterDto(inter);
        }

        public static VirtualEditionInterDtoBuilder aVirtualEditionInterDto(FragInter inter) {
            return new VirtualEditionInterDtoBuilder(inter);
        }

        public VirtualEditionInterDtoBuilder usedList(List<FragInterDto> usedList) {
            virtualEditionInterDto.setUsedList(usedList);
            return this;
        }

        public VirtualEditionInterDtoBuilder categories(List<String> categories) {
            virtualEditionInterDto.setCategories(categories);
            return this;
        }

        public VirtualEditionInterDtoBuilder users(List<VeUserDto> contributors) {
            virtualEditionInterDto.setUsers(contributors);
            return this;
        }

        public VirtualEditionInterDto build() {
            return virtualEditionInterDto;
        }
    }
}
