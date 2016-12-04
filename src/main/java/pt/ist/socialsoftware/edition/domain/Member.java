package pt.ist.socialsoftware.edition.domain;

import org.joda.time.LocalDate;

public class Member extends Member_Base {

	public enum MemberRole {
		ADMIN, MEMBER
	};

	public Member(VirtualEdition virtualEdition, LdoDUser user, MemberRole role, Boolean active) {
		setVirtualEdition(virtualEdition);
		setUser(user);
		setRole(role);
		setDate(LocalDate.now());
		setActive(active);
	}

	public boolean hasRole(String role) {
		return getRole().name().equals(role);
	}

	public void remove() {
		setUser(null);
		setVirtualEdition(null);

		deleteDomainObject();
	}

}
