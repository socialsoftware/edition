package pt.ist.socialsoftware.edition.ldod.frontend.virtual.validator;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.utils.validator.AbstractLdoDValidator;
import pt.ist.socialsoftware.edition.ldod.utils.validator.LdoDValidatorFunctions;

public class VirtualEditionValidator extends AbstractLdoDValidator {

    private VirtualEdition virtualEdition = null;
    private String acronym = null;
    private String title = null;

    public VirtualEditionValidator(VirtualEdition virtualEdition, String acronym, String title) {
        this.virtualEdition = virtualEdition;
        this.acronym = acronym;
        this.title = title;
    }

    public void validate() {
        if (LdoDValidatorFunctions.emptyOrWhitespaceString(this.acronym)) {
            this.errors.add("virtualedition.acronym.empty");
            this.values.put("acronym", this.virtualEdition != null ? this.virtualEdition.getShortAcronym() : "");
        }

        if (LdoDValidatorFunctions.hasBlanks(this.acronym)) {
            this.errors.add("virtualedition.acronym.blanks");
            this.values.put("acronym", this.virtualEdition != null ? this.virtualEdition.getShortAcronym() : "");
        }

        if (!this.acronym.matches("^[A-Za-z0-9\\-]+$")) {
            this.errors.add("virtualedition.acronym.alphanumeric");
            this.values.put("acronym", this.virtualEdition != null ? this.virtualEdition.getShortAcronym() : "");
        }

        if (LdoDValidatorFunctions.lengthLimit(this.acronym, 10)) {
            this.errors.add("virtualedition.acronym.length");
            this.values.put("acronym", this.virtualEdition != null ? this.virtualEdition.getShortAcronym() : "");
        }

        if (LdoDValidatorFunctions.emptyOrWhitespaceString(this.title)) {
            this.errors.add("virtualedition.title.empty");
            this.values.put("title", this.virtualEdition != null ? this.virtualEdition.getTitle() : "");
        }

    }

}
