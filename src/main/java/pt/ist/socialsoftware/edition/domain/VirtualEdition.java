package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public class VirtualEdition extends VirtualEdition_Base {

	public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym,
			String title, String date, Boolean pub) {
		setLdoD4Virtual(ldod);
		addParticipant(participant);
		setAcronym(acronym);
		setTitle(title);
		setDate(date);
		setPub(pub);
	}

	@Override
	public void setPub(Boolean pub) {
		if (!pub) {
			Set<LdoDUser> participants = getParticipantSet();
			for (LdoDUser user : getSelectedBySet()) {
				if (!participants.contains(user)) {
					this.removeSelectedBy(user);
				}
			}
		}
		super.setPub(pub);
	}

	public void remove() {
		setLdoD4Virtual(null);

		for (LdoDUser user : getParticipantSet()) {
			removeParticipant(user);
		}

		for (LdoDUser user : getSelectedBySet()) {
			removeSelectedBy(user);
		}

		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {
			removeVirtualEditionInters(inter);
		}

		deleteDomainObject();
	}

}
