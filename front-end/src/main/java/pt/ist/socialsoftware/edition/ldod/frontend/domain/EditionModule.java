package pt.ist.socialsoftware.edition.ldod.frontend.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.SessionRequiresInterface;

public class EditionModule extends EditionModule_Base {
    
    public EditionModule(String name) {
        FenixFramework.getDomainRoot().addModule(this);
        setName(name);
        SessionRequiresInterface.getInstance();
    }

    public void remove(){
        getUiComponent().remove();
        setRoot(null);
        deleteDomainObject();
    }
    
}
