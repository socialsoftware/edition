package pt.ist.socialsoftware.edition.api.utils.enums;

public enum PrecisionType {
    HIGH("high"), MEDIUM("medium"), LOW("low"), UNKNOWN("unknown");

    private final String desc;

    PrecisionType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
