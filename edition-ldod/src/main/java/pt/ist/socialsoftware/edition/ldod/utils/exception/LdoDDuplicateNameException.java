package pt.ist.socialsoftware.edition.ldod.utils.exception;

public class LdoDDuplicateNameException extends LdoDException {
    private static final long serialVersionUID = 1L;

    public LdoDDuplicateNameException(String name) {
        super(name);
    }
}
