package pt.ist.socialsoftware.edition.notification.utils;




import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;

import java.util.ArrayList;
import java.util.List;

public class LdoDEditVirtualEditionException extends LdoDException {

    private static final long serialVersionUID = 6962978813318025931L;

    private List<String> errors = new ArrayList<>();
    private VirtualEditionDto virtualEdition = null;
    private String acronym = null;
    private String title = null;
    private boolean pub = false;

    public LdoDEditVirtualEditionException(List<String> errors,
                                           VirtualEditionDto virtualEdition, String acronym, String title,
                                           boolean pub) {
        this.errors = errors;
        this.virtualEdition = virtualEdition;
        this.acronym = acronym;
        this.title = title;
        this.pub = pub;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public VirtualEditionDto getVirtualEdition() {
        return this.virtualEdition;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isPub() {
        return this.pub;
    }

}
