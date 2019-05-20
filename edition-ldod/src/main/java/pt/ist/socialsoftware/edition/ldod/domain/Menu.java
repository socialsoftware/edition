package pt.ist.socialsoftware.edition.ldod.domain;

public class Menu extends Menu_Base {
    
    public Menu(UiComponent uiComponent, String name, int position) {
        setUiComponent(uiComponent);
        setName(name);
        setPosition(position);
    }

    public void remove(){
        getOptionSet().forEach(Option::remove);
        setUiComponent(null);
        deleteDomainObject();
    }
}
