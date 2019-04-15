package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;

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

    @Override
    public void setAcronym(String acronym) {
        if (getAcronym() != null && !getAcronym().toUpperCase().equals(acronym.toUpperCase()) || getAcronym() == null) {
            for (ExpertEdition edition : Text.getInstance().getExpertEditionsSet()) {
                if (acronym.toUpperCase().equals(edition.getAcronym().toUpperCase())) {
                    throw new LdoDDuplicateAcronymException();
                }
            }

            for (VirtualEdition edition : LdoD.getInstance().getVirtualEditionsSet()) {
                if (edition.getAcronym() != null && acronym.toUpperCase().equals(edition.getAcronym().toUpperCase())) {
                    throw new LdoDDuplicateAcronymException();
                }
            }
        }

        super.setAcronym(acronym);
    }

    public abstract EditionType getSourceType();

    public abstract String getReference();

}
