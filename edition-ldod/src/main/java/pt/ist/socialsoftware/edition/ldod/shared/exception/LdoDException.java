package pt.ist.socialsoftware.edition.ldod.shared.exception;

import java.io.Serializable;

public class LdoDException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String message;

    public LdoDException() {
        this.message = "";
    }

    public LdoDException(String message) {
        this.message = message;

    }


    @Override
    public String getMessage() {
        return message;
    }


    public static final class LdoDExceptionBuilder {
        private String message;

        private LdoDExceptionBuilder() {
        }

        public static LdoDExceptionBuilder aLdoDException() {
            return new LdoDExceptionBuilder();
        }

        public LdoDExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public LdoDException build() {
            return new LdoDException(message);
        }
    }
}
