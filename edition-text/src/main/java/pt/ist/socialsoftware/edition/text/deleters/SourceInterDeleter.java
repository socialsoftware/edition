package pt.ist.socialsoftware.edition.text.deleters;

import pt.ist.socialsoftware.edition.text.domain.SourceInter;

public class SourceInterDeleter {

    public void remove(SourceInter sourceInter) {
        System.out.println("\n\n\n\n\n\nDELETING\n\n\n\n\n\n");
        sourceInter.setSource(null);
    }
}
