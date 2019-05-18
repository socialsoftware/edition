package pt.ist.socialsoftware.edition.ldod.domain;

public class Option extends Option_Base {
    
    public Option(Menu menu, String name, String link) {
        setMenu(menu);
        setName(name);
        setLink(link);
    }

    public void remove(){
        setMenu(null);
        setLink(null);
        deleteDomainObject();
    }
}
