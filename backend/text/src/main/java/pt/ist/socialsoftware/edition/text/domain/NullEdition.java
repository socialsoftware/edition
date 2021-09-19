package pt.ist.socialsoftware.edition.text.domain;



import java.util.HashSet;
import java.util.Set;

public class NullEdition extends NullEdition_Base {

    @Override
    public Boolean getPub() {
        return true;
    }

    public void remove() {
        setTextModule4NullEdition(null);
        deleteDomainObject();
    }

    public Set<ScholarInter> getIntersSet() {
        return new HashSet<>();
    }

    @Override
    public String getReference() {
        return "";
    }

    @Override
    public String getAcronym() {
        return "";
    }
}
