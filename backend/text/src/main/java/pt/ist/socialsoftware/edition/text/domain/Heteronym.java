package pt.ist.socialsoftware.edition.text.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Heteronym extends Heteronym_Base implements Comparable<Heteronym> {
    public static Logger logger = LoggerFactory.getLogger(Heteronym.class);

    public Heteronym() {
        super();
    }

    public Heteronym(TextModule text, String name) {
        setTextModule(text);
        setName(name);
    }

    public void remove() {
        setTextModule(null);

        getSourceSet().stream().forEach(s -> removeSource(s));
        getScholarInterSet().stream().forEach(i -> removeScholarInter(i));

        deleteDomainObject();
    }

    @Override
    public int compareTo(Heteronym o) {
        return this.getXmlId().compareTo(o.getXmlId());
    }

    public boolean isNullHeteronym() {
        return false;
    }

}
