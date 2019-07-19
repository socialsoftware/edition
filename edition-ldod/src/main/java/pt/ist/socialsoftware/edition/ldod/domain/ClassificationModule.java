package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.FenixFramework;

public class ClassificationModule extends ClassificationModule_Base {

    public static ClassificationModule getInstance() {
        return FenixFramework.getDomainRoot().getClassificationModule();
    }
    
    public ClassificationModule() {
        FenixFramework.getDomainRoot().setClassificationModule(this);
    }

    public void remove() {
        getPlayerSet().stream().forEach(player -> player.remove());
    }

    public Player getPlayerByUsername(String user) {
        return getPlayerSet().stream().filter(player -> player.getUser().equals(user)).findAny().orElse(null);
    }
    
}
