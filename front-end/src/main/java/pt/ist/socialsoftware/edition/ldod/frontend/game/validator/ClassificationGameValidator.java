package pt.ist.socialsoftware.edition.ldod.frontend.game.validator;

import pt.ist.socialsoftware.edition.ldod.frontend.game.FeGameRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.validator.AbstractLdoDValidator;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.validator.LdoDValidatorFunctions;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;


public class ClassificationGameValidator extends AbstractLdoDValidator {

    private final FeGameRequiresInterface feGameRequiresInterface = new FeGameRequiresInterface();

    private String description = null;
    private String interExternalId = null;

    public ClassificationGameValidator(String description, String interExternalId) {
        this.description = description;
        this.interExternalId = interExternalId;
    }

    public void validate() {
        if (LdoDValidatorFunctions.emptyOrWhitespaceString(this.description)) {
            this.errors.add("game.classification.description.empty");
            this.values.put("description", this.description != null ? this.description : "");
        }

        VirtualEditionInterDto inter = this.feGameRequiresInterface.getVirtualEditionInterByExternalId(this.interExternalId);

        if (inter == null) {
            this.errors.add("game.classification.inter.null");
            this.values.put("interExternalId", this.interExternalId != null ? this.interExternalId : "");
        }

    }

}
