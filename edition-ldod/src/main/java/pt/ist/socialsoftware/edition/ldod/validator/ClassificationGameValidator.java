package pt.ist.socialsoftware.edition.ldod.validator;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class ClassificationGameValidator extends AbstractLdoDValidator {

    private String description = null;
    private String date = null;
    private boolean openAnnotation = false;
    private String externalId = null;



    public ClassificationGameValidator(String description, String date, boolean openAnnotation, String externalId) {
        this.description = description;
        this.date = date;
        this.openAnnotation = openAnnotation;
        this.externalId = externalId;
    }

    public void validate() {
        if (LdoDValidatorFunctions.emptyOrWhitespaceString(this.description)) {
            this.errors.add("virtualedition.acronym.empty");
            this.values.put("description", this.description != null ? this.description : "");
        }

        if (LdoDValidatorFunctions.lengthLimit(this.description, 100)) {
            this.errors.add("virtualedition.acronym.length");
            this.values.put("description", this.description != null ? this.description : "");
        }

        if (this.externalId == null) {
            this.errors.add("virtualedition.acronym.blanks");
            this.values.put("acronym", this.externalId != null ? this.externalId : "");
        }

        if (this.date == null) {
            this.errors.add("virtualedition.acronym.blanks");
            this.values.put("acronym", this.date != null ? this.date : "");
        }



    }

}
