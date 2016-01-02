package pt.ist.socialsoftware.edition.domain;

public class Member extends Member_Base {

	public enum MemberRole {
		ADMIN, WRITER
	};

	public Member(VirtualEdition virtualEdition, LdoDUser user, MemberRole role) {
		setVirtualEdition(virtualEdition);
		setUser(user);
		setRole(role);
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
