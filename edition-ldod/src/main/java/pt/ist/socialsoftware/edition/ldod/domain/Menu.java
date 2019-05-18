package pt.ist.socialsoftware.edition.ldod.domain;

public class Menu extends Menu_Base {
    
    public Menu(UiComponent uiComponent, String name) {
        setUiComponent(uiComponent);
        setName(name);
    }

    public void remove(){
        getOptionSet().forEach(Option::remove);
        setUiComponent(null);
        deleteDomainObject();
    }
}
