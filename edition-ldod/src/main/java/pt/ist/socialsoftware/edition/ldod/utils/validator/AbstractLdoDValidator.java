package pt.ist.socialsoftware.edition.ldod.utils.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLdoDValidator {

    protected List<String> errors = new ArrayList<>();
    protected Map<String, Object> values = new HashMap<>();

    public List<String> getErrors() {
        return this.errors;
    }

    public Map<String, Object> getValues() {
        return this.values;
    }
}
