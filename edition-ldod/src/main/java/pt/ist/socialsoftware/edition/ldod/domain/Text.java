package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.FenixFramework;

import java.util.List;
import java.util.stream.Collectors;

public class Text extends Text_Base {
    public static Logger logger = LoggerFactory.getLogger(Text.class);

    public static Text getInstance() {
        return FenixFramework.getDomainRoot().getText();
    }
    
    public Text() {
        FenixFramework.getDomainRoot().setText(this);
        setNullEdition(new NullEdition());
    }

    public void remove(){
        getNullEdition().remove();
        getHeteronymsSet().forEach(h -> h.remove());

        deleteDomainObject();

    }

    public List<Heteronym> getSortedHeteronyms() {
        return getHeteronymsSet().stream().sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
                .collect(Collectors.toList());
    }
    
}
