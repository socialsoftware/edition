package pt.ist.socialsoftware.edition.notification.dtos.virtual;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.notification.endpoint.ServiceEndpoints.VIRTUAL_SERVICE_URL;

public class TaxonomyDto {

	private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl(VIRTUAL_SERVICE_URL);

	private boolean openManagement;
	private boolean openVocabulary;
	private boolean openAnnotation;
	private boolean hasCategories;

	private String editionAcronym;
	private String externalId;

	public TaxonomyDto() {
	}

	public String getExternalId() {
		return externalId;
	}

	public boolean isOpenManagement() {
		return this.openManagement;
	}

	public void setOpenManagement(boolean openManagement) {
		this.openManagement = openManagement;
	}

	public boolean isOpenVocabulary() {
		return this.openVocabulary;
	}

	public void setOpenVocabulary(boolean openVocabulary) {
		this.openVocabulary = openVocabulary;
	}

	public boolean isOpenAnnotation() {
		return this.openAnnotation;
	}

	public void setOpenAnnotation(boolean openAnnotation) {
		this.openAnnotation = openAnnotation;
	}

	public boolean getHasCategories() {
		return this.hasCategories;
	}

	public void setHasCategories(boolean hasCategories) {
		this.hasCategories = hasCategories;
	}

	public boolean isHasCategories() {
		return hasCategories;
	}

	public String getEditionAcronym() {
		return editionAcronym;
	}

	public void setEditionAcronym(String editionAcronym) {
		this.editionAcronym = editionAcronym;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@JsonIgnore
	public VirtualEditionDto getEdition() {
		return webClientVirtual.build()
				.get()
				.uri("/virtualEdition/" + this.editionAcronym)
				.retrieve()
				.bodyToMono(VirtualEditionDto.class)
				.block();
		//		return this.virtualProvidesInterface.getVirtualEdition(this.editionAcronym);
	}

	@JsonIgnore
	public Set<CategoryDto> getCategoriesSet() {
		return webClientVirtual.build()
				.get()
				.uri("/virtualEdition/" + this.editionAcronym + "/categoriesFromTaxonomy")
				.retrieve()
				.bodyToFlux(CategoryDto.class)
				.toStream()
				.collect(Collectors.toSet());
		//		return this.virtualProvidesInterface.getCategoriesFromTaxonomy(editionAcronym);
	}

	@JsonIgnore
	public void edit(boolean management, boolean vocabulary, boolean annotation) {
		webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
					.path("/taxonomy/" + editionAcronym + "/edit")
					.queryParam("management", management)
					.queryParam("vocabulary", vocabulary)
					.queryParam("annotation", annotation)
				.build())
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		//		this.virtualProvidesInterface.editTaxonomy(this.editionAcronym, management, vocabulary, annotation);
	}

	@JsonIgnore
	public void createGeneratedCategories(TopicListDto topicList) {
		webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
					.path("/createGeneratedCategories")
					.queryParam("editionAcronym", editionAcronym)
				.build())
				.body(BodyInserters.fromValue(topicList))
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		//		this.virtualProvidesInterface.createGeneratedCategories(this.editionAcronym, topicList);
	}

	@JsonIgnore
	public void removeTaxonomy() {
		webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
					.path("/removeTaxonomy")
					.queryParam("editionAcronym", this.editionAcronym)
					.build())
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		//		this.virtualProvidesInterface.removeTaxonomy(this.editionAcronym);
	}

	@JsonIgnore
	public void createCategory(String name) {
		webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
					.path("/createCategory")
					.queryParam("name", name)
					.queryParam("editionAcronym", editionAcronym)
				.build())
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		//		this.virtualProvidesInterface.createCategory(this.editionAcronym, name);
	}

	@JsonIgnore
	public CategoryDto mergeCategories(List<CategoryDto> categories) {
		return webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
						.path("/mergeCategories")
						.queryParam("editionAcronym", editionAcronym)
						.build())
				.body(BodyInserters.fromValue(categories))
				.retrieve()
				.bodyToMono(CategoryDto.class)
				.block();

		//		return this.virtualProvidesInterface.mergeCategories(this.editionAcronym, categories);
	}

	@JsonIgnore
	public void deleteCategories(List<CategoryDto> categories) {
		webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
					.path("/deleteCategories")
					.queryParam("editionAcronym", editionAcronym)
					.build())
				.body(BodyInserters.fromValue(categories))
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		//		this.virtualProvidesInterface.deleteCategories(this.editionAcronym, categories);
	}

	@JsonIgnore
	public CategoryDto extractCategory(String externalId, String[] interIds) {
		return webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
						.path("/extractCategories")
						.queryParam("externalId", externalId)
						.queryParam("editionAcronym", editionAcronym)
						.build())
				.body(BodyInserters.fromValue(interIds))
				.retrieve()
				.bodyToMono(CategoryDto.class)
				.block();
		//		return this.virtualProvidesInterface.extractCategories(this.editionAcronym, externalId, interIds);
	}

	@JsonIgnore
	public List<VirtualEditionDto> getUsedIn() {
		return webClientVirtual.build()
				.get()
				.uri("/taxonomy/" + editionAcronym + "/taxonomyUsedIn")
				.retrieve()
				.bodyToFlux(VirtualEditionDto.class)
				.collectList()
				.block();
		//		return this.virtualProvidesInterface.getTaxonomyUsedIn(this.editionAcronym);
	}

	@JsonIgnore
	public boolean canManipulateTaxonomy(String username) {
		return webClientVirtual.build()
				.get()
				.uri(uriBuilder -> uriBuilder
					.path("/virtualEdition/" + editionAcronym + "/canManipulateTaxonomy")
					.queryParam("username", username)
					.build())
				.retrieve()
				.bodyToMono(Boolean.class)
				.blockOptional()
				.orElse(false);
		//		return this.virtualProvidesInterface.canManipulateTaxonomy(this.editionAcronym, username);
	}

	@JsonIgnore
	public List<CategoryDto> getSortedCategories() {
		return getCategoriesSet().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
				.collect(Collectors.toList());
	}

	@JsonIgnore
	public CategoryDto createTestCategory(String name) {
		return webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
						.path("/createTestCategory")
						.queryParam("editionAcronym", editionAcronym)
						.queryParam("name", name)
						.build())
				.retrieve()
				.bodyToMono(CategoryDto.class)
				.block();
	}
}
