package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;

import java.util.List;

public class VirtualEditionInterDto extends FragInterDto {

    private List<FragInterDto> usedList;

    private String usesEditionReference;

    private String usesReference;

    private List<?> categories;

    private List<CategoryDto> notAssignedCategories;

    private List<VeUserDto> users;
    private String nextXmlId;
    private String prevXmlId;
    private String nextUrlId;
    private String prevUrlId;
    private String transcription;
    private String interTitle;

    public VirtualEditionInterDto() {
        super();
    }

    ;

    public VirtualEditionInterDto(FragInter inter) {
        super(inter);
        setShortName(inter.getShortName());
        setTitle(inter.getTitle());
    }

    public List<CategoryDto> getNotAssignedCategories() {
        return notAssignedCategories;
    }

    public void setNotAssignedCategories(List<CategoryDto> notAssignedCategories) {
        this.notAssignedCategories = notAssignedCategories;
    }

    public String getNextXmlId() {
        return nextXmlId;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getUsesEditionReference() {
        return usesEditionReference;
    }

    public void setUsesEditionReference(String usesEditionReference) {
        this.usesEditionReference = usesEditionReference;
    }

    public String getUsesReference() {
        return usesReference;
    }

    public void setUsesReference(String usesReference) {
        this.usesReference = usesReference;
    }

    public void setNextXmlId(String nextXmlId) {
        this.nextXmlId = nextXmlId;
    }

    public String getPrevXmlId() {
        return prevXmlId;
    }

    public void setPrevXmlId(String prevXmlId) {
        this.prevXmlId = prevXmlId;
    }

    public String getNextUrlId() {
        return nextUrlId;
    }

    public void setNextUrlId(String nextUrlId) {
        this.nextUrlId = nextUrlId;
    }

    public String getPrevUrlId() {
        return prevUrlId;
    }

    public void setPrevUrlId(String prevUrlId) {
        this.prevUrlId = prevUrlId;
    }

    public String getInterTitle() {
        return interTitle;
    }

    public void setInterTitle(String interTitle) {
        this.interTitle = interTitle;
    }

    public List<VeUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<VeUserDto> users) {
        this.users = users;
    }

    public List<?> getCategories() {
        return categories;
    }

    public void setCategories(List<?> categories) {
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

        public VirtualEditionInterDtoBuilder categories(List<?> categories) {
            virtualEditionInterDto.setCategories(categories);
            return this;
        }

        public VirtualEditionInterDtoBuilder notAssignedCategories(List<CategoryDto> categories) {
            virtualEditionInterDto.setNotAssignedCategories(categories);
            return this;
        }

        public VirtualEditionInterDtoBuilder users(List<VeUserDto> contributors) {
            virtualEditionInterDto.setUsers(contributors);
            return this;
        }

        public VirtualEditionInterDtoBuilder nextXmlId(String nextXmlId) {
            virtualEditionInterDto.setNextXmlId(nextXmlId);
            return this;
        }

        public VirtualEditionInterDtoBuilder prevXmlId(String prevXmlId) {
            virtualEditionInterDto.setPrevXmlId(prevXmlId);
            return this;
        }

        public VirtualEditionInterDtoBuilder nextUrlId(String nextUrlId) {
            virtualEditionInterDto.setNextUrlId(nextUrlId);
            return this;
        }

        public VirtualEditionInterDtoBuilder prevUrlId(String prevUrlId) {
            virtualEditionInterDto.setPrevUrlId(prevUrlId);
            return this;
        }

        public VirtualEditionInterDtoBuilder usesEditionReference(String ref) {
            virtualEditionInterDto.setUsesEditionReference(ref);
            return this;
        }

        public VirtualEditionInterDtoBuilder usesReference(String ref) {
            virtualEditionInterDto.setUsesReference(ref);
            return this;
        }

        public VirtualEditionInterDtoBuilder transcription(String transcription) {
            virtualEditionInterDto.setTranscription(transcription);
            return this;
        }

        public VirtualEditionInterDto build() {
            return virtualEditionInterDto;
        }
    }
}
