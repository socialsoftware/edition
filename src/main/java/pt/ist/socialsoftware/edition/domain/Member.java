package pt.ist.socialsoftware.edition.domain;

import org.joda.time.LocalDate;

public class Member extends Member_Base {

	public enum MemberRole {
		ADMIN, WRITER
	};

	public Member(VirtualEdition virtualEdition, LdoDUser user, MemberRole role, Boolean active) {
		setVirtualEdition(virtualEdition);
		setUser(user);
		setRole(role);
		setDate(LocalDate.now());
		setActive(active);
	}

	public void remove() {
		setUser(null);

		if (getVirtualEdition().getMemberSet().size() == 1)
			getVirtualEdition().remove();
		else
			setVirtualEdition(null);

		deleteDomainObject();
	}

}
