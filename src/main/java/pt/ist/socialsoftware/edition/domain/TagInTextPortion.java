package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class TagInTextPortion extends TagInTextPortion_Base {

	public TagInTextPortion init(Category category, Annotation annotation) {
		super.init();

		setType(TagType.TEXTPORTION);
		setFragInter(annotation.getFragInter());
		setCategory(category);
		addAnnotation(annotation);

		return this;
	}

	@Override
	public void remove() {
		for (Annotation annotation : getAnnotationSet()) {
			removeAnnotation(annotation);
		}

		super.remove();
	}

	public void removeThisAnnotation(Annotation annotation) {
		if (getAnnotationSet().size() == 1) {
			System.out.println("getAnnotationSet().size() == 1");
			remove();
		} else {
			removeAnnotation(annotation);
		}

	}

	@Override
	public int getWeight() {
		return getAnnotationSet().size();
	}

	@Override
	public Set<LdoDUser> getContributorSet() {
		Set<LdoDUser> contributors = new HashSet<LdoDUser>();
		for (Annotation annotation : getAnnotationSet()) {
			contributors.add(annotation.getUser());
		}

		return contributors;
	}

}
