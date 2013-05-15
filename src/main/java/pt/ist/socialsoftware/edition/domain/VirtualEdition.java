package pt.ist.socialsoftware.edition.domain;

public class VirtualEdition extends VirtualEdition_Base {

	public VirtualEdition(LdoD ldod, String name, String date) {
		setLdoD4Virtual(ldod);
		setName(name);
		setDate(date);
	}

}
