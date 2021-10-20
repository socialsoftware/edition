package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Member;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class MemberDto {
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private boolean isAdmin;
	private boolean canSwitchRole;
	private boolean canRemoveMember;
	private String externalId;

	public MemberDto(Member member, VirtualEdition vEdition, LdoDUser user) {
		this.setUsername(member.getUser().getUsername());
		this.setFirstname(member.getUser().getFirstName());
		this.setLastname(member.getUser().getLastName());
		this.setEmail(member.getUser().getEmail());
		this.setAdmin(member.hasRole("ADMIN"));
		this.setCanSwitchRole(vEdition.canSwitchRole(user, member.getUser()));
		this.setCanRemoveMember(vEdition.canRemoveMember(user, member.getUser()));
		this.setExternalId(member.getUser().getExternalId());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isCanSwitchRole() {
		return canSwitchRole;
	}

	public void setCanSwitchRole(boolean canSwitchRole) {
		this.canSwitchRole = canSwitchRole;
	}

	public boolean isCanRemoveMember() {
		return canRemoveMember;
	}

	public void setCanRemoveMember(boolean canRemoveMember) {
		this.canRemoveMember = canRemoveMember;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
