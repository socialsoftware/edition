package pt.ist.socialsoftware.edition.ldod.frontend.user.session;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pt.ist.socialsoftware.edition.ldod.frontend.reading.ReadingRecommendation;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FrontendSession implements Serializable {
    private final SessionRequiresInterface sessionRequiresInterface = new SessionRequiresInterface();

    private static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";

    private static final long serialVersionUID = 3742738985902099143L;

    private final List<String> selectedVEAcr = new ArrayList<>();

    private final ReadingRecommendation recommendation = new ReadingRecommendation();

    public static FrontendSession getFrontendSession() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        FrontendSession frontendSession = null;
        if (request.getSession().getAttribute("frontendSession") == null) {
            frontendSession = new FrontendSession();

            frontendSession.addSelectedVE("LdoD-JPC-anot");

            frontendSession.addSelectedVE("LdoD-Mallet");

            frontendSession.addSelectedVE("LdoD-Twitter");

            frontendSession.addSelectedVE("LdoD-Jogo-Class");

            request.getSession().setAttribute("frontendSession", frontendSession);
        } else {
            frontendSession = (FrontendSession) request.getSession().getAttribute("frontendSession");
        }

        return frontendSession;
    }

    public List<String> getSelectedVEAcr() {
        return this.selectedVEAcr;
    }

    public List<VirtualEditionDto> getSelectedVirtualEditions() {
        return getSelectedVEAcr().stream()
                .map(acronym -> this.sessionRequiresInterface.getVirtualEditionByAcronym(acronym))
                .collect(Collectors.toList());
    }

    public boolean hasSelectedVE(String acronym) {
        return this.selectedVEAcr.contains(acronym);
    }

    public void removeSelectedVE(String acronym) {
        this.selectedVEAcr.remove(acronym);
    }

    public void addSelectedVE(String virtualEditionAcronym) {
        // do not add the archive virtual edition because it is already
        // hardcoded in the menu
        if (!this.selectedVEAcr.contains(virtualEditionAcronym) && !virtualEditionAcronym.equals(ARCHIVE_EDITION_ACRONYM)) {
            this.selectedVEAcr.add(virtualEditionAcronym);
            Collections.sort(this.selectedVEAcr);
        }
    }

    public void toggleSelectedVirtualEdition(String user, String virtualEditionAcronym) {
        if (hasSelectedVE(virtualEditionAcronym)) {
            removeSelectedVE(virtualEditionAcronym);
            if (user != null) {
                this.sessionRequiresInterface.removeSelectedByUser(user, virtualEditionAcronym);
            }
        } else {
            addSelectedVE(virtualEditionAcronym);
            if (user != null) {
                this.sessionRequiresInterface.addSelectedByUser(user, virtualEditionAcronym);
            }
        }
    }

    public void updateSession(String username) {
        this.sessionRequiresInterface.getSelectedVirtualEditionsByUser(username).stream().forEach(acronym -> addSelectedVE(acronym));
    }

    public ReadingRecommendation getRecommendation() {
        return this.recommendation;
    }

}
