package pt.ist.socialsoftware.edition.ldod.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

public class PermissionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> read = new ArrayList<String>();
	private List<String> admin = new ArrayList<String>();
	private List<String> update = new ArrayList<String>();
	private List<String> delete = new ArrayList<String>();

	public PermissionDTO() {
	}

	public PermissionDTO(VirtualEdition virtualEdition, LdoDUser user) {
		if (!virtualEdition.getPub()) {
			for (LdoDUser participant : virtualEdition.getParticipantSet()) {
				read.add(participant.getUsername());
			}
		}

		// admin.add(user.getUsername());

		if (virtualEdition.getParticipantSet().contains(user))
			update.add(user.getUsername());

		delete.add(user.getUsername());

	}

	public List<String> getRead() {
		return read;
	}

	public void setRead(List<String> read) {
		this.read = read;
	}

	public List<String> getAdmin() {
		return admin;
	}

	public void setAdmin(List<String> admin) {
		this.admin = admin;
	}

	public List<String> getUpdate() {
		return update;
	}

	public void setUpdate(List<String> update) {
		this.update = update;
	}

	public List<String> getDelete() {
		return delete;
	}

	public void setDelete(List<String> delete) {
		this.delete = delete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
