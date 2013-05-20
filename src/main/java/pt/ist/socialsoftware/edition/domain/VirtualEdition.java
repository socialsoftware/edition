package pt.ist.socialsoftware.edition.domain;

public class VirtualEdition extends VirtualEdition_Base {

	public VirtualEdition(LdoD ldod, String acronym, String name, String date) {
		setLdoD4Virtual(ldod);
		setAcronym(acronym);
		setName(name);
		setDate(date);
	}

	public void remove() {
		removeLdoD4Virtual();

		deleteDomainObject();
	}

}
