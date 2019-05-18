package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.FenixFramework;

public class Module extends Module_Base {
    
    public Module(String name) {
        FenixFramework.getDomainRoot().addModule(this);
        setName(name);
    }

    public void remove(){
        getUiComponent().remove();
        setRoot(null);
        deleteDomainObject();
    }
    
}
