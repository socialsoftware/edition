package pt.ist.socialsoftware.edition.text.domain;

public abstract class Source extends Source_Base implements Comparable<Source> {

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

    public Source() {
        setHeteronym(NullHeteronym.getNullHeteronym());
    }

    public String getName() {
        return getAltIdentifier();
    }

    public void remove() {
        setHeteronym(null);
        setFragment(null);

        if (getLdoDDate() != null) {
            getLdoDDate().remove();
        }

        // A source may not have a facsimile ???? - need to be checked with
        // encoders
        if (getFacsimile() != null) {
            getFacsimile().remove();
        }

        for (SourceInter inter : getSourceIntersSet()) {
            removeSourceInters(inter);
        }

        deleteDomainObject();
    }

    @Override
    public int compareTo(Source other) {
        return getName().compareTo(other.getName());
    }

}