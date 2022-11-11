package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Annotation;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fragment2CriticalDto {
	private String urlId;
	private String title;
	private List<String> categories;
	private List<String> editions;
	String text;
	private List<AnnotationDTO> annotations = new ArrayList<>();

	public Fragment2CriticalDto(VirtualEditionInter inter) {
		this.urlId = inter.getUrlId();
		this.title = inter.getFragment().getTitle();
		this.categories = inter.getCategories().stream().map(c -> c.getName()).sorted().collect(Collectors.toList());
		this.editions = inter.getListUsed().stream().map(FragInter::getShortName).collect(Collectors.toList());
	}

	public Fragment2CriticalDto(VirtualEditionInter inter, String text) {
		this(inter);
		this.text = text;
		for (Annotation annotation : inter.getAllDepthAnnotations()) {
			AnnotationDTO annotationJson = new AnnotationDTO(annotation);
			annotations.add(annotationJson);
		}
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getEditions() {
		return editions;
	}

	public void setEditions(List<String> editions) {
		this.editions = editions;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<AnnotationDTO> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationDTO> annotations) {
		this.annotations = annotations;
	}
}
