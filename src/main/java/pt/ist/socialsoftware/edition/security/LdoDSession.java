package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class LdoDSession implements Serializable {
	private static final long serialVersionUID = 3742738985902099143L;

	private final List<String> selectedVEAcr = new ArrayList<String>();

	@Atomic(mode = TxMode.WRITE)
	public void updateSession(LdoDUser user) {
		user.getSelectedVirtualEditionsSet().stream().forEach(ve -> addSelectedVE(ve));

		user.getSelectedVirtualEditionsSet().addAll(materializeVirtualEditions());
	}

	public boolean hasSelectedVE(String acronym) {
		return selectedVEAcr.contains(acronym);
	}

	public void removeSelectedVE(String acronym) {
		selectedVEAcr.remove(acronym);
	}

	public void addSelectedVE(VirtualEdition virtualEdition) {
		String toAddAcr = new String(virtualEdition.getAcronym());

		if (!selectedVEAcr.contains(toAddAcr)) {
			selectedVEAcr.add(toAddAcr);
			Collections.sort(selectedVEAcr);
		}
	}

	public List<VirtualEdition> materializeVirtualEditions() {
		LdoD ldod = LdoD.getInstance();

		return selectedVEAcr.stream().map(acr -> ldod.getEdition(acr)).filter(e -> e != null)
				.map(VirtualEdition.class::cast).collect(Collectors.toList());

	}

	public List<String> getSelectedVEAcr() {
		return selectedVEAcr;
	}

	@Atomic(mode = TxMode.WRITE)
	public void toggleSelectedVirtualEdition(LdoDUser user, VirtualEdition virtualEdition) {
		if (hasSelectedVE(virtualEdition.getAcronym())) {
			removeSelectedVE(virtualEdition.getAcronym());
			if (user != null)
				user.removeSelectedVirtualEditions(virtualEdition);
		} else {
			addSelectedVE(virtualEdition);
			if (user != null)
				user.addSelectedVirtualEditions(virtualEdition);
		}
	}

	public void synchronizeSession(LdoDUser user) {
		List<VirtualEdition> selected = materializeVirtualEditions();

		clearSession();
		if (user != null) {
			user.getSelectedVirtualEditionsSet().stream().forEach(ve -> addSelectedVE(ve));
		} else {
			selected.stream().filter(ve -> ve.getPub()).forEach(ve -> addSelectedVE(ve));
		}

	}

	private void clearSession() {
		selectedVEAcr.clear();
	}

}
