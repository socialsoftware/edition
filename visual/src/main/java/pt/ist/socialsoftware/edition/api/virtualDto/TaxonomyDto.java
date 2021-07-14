package pt.ist.socialsoftware.edition.api.virtualDto;


import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxonomyDto {

//	private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://localhost:8083/api");
	private final WebClient.Builder webClientVirtual = WebClient.builder().baseUrl("http://docker-virtual:8083/api");


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

	public void setEditionAcronym(String editionAcronym) {
		this.editionAcronym = editionAcronym;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
