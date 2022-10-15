package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

import java.util.List;

public class VirtualEditionInterDto extends FragInterDto {

    private List<FragInterDto> usedList;

    public VirtualEditionInterDto(){};

    public VirtualEditionInterDto(FragInter inter) {
        super(inter);
        setShortName(inter.getShortName());
        setTitle(inter.getTitle());
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


        public VirtualEditionInterDto build() {
            return virtualEditionInterDto;
        }
    }
}
