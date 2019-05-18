package pt.ist.socialsoftware.edition.ldod.domain;

public class UiComponent extends UiComponent_Base {
    
    public UiComponent(Module module) {
        setModule(module);
    }

    public void remove() {
        getMenuSet().forEach(Menu::remove);
        setModule(null);
        deleteDomainObject();
    }
    
}
