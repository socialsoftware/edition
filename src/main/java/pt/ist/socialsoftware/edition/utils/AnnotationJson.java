package pt.ist.socialsoftware.edition.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Annotation;
import pt.ist.socialsoftware.edition.domain.Range;
import pt.ist.socialsoftware.edition.domain.TagInTextPortion;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationJson implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private List<RangeJson> ranges;
	private String quote;
	private String text;
	private List<String> tags;
	private String uri;
	private String user;
	private PermissionJson permissions;

	public AnnotationJson() {
	}

	public AnnotationJson(Annotation annotation) {
		setId(annotation.getExternalId());
		setQuote(annotation.getQuote());
		setText(annotation.getText());
		setUri(annotation.getFragInter().getExternalId());

		tags = new ArrayList<String>();
		for (TagInTextPortion tag : annotation.getTagInTextPortionSet()) {
			tags.add(tag.getActiveCategory().getName());
		}

		ranges = new ArrayList<RangeJson>();
		for (Range range : annotation.getRangeSet()) {
			ranges.add(new RangeJson(range));
		}

		setUser(annotation.getUser().getUsername());

		setPermissions(new PermissionJson(annotation.getFragInter()
				.getEdition(), annotation.getUser()));
	}

	public List<RangeJson> getRanges() {
		return ranges;
	}

	public void setRanges(List<RangeJson> range) {
		this.ranges = range;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public PermissionJson getPermissions() {
		return permissions;
	}

	public void setPermissions(PermissionJson permissions) {
		this.permissions = permissions;
	}

}
