package pt.ist.socialsoftware.edition.domain;

public class Heteronym extends Heteronym_Base implements Comparable<Heteronym> {

	public Heteronym() {
		super();
	}

	public Heteronym(LdoD ldoD, String name) {
		setLdoD(ldoD);
		setName(name);
	}

	public void remove() {
		setLdoD(null);

		deleteDomainObject();
	}

	@Override
	public int compareTo(Heteronym o) {
		return this.getXmlId().compareTo(o.getXmlId());
	}

}
