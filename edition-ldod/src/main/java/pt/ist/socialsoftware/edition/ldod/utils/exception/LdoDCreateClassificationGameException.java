package pt.ist.socialsoftware.edition.ldod.utils.exception;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

import java.util.ArrayList;
import java.util.List;

public class LdoDCreateClassificationGameException extends LdoDException {
    private static final long serialVersionUID = 1L;

    private List<String> errors = new ArrayList<>();
    private final String description;
    private final String interExternalId;
    private final String date;
    private final VirtualEdition virtualEdition;

    public LdoDCreateClassificationGameException(List<String> errors, String description, String date,
												 String interExternalId, VirtualEdition virtualEdition) {
        this.errors = errors;
        this.description = description;
        this.date = date;
        this.interExternalId = interExternalId;
        this.virtualEdition = virtualEdition;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public String getInterExternalId() {
        return this.interExternalId;
    }

    public VirtualEdition getVirtualEdition() {
        return this.virtualEdition;
    }

}
