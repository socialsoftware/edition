package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.text.domain.CollectionManager;
import pt.ist.socialsoftware.edition.text.domain.Edition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualManager extends VirtualManager_Base {
	private static Logger log = LoggerFactory.getLogger(VirtualManager.class);

	public static VirtualManager getInstance() {
		return FenixFramework.getDomainRoot().getVirtualManager();
	}

	public VirtualManager() {
		FenixFramework.getDomainRoot().setVirtualManager(this);
		setLastTwitterID(new LastTwitterID()); // check if this is supposed to be here
	}

	public VirtualEdition getVirtualEdition(String acronym) {
		for (VirtualEdition edition : getVirtualEditionsSet()) {
			if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
				return edition;
			}
		}

		return null;
	}

	public List<VirtualEdition> getVirtualEditions4User(LdoDUser user, LdoDSession session) {
		List<VirtualEdition> manageVE = new ArrayList<>();
		List<VirtualEdition> selectedVE = new ArrayList<>();
		List<VirtualEdition> mineVE = new ArrayList<>();
		List<VirtualEdition> publicVE = new ArrayList<>();

		session.synchronizeSession(user);

		if (user == null) {
			selectedVE.addAll(session.materializeVirtualEditions());
		}

		for (VirtualEdition virtualEdition : getVirtualEditionsSet()) {
			if (user != null && virtualEdition.getSelectedBySet().contains(user)) {
				selectedVE.add(virtualEdition);
			} else if (virtualEdition.getParticipantSet().contains(user)) {
				mineVE.add(virtualEdition);
			} else if (virtualEdition.getPub() && !selectedVE.contains(virtualEdition)) {
				publicVE.add(virtualEdition);
			}
		}

		manageVE.addAll(selectedVE);
		manageVE.addAll(mineVE);
		manageVE.addAll(publicVE);

		return manageVE;
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEdition createVirtualEdition(LdoDUser user, String acronym, String title, LocalDate date, boolean pub,
			Edition usedEdition) {
		log.debug("createVirtualEdition user:{}, acronym:{}, title:{}", user.getUsername(), acronym, title);
		return new VirtualEdition(this, user, acronym, title, date, pub, usedEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEdition createVirtualEdition(LdoDUser user, String acronym, String title, LocalDate date, boolean pub,
											   Edition usedEdition, String mediaSource, String beginDate, String endDate, String geoLocation,
											   String frequency) {
		log.debug("createVirtualEdition user:{}, acronym:{}, title:{}", user.getUsername(), acronym, title);
		return new VirtualEdition(this, user, acronym, title, date, pub, usedEdition, mediaSource, beginDate, endDate,
				geoLocation, frequency);
	}

	@Atomic(mode = TxMode.WRITE)
	public RecommendationWeights createRecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		return new RecommendationWeights(user, virtualEdition);
	}



	public VirtualEdition getArchiveEdition() {
		return getVirtualEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}



	public VirtualEdition getVirtualEditionByXmlId(String xmlId) {
		return getVirtualEditionsSet().stream().filter(ve -> ve.getXmlId().equals(xmlId)).findFirst().orElse(null);
	}

	public Set<TwitterCitation> getAllTwitterCitation() {
		// allTwitterCitations -> all twitter citations in the archive
		Set<TwitterCitation> allTwitterCitations = getCitationSet().stream().filter(TwitterCitation.class::isInstance)
				.map(TwitterCitation.class::cast).collect(Collectors.toSet());
		return allTwitterCitations;
	}

	public TwitterCitation getTwitterCitationByTweetID(long id) {
		TwitterCitation result = null;
		Set<TwitterCitation> allTwiiterCitations = getAllTwitterCitation();
		for (TwitterCitation tc : allTwiiterCitations) {
			if (tc.getTweetID() == id) {
				result = tc;
			}
		}
		return result;
	}

	public Tweet getTweetByTweetID(long id) {
		Tweet result = null;
		Set<Tweet> allTweets = getTweetSet();
		for (Tweet t : allTweets) {
			if (t.getTweetID() == id) {
				result = t;
			}
		}
		return result;
	}

	public boolean checkIfTweetExists(long id) {
		Set<Tweet> allTweets = getTweetSet();
		for (Tweet t : allTweets) {
			if (t.getTweetID() == id) {
				return true;
			}
		}
		return false;
	}

	public Set<Citation> getCitationSet() {
		return CollectionManager.getInstance().getFragmentsSet().stream().flatMap(f -> f.getCitationSet().stream()).collect(Collectors.toSet());
	}

	public Citation getCitationById(long id) {
		return getCitationSet().stream().filter(citation -> citation.getId() == id).findFirst().orElse(null);
	}
}
