package pt.ist.socialsoftware.edition.ldod.shared.exception;

public class LdoDDuplicateAcronymException extends LdoDException {

    private String message;

    public LdoDDuplicateAcronymException() {
    }

    ;

    public LdoDDuplicateAcronymException(String message) {
        this.message = message;
    }

    ;

    @Override
    public String getMessage() {
        return message;
    }


    private static final long serialVersionUID = -3947538146603301616L;

}
