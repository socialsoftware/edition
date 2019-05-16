package pt.ist.socialsoftware.edition.ldod.domain;

public class Option extends Option_Base {
    
    public Option(Menu menu, String link) {
        setMenu(menu);
        setLink(link);
    }

    public void remove(){
        setMenu(null);
        setLink(null);
    }
}
