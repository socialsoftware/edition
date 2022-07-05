package pt.ist.socialsoftware.edition.ldod.shared.exception;

import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class LdoDCreateVirtualEditionException extends LdoDException {
	private static final long serialVersionUID = 1L;

	private List<String> errors = new ArrayList<>();
	private String acronym = null;
	private String title = null;
	private boolean pub = false;
	private List<VirtualEdition> virtualEditions = null;
	private LdoDUser user = null;

	public LdoDCreateVirtualEditionException(List<String> errors, String acronym, String title, boolean pub,
			List<VirtualEdition> virtualEditions, LdoDUser user) {
		this.errors = errors;
		this.acronym = acronym;
		this.title = title;
		this.pub = pub;
		this.virtualEditions = virtualEditions;
		this.user = user;
	}

	public List<String> getErrors() {
		return errors;
	}

	public String getAcronym() {
		return acronym;
	}

	public String getTitle() {
		return title;
	}

	public boolean isPub() {
		return pub;
	}

	public List<VirtualEdition> getVirtualEditions() {
		return virtualEditions;
	}

	public LdoDUser getUser() {
		return user;
	}

}
