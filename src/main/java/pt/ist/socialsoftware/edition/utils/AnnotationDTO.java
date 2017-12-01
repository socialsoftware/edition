package pt.ist.socialsoftware.edition.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import pt.ist.socialsoftware.edition.domain.Annotation;
import pt.ist.socialsoftware.edition.domain.Range;
import pt.ist.socialsoftware.edition.domain.Tag;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private List<RangeJson> ranges;
	private String quote;
	private String text;
	private List<String> tags;
	private String uri;
	private String user;
	private PermissionDTO permissions;

	public AnnotationDTO() {
	}

	public AnnotationDTO(Annotation annotation) {
		setId(annotation.getExternalId());
		setQuote(StringEscapeUtils.unescapeHtml(annotation.getQuote()));
		setText(StringEscapeUtils.unescapeHtml(annotation.getText()));
		setUri(annotation.getVirtualEditionInter().getExternalId());

		this.tags = new ArrayList<>();
		for (Tag tag : annotation.getTagSet()) {
			this.tags.add(
					tag.getCategory().getNameInEditionContext(annotation.getVirtualEditionInter().getVirtualEdition()));
		}

		this.ranges = new ArrayList<>();
		for (Range range : annotation.getRangeSet()) {
			this.ranges.add(new RangeJson(range));
		}

		setUser(annotation.getUser().getUsername());

		setPermissions(
				new PermissionDTO(annotation.getVirtualEditionInter().getVirtualEdition(), annotation.getUser()));
	}

	public List<RangeJson> getRanges() {
		return this.ranges;
	}

	public void setRanges(List<RangeJson> range) {
		this.ranges = range;
	}

	public String getQuote() {
		return this.quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getTags() {
		return this.tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public PermissionDTO getPermissions() {
		return this.permissions;
	}

	public void setPermissions(PermissionDTO permissions) {
		this.permissions = permissions;
	}

}
