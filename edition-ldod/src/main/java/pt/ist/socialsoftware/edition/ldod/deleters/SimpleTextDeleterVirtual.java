package pt.ist.socialsoftware.edition.ldod.deleters;

import pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.text.deleters.SimpleTextDeleter;
import pt.ist.socialsoftware.edition.text.domain.SimpleText;

public class SimpleTextDeleterVirtual extends SimpleTextDeleter {

    @Override
    public void remove(SimpleText simpleText) {
		for (HumanAnnotation annotation : simpleText.getStartAnnotationsSet()) {
			annotation.remove();
		}

		for (HumanAnnotation annotation : simpleText.getEndAnnotationsSet()) {
			annotation.remove();
		}
        super.remove(simpleText);
    }
}
