package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.FenixFramework;

public class Text extends Text_Base {
    public static Logger logger = LoggerFactory.getLogger(Text.class);

    public static Text getInstance() {
        return FenixFramework.getDomainRoot().getText();
    }
    
    public Text() {
        FenixFramework.getDomainRoot().setText(this);
    }

    public void remove(){

        deleteDomainObject();

    }
    
}
