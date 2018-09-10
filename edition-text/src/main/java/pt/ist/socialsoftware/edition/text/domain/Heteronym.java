package pt.ist.socialsoftware.edition.text.domain;

public class Heteronym extends Heteronym_Base implements Comparable<Heteronym> {

	public Heteronym() {
		super();
	}

	public Heteronym(CollectionManager collectionManager, String name) {
		setCollectionManager(collectionManager);
		setName(name);
	}

	public void remove() {
		setCollectionManager(null);

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
