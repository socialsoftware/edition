//package pt.ist.socialsoftware.edition.recommendation.api.virtualDto;
//
//import org.springframework.web.reactive.function.client.WebClient;
//import pt.ist.socialsoftware.edition.recommendation.api.userDto.UserDto;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CategoryDto {
//
//    private String externalId;
//    private String acronym;
//    private String urlId;
//    private String nameInEdition;
//    private String name;
//    private List<UserDto> users;
//    private boolean hasTags;
//
//    public CategoryDto() {
//    }
//
//    public String getExternalId() {
//        return this.externalId;
//    }
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//
//    public String getAcronym() {
//        return this.acronym;
//    }
//
//    public void setAcronym(String acronym) {
//        this.acronym = acronym;
//    }
//
//    public String getUrlId() {
//        return this.urlId;
//    }
//
//    public void setUrlId(String urlId) {
//        this.urlId = urlId;
//    }
//
//    public String getNameInEdition() {
//        return this.nameInEdition;
//    }
//
//    public void setNameInEdition(String name) {
//        this.nameInEdition = name;
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public List<UserDto> getUsers() {
//        return this.users;
//    }
//
//    public void setUsers(List<UserDto> users) {
//        this.users = users;
//    }
//
//    public List<String> getUsernames() {
//        return getUsers().stream().map(userDto -> userDto.getUsername()).collect(Collectors.toList());
//    }
//
//
//    public boolean isHasTags() { return this.hasTags; }
//
//
//    public void setHasTags(boolean hasTags) {
//        this.hasTags = hasTags;
//    }
//}
