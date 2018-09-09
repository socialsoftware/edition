package pt.ist.socialsoftware.edition.ldod.domain;

public class Heteronym extends Heteronym_Base implements Comparable<Heteronym> {

	public Heteronym() {
		super();
	}

	public Heteronym(VirtualManager virtualManager, String name) {
		setVirtualManager(virtualManager);
		setName(name);
	}

	public void remove() {
		setVirtualManager(null);

		getSourceSet().stream().forEach(s -> removeSource(s));
		getFragInterSet().stream().forEach(i -> removeFragInter(i));

		deleteDomainObject();
	}

	@Override
	public int compareTo(Heteronym o) {
		return this.getXmlId().compareTo(o.getXmlId());
	}

	public boolean isNullHeteronym() {
		return false;
	}

}
