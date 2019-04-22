package pt.ist.socialsoftware.edition.ldod.domain;

public abstract class Edition {

    public enum EditionType {
        AUTHORIAL("authorial"), EDITORIAL("editorial"), VIRTUAL("virtual");

        private final String desc;

        EditionType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

}
