package pt.ist.socialsoftware.edition.ldod.validator;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class ClassificationGameValidator extends AbstractLdoDValidator {

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

		VirtualEditionInter inter = FenixFramework.getDomainObject(this.interExternalId);

		if (inter == null) {
			this.errors.add("game.classification.inter.null");
			this.values.put("interExternalId", this.interExternalId != null ? this.interExternalId : "");
		}

	}

}
