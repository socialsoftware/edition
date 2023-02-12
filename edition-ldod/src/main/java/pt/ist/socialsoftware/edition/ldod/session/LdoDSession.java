package pt.ist.socialsoftware.edition.ldod.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;

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
			VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition("LdoD-JPC-anot");
			if (virtualEdition != null) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
			virtualEdition = LdoD.getInstance().getVirtualEdition("LdoD-Mallet");
			if (virtualEdition != null) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
			virtualEdition = LdoD.getInstance().getVirtualEdition("LdoD-Twitter");
			if (virtualEdition != null) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
			virtualEdition = LdoD.getInstance().getVirtualEdition("LdoD-Jogo-Class");
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
	public void updateSession(LdoDUser user) {
		user.getSelectedVirtualEditionsSet().stream().forEach(ve -> addSelectedVE(ve));

		user.getSelectedVirtualEditionsSet().addAll(materializeVirtualEditions());
	}

	public boolean hasSelectedVE(String acronym) {
		return this.selectedVEAcr.contains(acronym);
	}

	public void removeSelectedVE(String acronym) {
		this.selectedVEAcr.remove(acronym);
	}

	public void addSelectedVE(VirtualEdition virtualEdition) {
		String toAddAcr = virtualEdition.getAcronym();

		// do not add the archive virtual edition because it is already
		// hardcoded in the menu
		if (!this.selectedVEAcr.contains(toAddAcr) && !toAddAcr.equals(Edition.ARCHIVE_EDITION_ACRONYM)) {
			this.selectedVEAcr.add(toAddAcr);
			Collections.sort(this.selectedVEAcr);
		}
	}

	public List<VirtualEdition> materializeVirtualEditions() {
		LdoD ldod = LdoD.getInstance();

		return this.selectedVEAcr.stream().map(acr -> ldod.getEdition(acr)).filter(e -> e != null)
				.map(VirtualEdition.class::cast).collect(Collectors.toList());

	}

	public List<String> getSelectedVEAcr() {
		return this.selectedVEAcr;
	}

	@Atomic(mode = TxMode.WRITE)
	public void toggleSelectedVirtualEdition(LdoDUser user, VirtualEdition virtualEdition) {
		if (hasSelectedVE(virtualEdition.getAcronym())) {
			removeSelectedVE(virtualEdition.getAcronym());
			if (user != null) {
				user.removeSelectedVirtualEditions(virtualEdition);
			}
		} else {
			addSelectedVE(virtualEdition);
			if (user != null) {
				user.addSelectedVirtualEditions(virtualEdition);
			}
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
		this.selectedVEAcr.clear();
	}

	public ReadingRecommendation getRecommendation() {
		return this.recommendation;
	}

}
