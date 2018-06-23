package pt.ist.socialsoftware.edition.ldod.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLdoDValidator {

	protected List<String> errors = new ArrayList<String>();
	protected Map<String, Object> values = new HashMap<String, Object>();

	public List<String> getErrors() {
		return errors;
	}

	public Map<String, Object> getValues() {
		return values;
	}
}
