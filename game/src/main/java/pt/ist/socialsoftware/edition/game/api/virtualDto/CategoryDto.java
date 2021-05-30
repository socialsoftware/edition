package pt.ist.socialsoftware.edition.game.api.virtualDto;

import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.game.api.userDto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
//    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


    private String externalId;
    private String acronym;
    private String urlId;
    private String nameInEdition;
    private String name;
    private List<UserDto> users;
    private boolean hasTags;

    public CategoryDto() {
    }

    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getUrlId() {
        return this.urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getNameInEdition() {
        return this.nameInEdition;
    }

    public void setNameInEdition(String name) {
        this.nameInEdition = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDto> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public List<String> getUsernames() {
        return getUsers().stream().map(userDto -> userDto.getUsername()).collect(Collectors.toList());
    }

    public TaxonomyDto getTaxonomy() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEdition/" + this.acronym + "/taxonomy")
                .retrieve()
                .bodyToMono(TaxonomyDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionTaxonomy(this.acronym);
    }

    public List<VirtualEditionInterDto> getSortedInters(VirtualEditionDto virtualEditionDto) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/category/" + externalId + "/sortedInters")
                    .queryParam("acronym", virtualEditionDto.getAcronym())
                    .build())
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedInterFromCategoriesTag(externalId, virtualEditionDto.getAcronym());
    }

    public List<VirtualEditionInterDto> getSortedInters() {
        return webClientVirtual.build()
                .get()
                .uri("/category/" + externalId + "/sortedInters")
                .retrieve()
                .bodyToFlux(VirtualEditionInterDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedInterFromCategoriesTag(externalId);
    }


    public List<String> getSortedUsers() {
        return webClientVirtual.build()
                .get()
                .uri("/category/" + externalId + "/sortedUsers")
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedUsersFromCategoriesTag(externalId);
    }

    public List<VirtualEditionDto> getSortedEditions() {
        return webClientVirtual.build()
                .get()
                .uri("/category/" + externalId + "/sortedEditions")
                .retrieve()
                .bodyToFlux(VirtualEditionDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedEditionsFromCategoriesTag(externalId);
    }

    public boolean isHasTags() { return this.hasTags; }

    public void updateName(String name) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/category/" + this.externalId + "/updateName")
                    .queryParam("name", name)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        //        this.virtualProvidesInterface.updateCategoryNameByExternalId(this.externalId, name);
    }

    public void removeCategory(String externalId) {
        webClientVirtual.build()
                .post()
                .uri("/category/" + this.externalId + "/remove")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.removeCategory(externalId);
    }
}
