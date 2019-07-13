package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

public class SelectedBy extends SelectedBy_Base {

    public SelectedBy(VirtualEdition virtualEdition, String user) {
        if (virtualEdition.isSelectedBy(user)) {
            throw new LdoDException();
        }

        setVirtualEdition(virtualEdition);
        setUser(user);
    }

    public void remove() {
        setVirtualEdition(null);

        deleteDomainObject();
    }

}
