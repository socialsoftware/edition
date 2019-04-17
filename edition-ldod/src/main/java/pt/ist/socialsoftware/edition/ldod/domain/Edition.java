package pt.ist.socialsoftware.edition.ldod.domain;

public abstract class Edition extends pt.ist.socialsoftware.edition.ldod.domain.Edition_Base {
    public static final String COELHO_EDITION_ACRONYM = "JPC";
    public static final String CUNHA_EDITION_ACRONYM = "TSC";
    public static final String ZENITH_EDITION_ACRONYM = "RZ";
    public static final String PIZARRO_EDITION_ACRONYM = "JP";
    public static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";
    public static final String COELHO_EDITION_NAME = "Jacinto do Prado Coelho";
    public static final String CUNHA_EDITION_NAME = "Teresa Sobral Cunha";
    public static final String ZENITH_EDITION_NAME = "Richard Zenith";
    public static final String PIZARRO_EDITION_NAME = "Jerónimo Pizarro";
    public static final String ARCHIVE_EDITION_NAME = "Edição do Arquivo LdoD";

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
