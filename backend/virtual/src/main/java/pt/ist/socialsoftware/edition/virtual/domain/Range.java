package pt.ist.socialsoftware.edition.virtual.domain;

import pt.ist.socialsoftware.edition.virtual.domain.Range_Base;

public class Range extends Range_Base {

	public Range(Annotation annotation, String start, int startOffset,
			String end, int endOffset) {
		setAnnotation(annotation);
		setStart(start);
		setStartOffset(startOffset);
		setEnd(end);
		setEndOffset(endOffset);
	}

	public void remove() {
		setAnnotation(null);
		deleteDomainObject();
	}

}
