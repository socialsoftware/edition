package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class LdoDSession implements Serializable {
	private static final long serialVersionUID = 3742738985902099143L;

	private final Set<String> selectedVEIds = new HashSet<String>();
	private final Set<String> selectedVEAcr = new HashSet<String>();

	public boolean hasSelectedVE(VirtualEdition virtualEdition) {
		return selectedVEIds.contains(virtualEdition.getExternalId());
	}

	public void removeSelectedVE(VirtualEdition virtualEdition) {
		selectedVEIds.remove(virtualEdition.getExternalId());
		selectedVEAcr.remove(virtualEdition.getAcronym());
	}

	public void addSelectedVE(VirtualEdition virtualEdition) {
		String toAddId = new String(virtualEdition.getExternalId());
		String toAddAcr = new String(virtualEdition.getAcronym());
		selectedVEIds.add(toAddId);
		selectedVEAcr.add(toAddAcr);
	}

	// materializes objects on demand and regenerates acronyms
	public Set<VirtualEdition> getSelectedVEs() {
		Set<VirtualEdition> selectedVE = new HashSet<VirtualEdition>();
		selectedVEAcr.clear();
		for (String veId : selectedVEIds) {
			VirtualEdition virtualEdition = (VirtualEdition) AbstractDomainObject
					.fromExternalId(veId);
			selectedVE.add(virtualEdition);
			selectedVEAcr.add(virtualEdition.getAcronym());
		}
		return selectedVE;
	}

	public Set<String> getSelectedVEAcr() {
		return selectedVEAcr;
	}

}
