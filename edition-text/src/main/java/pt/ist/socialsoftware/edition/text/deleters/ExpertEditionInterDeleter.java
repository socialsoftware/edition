package pt.ist.socialsoftware.edition.text.deleters;

import pt.ist.socialsoftware.edition.text.domain.ExpertEditionInter;

public class ExpertEditionInterDeleter {

    public void remove(ExpertEditionInter expertEditionInter) {
        expertEditionInter.setExpertEdition(null);
    }
}
