package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class TagInTextPortion extends TagInTextPortion_Base {

	public TagInTextPortion(Category category, Annotation annotation) {
		setFragInter(annotation.getFragInter());
		setCategory(category);
		addAnnotation(annotation);
	}

	@Override
	public void remove() {
		for (Annotation annotation : getAnnotationSet()) {
			removeAnnotation(annotation);
		}

		super.remove();
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
