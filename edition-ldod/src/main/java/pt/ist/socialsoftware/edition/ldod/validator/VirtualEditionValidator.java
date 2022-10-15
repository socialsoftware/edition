package pt.ist.socialsoftware.edition.ldod.validator;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

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

    public static void validateInputs(String acronym, String title) {
        if (LdoDValidatorFunctions.emptyOrWhitespaceString(acronym))
            throw new LdoDException(Message.EMPTY_ACRONYM.getLabel());

        if (LdoDValidatorFunctions.hasBlanks(acronym))
            throw new LdoDException(String.format(Message.ACRONYM_BLANKS.getLabel(), acronym));

        if (!acronym.matches("^[A-Za-z0-9\\-]+$"))
            throw new LdoDException(String.format(Message.ACRONYM_ALPHANUMERIC.getLabel(), acronym));
        if (LdoDValidatorFunctions.lengthLimit(acronym, 10))
            throw new LdoDException(String.format(Message.ACRONYM_LENGTH_EXCEED.getLabel(), acronym));

        if (LdoDValidatorFunctions.emptyOrWhitespaceString(title))
            throw new LdoDException(Message.EMPTY_TITLE.getLabel());

    }

}
