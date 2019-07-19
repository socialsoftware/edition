package pt.ist.socialsoftware.edition.ldod.utils.exception;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

import java.util.ArrayList;
import java.util.List;

public class LdoDCreateClassificationGameException extends LdoDException {
    private static final long serialVersionUID = 1L;

    private List<String> errors = new ArrayList<>();
    private final String description;
    private final String interExternalId;
    private final String date;
    private final VirtualEditionDto virtualEdition;

    public LdoDCreateClassificationGameException(List<String> errors, String description, String date,
												 String interExternalId, VirtualEditionDto virtualEdition) {
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

    public VirtualEditionDto getVirtualEdition() {
        return this.virtualEdition;
    }

}
