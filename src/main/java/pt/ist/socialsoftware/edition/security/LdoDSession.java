package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class LdoDSession implements Serializable {
    private static final long serialVersionUID = 3742738985902099143L;

    private final List<String> selectedVEIds = new ArrayList<String>();
    private final List<String> selectedVEAcr = new ArrayList<String>();

    @Atomic(mode = TxMode.WRITE)
    public void updateSession(LdoDUser user) {
        user.getSelectedVirtualEditionsSet().stream()
                .forEach(ve -> addSelectedVE(ve));

        user.getSelectedVirtualEditionsSet().addAll(getSelectedVEs());
    }

    public boolean hasSelectedVE(String externalId) {
        return selectedVEIds.contains(externalId);
    }

    public void removeSelectedVE(String externalId, String acronym) {
        selectedVEIds.remove(externalId);
        selectedVEAcr.remove(acronym);
    }

    public void addSelectedVE(VirtualEdition virtualEdition) {
        String toAddId = new String(virtualEdition.getExternalId());
        String toAddAcr = new String(virtualEdition.getAcronym());

        if (!selectedVEIds.contains(toAddId)) {
            selectedVEIds.add(toAddId);
            selectedVEAcr.add(toAddAcr);
        }
    }

    // materializes objects on demand and regenerates acronyms
    public List<VirtualEdition> getSelectedVEs() {
        List<VirtualEdition> selectedVE = new ArrayList<VirtualEdition>();
        selectedVEAcr.clear();
        for (String veId : selectedVEIds) {
            VirtualEdition virtualEdition = (VirtualEdition) FenixFramework
                    .getDomainObject(veId);
            selectedVE.add(virtualEdition);
            selectedVEAcr.add(virtualEdition.getAcronym());
        }
        return selectedVE;
    }

    public List<String> getSelectedVEAcr() {
        return selectedVEAcr;
    }

    @Atomic(mode = TxMode.WRITE)
    public void toggleSelectedVirtualEdition(LdoDUser user,
            VirtualEdition virtualEdition) {
        if (hasSelectedVE(virtualEdition.getExternalId())) {
            removeSelectedVE(virtualEdition.getExternalId(),
                    virtualEdition.getAcronym());
            if (user != null)
                user.removeSelectedVirtualEditions(virtualEdition);
        } else {
            addSelectedVE(virtualEdition);
            if (user != null)
                user.addSelectedVirtualEditions(virtualEdition);
        }
    }

}
