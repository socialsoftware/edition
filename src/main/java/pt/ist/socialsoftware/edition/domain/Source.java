package pt.ist.socialsoftware.edition.domain;

public abstract class Source extends Source_Base implements Comparable<Source> {

	public enum SourceType {
		MANUSCRIPT("manuscript"), PRINTED("printed");

		private final String desc;

		SourceType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public abstract String getName();

	public abstract String getMetaTextual();

	public void remove() {
		setFragment(null);

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
