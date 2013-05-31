package pt.ist.socialsoftware.edition.domain;

public class VirtualEdition extends VirtualEdition_Base {

	public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym,
			String title, String date) {
		setLdoD4Virtual(ldod);
		addParticipant(participant);
		setAcronym(acronym);
		setTitle(title);
		setDate(date);
	}

	public void remove() {
		removeLdoD4Virtual();

		for (LdoDUser user : getParticipant()) {
			removeParticipant(user);
		}

		for (LdoDUser user : getSelectedBy()) {
			removeSelectedBy(user);
		}

		for (VirtualEditionInter inter : getVirtualEditionInters()) {
			removeVirtualEditionInters(inter);
		}

		deleteDomainObject();
	}

}
