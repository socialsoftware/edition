package pt.ist.socialsoftware.edition.text.deleters;

import pt.ist.socialsoftware.edition.text.domain.SourceInter;

public class SourceInterDeleter {

    public void remove(SourceInter sourceInter) {
        sourceInter.setSource(null);
    }
}
