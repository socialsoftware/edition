package pt.ist.socialsoftware.edition.validator;

import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class VirtualEditionValidator extends AbstractLdoDValidator {

	private VirtualEdition virtualEdition = null;
	private String acronym = null;
	private String name = null;

	public VirtualEditionValidator(VirtualEdition virtualEdition,
			String acronym, String name) {
		this.virtualEdition = virtualEdition;
		this.acronym = acronym;
		this.name = name;
	}

	public void validate() {
		if (LdoDValidatorFunctions.emptyOrWhitespaceString(acronym)) {
			errors.add("virtualedition.acronym.empty");
			values.put("acronym",
					virtualEdition != null ? virtualEdition.getAcronym() : "");
		}
		if (LdoDValidatorFunctions.emptyOrWhitespaceString(name)) {
			errors.add("virtualedition.name.empty");
			values.put("name",
					virtualEdition != null ? virtualEdition.getName() : "");
		}

	}

}
