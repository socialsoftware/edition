package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
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
		Tag tag = LdoD.getInstance().getTag(tagName);

		if (tag == null) {
			new Tag(annotation, tagName);
		} else {
			tag.addAnnotation(annotation);
		}
	}

	public Set<FragInter> getFragInterSet() {
		Set<FragInter> inters = new HashSet<FragInter>();

		for (Annotation annotation : getAnnotationSet()) {
			inters.add(annotation.getFragInter());
		}

		return inters;
	}
}
