package pt.ist.socialsoftware.edition.notification.dtos.virtual;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class VirtualEditionInterDto {

    private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);


    private String xmlId;

    // cached attributes
    private String externalId;
    private String title;
    private String fragmentXmlId;
    private String urlId;
    private String shortName;
    private String reference;
    private int number;
    private ScholarInterDto lastUsed;
    private VirtualEditionDto virtualEditionDto;



    public VirtualEditionInterDto() {}

    public String getXmlId() {
        return this.xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getExternalId() {
        //return this.virtualProvidesInterface.getVirtualEditionInterExternalId(this.xmlId);
        return this.externalId;
    }

    public String getTitle() {
        //return this.virtualProvidesInterface.getVirtualEditionInterTitle(this.xmlId);
        return this.title;
    }

    public FragScholarInterDto.InterType getType() {
        return FragScholarInterDto.InterType.VIRTUAL;
    }

    public String getFragmentXmlId() {
        //return this.virtualProvidesInterface.getFragmentXmlIdVirtualEditionInter(this.xmlId);
        return this.fragmentXmlId;
    }

    public String getUrlId() {
        //return this.virtualProvidesInterface.getVirtualEditionInterUrlId(this.xmlId);
        return this.urlId;
    }

    public String getShortName() {
        //return this.virtualProvidesInterface.getVirtualEditionInterShortName(this.xmlId);
        return this.shortName;
    }

    @JsonIgnore
    public ScholarInterDto getLastUsed() {
        //return this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
        if (this.lastUsed == null)
            this.lastUsed = webClientVirtual.build()
                    .get()
                    .uri("/virtualEditionInter/" + this.xmlId + "/lastUsed")
                    .retrieve()
                    .bodyToMono(ScholarInterDto.class)
                    .block();

//            this.lastUsed = this.virtualProvidesInterface.getVirtualEditionLastUsedScholarInter(this.xmlId);
        return this.lastUsed;
    }

    @JsonIgnore
    public String getUsesScholarInterId() {
        return getLastUsed().getXmlId();
    }

    public String getReference() {
        //return this.virtualProvidesInterface.getVirtualEditionInterReference(this.xmlId);
        return this.reference;
    }

    public int getNumber() {
        //return this.virtualProvidesInterface.getVirtualEditionInterNumber(this.xmlId);
        return this.number;
    }

    @JsonIgnore
    public VirtualEditionInterDto getNextInter() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + this.xmlId + "/nextInter")
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getNextVirtualInter(this.xmlId);
    }

    @JsonIgnore
    public VirtualEditionInterDto getPrevInter() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + this.xmlId + "/prevInter")
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getPrevVirtualInter(this.xmlId);
    }

    @JsonIgnore
    public VirtualEditionInterDto getUsesInter() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + this.xmlId + "/uses")
                .retrieve()
                .bodyToMono(VirtualEditionInterDto.class)
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterUses(this.xmlId);
    }

    @JsonIgnore
    public List<String> getSortedCategoriesName() {
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + this.xmlId + "/sortedCategoriesName")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getSortedVirtualEditionInterCategoriesName(this.xmlId);
    }

    @JsonIgnore
    public List<CategoryDto> getSortedCategories(VirtualEditionDto virtualEditionDto) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/virtualEdition/"  + virtualEditionDto.getAcronym() + "/sortedVirtualEditionInterCategories")
                .queryParam("xmlId", this.xmlId)
                .build())
                .retrieve()
                .bodyToFlux(CategoryDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getSortedVirtualEditionInterCategoriesFromVirtualEdition(this.xmlId, virtualEditionDto.getAcronym());
    }

    @JsonIgnore
    public List<CategoryDto> getCategoriesUsedInTags(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/categoriesUsedInTags")
                    .queryParam("xmlId", xmlId)
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToFlux(CategoryDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getCategoriesUsedInTags(this.xmlId, username);
    }

    @JsonIgnore
    public List<CategoryDto> getAllDepthCategoriesAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionInter/" + this.xmlId + "/allDepthCategoriesAccessibleByUser")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToFlux(CategoryDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesAccessibleByUser(this.xmlId, username);
    }

    @JsonIgnore
    public Set<TagDto> getAllDepthTagsAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/allDepthTagsAcessibleByUser")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToFlux(TagDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthTagsAccessibleByUser(this.xmlId, username);
    }

    @JsonIgnore
    public Set<TagDto> getTagSet(){
        return webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + this.xmlId + "/allTags")
                .retrieve()
                .bodyToFlux(TagDto.class)
                .toStream()
                .collect(Collectors.toSet());
        //        return this.virtualProvidesInterface.getAllTags(xmlId);
    }

    @JsonIgnore
    public List<CategoryDto> getAllDepthCategoriesUsedByUserInTags(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionInter/" + this.xmlId + "/allDepthCategoriesUsedByUserInTags")
                    .queryParam("username", username)
                    .build())
                .retrieve()
                .bodyToFlux(CategoryDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesUsedByUserInTags(this.xmlId, username);
    }

    @JsonIgnore
    public List<CategoryDto> getAllDepthCategoriesNotUsedInTags(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/allDepthCategoriesNotUsedInTags")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToFlux(CategoryDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesNotUsedInTags(this.xmlId, username);
    }

    @JsonIgnore
    public VirtualEditionDto getVirtualEditionDto() {
        if (virtualEditionDto == null)
             virtualEditionDto =  webClientVirtual.build()
                .get()
                .uri("/virtualEditionInter/" + this.xmlId + "/virtualEdition")
                .retrieve()
                .bodyToMono(VirtualEditionDto.class)
                .block();
        return virtualEditionDto;
        //        return this.virtualProvidesInterface.getVirtualEditionOfVirtualEditionInter(this.xmlId);
    }

    @JsonIgnore
    public List<AnnotationDto> getAllDepthAnnotationsAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                   .path("/virtualEditionInter/" + this.xmlId + "/allDepthAnnotationsAccessibleByUser")
                   .queryParam("username", username)
                   .build())
                .retrieve()
                .bodyToFlux(AnnotationDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getAllDepthAnnotationsAccessibleByUser(this.xmlId, username);
    }

    @JsonIgnore
    public List<TagDto> getAllDepthTagsNotHumanAnnotationAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/allDepthTagsNotHumanAnnotationAccessibleByUser")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToFlux(TagDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getAllDepthTagsNotHumanAnnotationAccessibleByUser(this.xmlId, username);
    }

    @JsonIgnore
    public List<HumanAnnotationDto> getVirtualEditionInterHumanAnnotationsAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/humanAnnotationsAccessibleByUser")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToFlux(HumanAnnotationDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterHumanAnnotationsAccessibleByUser(this.xmlId, username);
    }

    @JsonIgnore
    public List<AwareAnnotationDto> getVirtualEditionInterAwareAnnotationsAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/awareAnnotationsAccessibleByUser")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToFlux(AwareAnnotationDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterAwareAnnotationsAccessibleByUser(this.xmlId, username);
    }

    @JsonIgnore
    public List<VirtualEditionInterDto> getUsesPath() {
        List<VirtualEditionInterDto> usesPath = new ArrayList<>();
        VirtualEditionInterDto uses = getUsesInter();
        while (uses != null) {
            usesPath.add(uses);
            uses = uses.getUsesInter();
        }

        return usesPath;
    }

    @JsonIgnore
    public List<CategoryDto> getAllDepthCategoriesUsedInTags(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/allDepthCategoriesUsedInTags")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToFlux(CategoryDto.class)
                .collectList()
                .block();
        //        return this.virtualProvidesInterface.getVirtualEditionInterAllDepthCategoriesUsedInTags(this.xmlId, username);
    }

    @JsonIgnore
    public FragmentDto getFragmentDto() {
        return getLastUsed().getFragmentDto();
    }

    @JsonIgnore
    public String getAllDepthCategoriesJSON(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/virtualEditionInter/" + this.xmlId + "/allDepthCategoriesJSON")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //        return this.virtualProvidesInterface.getAllDepthCategoriesJSON(this.xmlId, username);
    }

    @JsonIgnore
    public Set<String> getContributorSet(String externalId, String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionInter/" + this.xmlId + "/contributorSet")
                    .queryParam("username", username)
                    .queryParam("externalId", externalId)
                    .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
                .block();
        //        return this.virtualProvidesInterface.getContributorSetFromVirtualEditionInter(externalId, this.xmlId, username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VirtualEditionInterDto other = (VirtualEditionInterDto) o;
        return this.xmlId.equals(other.getXmlId());
    }

    @Override
    public int hashCode() {
        return this.xmlId.hashCode();
    }

    @JsonIgnore
    public void dissociate(String authenticatedUser, String category) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/dissociateVirtualEditionInterCategory")
                    .queryParam("xmlId", xmlId)
                    .queryParam("username", authenticatedUser)
                    .queryParam("categoryExternalId", category)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.dissociateVirtualEditionInterCategory(this.xmlId, authenticatedUser, category);
    }

    @JsonIgnore
    public void associate(String authenticatedUser, Set<String> collect) {
        webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/associateVirtualEditionInterCategoriesByExternalId")
                        .queryParam("externalId", externalId)
                        .queryParam("username", authenticatedUser)
                        .queryParam("categories", collect)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        //        this.virtualProvidesInterface.associateVirtualEditionInterCategoriesbyExternalId(this.externalId, authenticatedUser, collect);
    }

    @JsonIgnore
    public HumanAnnotationDto createHumanAnnotation(String quote, String text, String username, List<RangeJson> ranges, List<String> tags) {
        return webClientVirtual.build()
                .post()
                .uri(uriBuilder -> uriBuilder
                    .path("/createHumanAnnotation")
                    .queryParam("xmlId", xmlId)
                    .queryParam("quote", quote)
                    .queryParam("text", text)
                    .queryParam("user", username)
                    .queryParam("tags", tags)
                    .build())
                .bodyValue(ranges)
                .retrieve()
                .bodyToMono(HumanAnnotationDto.class)
                .block();
        //       return this.virtualProvidesInterface.createHumanAnnotation(this.xmlId, quote, text, username, ranges, tags);
    }

    @JsonIgnore
    public List<HumanAnnotationDto> getAllDepthHumanAnnotationsAccessibleByUser(String username) {
        return webClientVirtual.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/virtualEditionInter/" + xmlId + "/allDepthHumanAnnotationsAccessibleByUser")
                    .queryParam("username", username)
                .build())
                .retrieve()
                .bodyToFlux(HumanAnnotationDto.class)
                .collectList()
                .block();
    }

    @JsonIgnore
    public void removeInter() {
        webClientVirtual.build()
                .post()
                .uri("/virtualEditionInter/" + externalId + "/remove")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLastUsed(ScholarInterDto lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void setFragmentXmlId(String fragmentXmlId) {
        this.fragmentXmlId = fragmentXmlId;
    }
}
