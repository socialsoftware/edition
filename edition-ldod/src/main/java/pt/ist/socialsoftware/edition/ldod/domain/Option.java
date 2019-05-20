package pt.ist.socialsoftware.edition.ldod.domain;

public class Option extends Option_Base {
    
    public Option(Menu menu, String name, String link, int position) {
        setMenu(menu);
        setName(name);
        setLink(link);
        setPosition(position);
    }

    public void remove(){
        setMenu(null);

        deleteDomainObject();
    }
}
