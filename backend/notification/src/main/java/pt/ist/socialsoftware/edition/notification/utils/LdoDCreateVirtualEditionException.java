package pt.ist.socialsoftware.edition.notification.utils;//package pt.ist.socialsoftware.edition.ldod.frontend.utils;




import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;

import java.util.ArrayList;
import java.util.List;

public class LdoDCreateVirtualEditionException extends LdoDException {
    private static final long serialVersionUID = 1L;

    private List<String> errors = new ArrayList<>();
    private String acronym = null;
    private String title = null;
    private boolean pub = false;
    private List<VirtualEditionDto> virtualEditions = null;
    private String user = null;

    public LdoDCreateVirtualEditionException(List<String> errors, String acronym, String title, boolean pub,
                                             List<VirtualEditionDto> virtualEditions, String user) {
        this.errors = errors;
        this.acronym = acronym;
        this.title = title;
        this.pub = pub;
        this.virtualEditions = virtualEditions;
        this.user = user;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isPub() {
        return this.pub;
    }

    public List<VirtualEditionDto> getVirtualEditions() {
        return this.virtualEditions;
    }

    public String getUser() {
        return this.user;
    }

}
