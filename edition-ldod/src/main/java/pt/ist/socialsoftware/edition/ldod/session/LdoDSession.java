package pt.ist.socialsoftware.edition.ldod.session;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LdoDSession implements Serializable {
    private static final long serialVersionUID = 3742738985902099143L;

    private final List<String> selectedVEAcr = new ArrayList<>();

    private final ReadingRecommendation recommendation = new ReadingRecommendation();

    public static LdoDSession getLdoDSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        LdoDSession ldoDSession = null;
        if (request.getSession().getAttribute("ldoDSession") == null) {
            ldoDSession = new LdoDSession();
            VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition("VirtualModule-JPC-anot");
            if (virtualEdition != null) {
                ldoDSession.addSelectedVE(virtualEdition);
            }
            virtualEdition = VirtualModule.getInstance().getVirtualEdition("VirtualModule-Mallet");
            if (virtualEdition != null) {
                ldoDSession.addSelectedVE(virtualEdition);
            }
            virtualEdition = VirtualModule.getInstance().getVirtualEdition("VirtualModule-Twitter");
            if (virtualEdition != null) {
                ldoDSession.addSelectedVE(virtualEdition);
            }
            virtualEdition = VirtualModule.getInstance().getVirtualEdition("VirtualModule-Jogo-Class");
            if (virtualEdition != null) {
                ldoDSession.addSelectedVE(virtualEdition);
            }
            request.getSession().setAttribute("ldoDSession", ldoDSession);
        } else {
            ldoDSession = (LdoDSession) request.getSession().getAttribute("ldoDSession");
        }

        return ldoDSession;
    }

    @Atomic(mode = TxMode.WRITE)
    public void updateSession(User user) {
        VirtualModule.getInstance().getSelectedVirtualEditionsByUser(user.getUsername()).stream().forEach(ve -> addSelectedVE(ve));

        VirtualModule.getInstance().getSelectedVirtualEditionsByUser(user.getUsername()).addAll(materializeVirtualEditions());
    }

    public boolean hasSelectedVE(String acronym) {
        return this.selectedVEAcr.contains(acronym);
    }

    public void removeSelectedVE(String acronym) {
        this.selectedVEAcr.remove(acronym);
    }

    public void addSelectedVE(VirtualEdition virtualEdition) {
        String toAddAcr = new String(virtualEdition.getAcronym());

        // do not add the archive virtual edition because it is already
        // hardcoded in the menu
        if (!this.selectedVEAcr.contains(toAddAcr) && !toAddAcr.equals(ExpertEdition.ARCHIVE_EDITION_ACRONYM)) {
            this.selectedVEAcr.add(toAddAcr);
            Collections.sort(this.selectedVEAcr);
        }
    }

    public List<VirtualEdition> materializeVirtualEditions() {
        VirtualModule virtualModule = VirtualModule.getInstance();

        return this.selectedVEAcr.stream().map(acr -> virtualModule.getVirtualEdition(acr)).filter(e -> e != null)
                .map(VirtualEdition.class::cast).collect(Collectors.toList());

    }

    public List<String> getSelectedVEAcr() {
        return this.selectedVEAcr;
    }

    @Atomic(mode = TxMode.WRITE)
    public void toggleSelectedVirtualEdition(String user, VirtualEdition virtualEdition) {
        if (hasSelectedVE(virtualEdition.getAcronym())) {
            removeSelectedVE(virtualEdition.getAcronym());
            if (user != null) {
                virtualEdition.removeSelectedByUser(user);
            }
        } else {
            addSelectedVE(virtualEdition);
            if (user != null) {
                virtualEdition.addSelectedByUser(user);
            }
        }
    }

    public void synchronizeSession(String user) {
        UserProvidesInterface userProvidesInterface = new UserProvidesInterface();

        List<VirtualEdition> selected = materializeVirtualEditions();

        clearSession();
        if (userProvidesInterface.getUser(user) != null) {
            VirtualModule.getInstance().getSelectedVirtualEditionsByUser(user).stream().forEach(ve -> addSelectedVE(ve));
        } else {
            selected.stream().filter(ve -> ve.getPub()).forEach(ve -> addSelectedVE(ve));
        }

    }

    private void clearSession() {
        this.selectedVEAcr.clear();
    }

    public ReadingRecommendation getRecommendation() {
        return this.recommendation;
    }

}
