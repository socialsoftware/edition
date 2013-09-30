package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public class Tag extends Tag_Base {

	public Tag(Annotation annotation, String tag) {
		addAnnotation(annotation);
		setTag(tag);
	}

	public void remove() {
		for (Annotation annotation : getAnnotationSet()) {
			removeAnnotation(annotation);
		}

		deleteDomainObject();
	}

	public static void create(Annotation annotation, String tagName) {
		Tag tag = Tag.get(annotation, tagName);

		if (tag == null) {
			new Tag(annotation, tagName);
		} else {
			tag.addAnnotation(annotation);
		}

	}

	private static Tag get(Annotation annotation, String tagName) {
		Set<Tag> tags = annotation.getFragInter().getEdition().getTagSet();
		for (Tag tag : tags) {
			if (tag.getTag().equals(tagName)) {
				return tag;
			}
		}

		return null;
	}
}
