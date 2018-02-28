package pt.ist.socialsoftware.edition.core.domain;

import org.joda.time.LocalDate;

import pt.ist.socialsoftware.edition.core.domain.Member_Base;

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
