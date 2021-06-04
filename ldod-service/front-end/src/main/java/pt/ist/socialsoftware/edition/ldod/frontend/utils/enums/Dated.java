package pt.ist.socialsoftware.edition.ldod.frontend.utils.enums;

public enum Dated {
    ALL("all"), DATED("dated"), UNDATED("undated");

    private final String dated;

    Dated(String dated) {
        this.dated = dated;
    }

    public String getDated() {
        return this.dated;
    }
}
