package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.FenixFramework;

public class EditionModule extends EditionModule_Base {
    
    public EditionModule(String name) {
        FenixFramework.getDomainRoot().addModule(this);
        setName(name);
    }

    public void remove(){
        getUiComponent().remove();
        setRoot(null);
        deleteDomainObject();
    }
    
}
