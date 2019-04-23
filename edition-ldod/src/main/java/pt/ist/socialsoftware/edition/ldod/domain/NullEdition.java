package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.HashSet;
import java.util.Set;

public class NullEdition extends NullEdition_Base {

    @Override
    public Boolean getPub() {
        return true;
    }

    public void remove() {
        setText4NullEdition(null);
        deleteDomainObject();
    }

    public Set<ScholarInter> getIntersSet() {
        return new HashSet<>();
    }

    public String getReference() {
        return "";
    }

    @Override
    public String getAcronym() {
        return "";
    }
}
