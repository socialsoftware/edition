package pt.ist.socialsoftware.edition.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Annotation;
import pt.ist.socialsoftware.edition.domain.Range;
import pt.ist.socialsoftware.edition.domain.Tag;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationJson implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private List<RangeJson> ranges;
	private String quote;
	private String text;
	private List<String> tags;
	private String uri;

	public AnnotationJson() {
	}

	public AnnotationJson(Annotation annotation) {
		setId(annotation.getExternalId());
		setQuote(annotation.getQuote());
		setText(annotation.getText());
		setUri(annotation.getFragInter().getExternalId());

		tags = new ArrayList<String>();
		for (Tag tag : annotation.getTagSet()) {
			tags.add(tag.getTag());
		}

		ranges = new ArrayList<RangeJson>();
		for (Range range : annotation.getRangeSet()) {
			ranges.add(new RangeJson(range));
		}
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

}
