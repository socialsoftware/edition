package pt.ist.socialsoftware.edition.ldod.frontend.utils.enums;

public enum Mode {
    AND("and"), OR("or");

    private final String mode;

    Mode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }

}
