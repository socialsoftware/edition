package pt.ist.socialsoftware.edition.notification.enums;

public enum SourceType {
    MANUSCRIPT("manuscript"), PRINTED("printed");

    private final String desc;

    SourceType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
