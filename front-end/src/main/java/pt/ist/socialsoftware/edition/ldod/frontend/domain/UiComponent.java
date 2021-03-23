package pt.ist.socialsoftware.edition.ldod.frontend.domain;

public class UiComponent extends UiComponent_Base {
    
    public UiComponent(EditionModule module) {
        setModule(module);
    }

    public void remove() {
        getMenuSet().forEach(Menu::remove);
        setModule(null);
        deleteDomainObject();
    }
    
}
