package pt.ist.socialsoftware.edition.validator;

import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class VirtualEditionValidator extends AbstractLdoDValidator {

	private VirtualEdition virtualEdition = null;
	private String acronym = null;
	private String title = null;

	public VirtualEditionValidator(VirtualEdition virtualEdition,
			String acronym, String title) {
		this.virtualEdition = virtualEdition;
		this.acronym = acronym;
		this.title = title;
	}

	public void validate() {
		if (LdoDValidatorFunctions.emptyOrWhitespaceString(acronym)) {
			errors.add("virtualedition.acronym.empty");
			values.put("acronym",
					virtualEdition != null ? virtualEdition.getAcronym() : "");
		}
		if (LdoDValidatorFunctions.emptyOrWhitespaceString(title)) {
			errors.add("virtualedition.title.empty");
			values.put("title",
					virtualEdition != null ? virtualEdition.getTitle() : "");
		}

	}

}
