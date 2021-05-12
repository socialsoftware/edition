package pt.ist.socialsoftware.edition.recommendation.api.virtualDto;


import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxonomyDto {

	private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");


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

	public VirtualEditionDto getEdition() {
		return webClientVirtual.build()
				.get()
				.uri("/virtualEdition/" + this.editionAcronym)
				.retrieve()
				.bodyToMono(VirtualEditionDto.class)
				.block();
		//		return this.virtualProvidesInterface.getVirtualEdition(this.editionAcronym);
	}

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

	public void createGeneratedCategories(TopicListDTO topicList) {
		webClientVirtual.build()
				.post()
				.uri("/createGeneratedCategories")
				.body(BodyInserters.fromValue(topicList))
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		//		this.virtualProvidesInterface.createGeneratedCategories(this.editionAcronym, topicList);
	}

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

	public CategoryDto extractCategory(String externalId, String[] interIds) {
		return webClientVirtual.build()
				.post()
				.uri(uriBuilder -> uriBuilder
						.path("/extractCategory")
						.queryParam("externalId", externalId)
						.queryParam("editionAcronym", editionAcronym)
						.build())
				.body(BodyInserters.fromValue(interIds))
				.retrieve()
				.bodyToMono(CategoryDto.class)
				.block();
		//		return this.virtualProvidesInterface.extractCategories(this.editionAcronym, externalId, interIds);
	}

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

	public List<CategoryDto> getSortedCategories() {
		return getCategoriesSet().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
				.collect(Collectors.toList());
	}

	public void setEditionAcronym(String editionAcronym) {
		this.editionAcronym = editionAcronym;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
