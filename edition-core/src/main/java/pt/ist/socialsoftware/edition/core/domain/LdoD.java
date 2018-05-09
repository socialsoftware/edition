package pt.ist.socialsoftware.edition.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.core.session.LdoDSession;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser.SocialMediaService;
import pt.ist.socialsoftware.edition.core.domain.LdoD_Base;
import pt.ist.socialsoftware.edition.core.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.core.shared.exception.LdoDDuplicateUsernameException;

public class LdoD extends LdoD_Base {
	private static Logger log = LoggerFactory.getLogger(LdoD.class);

	public static LdoD getInstance() {
		return FenixFramework.getDomainRoot().getLdoD();
	}
	
	public Set<TwitterCitation> getAllTwitterCitation() {
		LdoD ldoD = LdoD.getInstance();
		//allTwitterCitations - all twitter citations in the archive
		Set<TwitterCitation> allTwitterCitations = ldoD.getCitationsSet()
						.stream().filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
						.collect(Collectors.toSet());
		return allTwitterCitations;
	}
	
	
	public TwitterCitation getTwitterCitationByTweetID(long id) {		
		TwitterCitation result = null;
		Set<TwitterCitation> allTwiiterCitations = getAllTwitterCitation();
		for(TwitterCitation tc: allTwiiterCitations) {
			if(tc.getTweetID() == id) {
				result = tc;
			}
		}
		return result;
	}
	
	public Tweet getTweetByTweetID(long id) {		
		Tweet result = null;
		Set<Tweet> allTweets = getTweetsSet();
		for(Tweet t: allTweets) {
			if(t.getTweetID() == id) {
				result = t;
			}
		}
		return result;
	}
	
	public boolean checkIfTweetExists(long id) {
		Set<Tweet> allTweets = getTweetsSet();
		for(Tweet t: allTweets) {
			if(t.getTweetID() == id) {
				return true;
			}
		}
		return false;
	}
	
	public LdoD() {
		FenixFramework.getDomainRoot().setLdoD(this);
		setNullEdition(new NullEdition());
		setLastTwitterID(new LastTwitterID()); //check if this is supposed to be here
	}

	public List<Heteronym> getSortedHeteronyms() {
		return getHeteronymsSet().stream().sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
				.collect(Collectors.toList());
	}

	public List<ExpertEdition> getSortedExpertEdition() {
		return getExpertEditionsSet().stream().sorted().collect(Collectors.toList());
	}

	public Edition getEdition(String acronym) {
		for (Edition edition : getExpertEditionsSet()) {
			if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
				return edition;
			}
		}

		return getVirtualEdition(acronym);
	}

	public VirtualEdition getVirtualEdition(String acronym) {
		for (VirtualEdition edition : getVirtualEditionsSet()) {
			if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
				return edition;
			}
		}

		return null;
	}

	public LdoDUser getUser(String username) {
		for (LdoDUser user : getUsersSet()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public Fragment getFragmentByXmlId(String target) {
		for (Fragment fragment : getFragmentsSet()) {
			if (fragment.getXmlId().equals(target)) {
				return fragment;
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
			if ((user != null) && (virtualEdition.getSelectedBySet().contains(user))) {
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
	public RecommendationWeights createRecommendationWeights(LdoDUser user, VirtualEdition virtualEdition) {
		return new RecommendationWeights(user, virtualEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public void switchAdmin() {
		setAdmin(!getAdmin());
	}

	@Atomic(mode = TxMode.WRITE)
	public LdoDUser createUser(PasswordEncoder passwordEncoder, String username, String password, String firstName,
			String lastName, String email, SocialMediaService socialMediaService, String socialMediaId) {

		removeOutdatedUnconfirmedUsers();

		if (getUser(username) == null) {
			LdoDUser user = new LdoDUser(this, username, passwordEncoder.encode(password), firstName, lastName, email);
			user.setSocialMediaService(socialMediaService);
			user.setSocialMediaId(socialMediaId);

			Role userRole = Role.getRole(RoleType.ROLE_USER);
			user.addRoles(userRole);

			return user;
		} else {
			throw new LdoDDuplicateUsernameException(username);
		}
	}

	public UserConnection getUserConnection(String userId, String providerId, String providerUserId) {
		return getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(userId)
				&& uc.getProviderId().equals(providerId) && uc.getProviderUserId().equals(providerUserId)).findFirst()
				.orElse(null);
	}

	@Atomic(mode = TxMode.WRITE)
	public void createUserConnection(String userId, String providerId, String providerUserId, int rank,
			String displayName, String profileUrl, String imageUrl, String accessToken, String secret,
			String refreshToken, Long expireTime) {

		new UserConnection(this, userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl,
				accessToken, secret, refreshToken, expireTime);
	}

	public void removeOutdatedUnconfirmedUsers() {
		DateTime now = DateTime.now();
		getTokenSet().stream().filter(t -> t.getExpireTimeDateTime().isBefore(now)).map(t -> t.getUser())
				.forEach(u -> u.remove());
	}

	public RegistrationToken getTokenSet(String token) {
		return getTokenSet().stream().filter(t -> t.getToken().equals(token)).findFirst().orElse(null);
	}

	public Set<SourceInter> getFragmentRepresentatives() {
		return getFragmentsSet().stream().map(f -> f.getRepresentativeSourceInter()).collect(Collectors.toSet());
	}

	public VirtualEdition getArchiveEdition() {
		return getVirtualEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getJPCEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.COELHO_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getTSCEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.CUNHA_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getRZEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ZENITH_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public ExpertEdition getJPEdition() {
		return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.PIZARRO_EDITION_ACRONYM))
				.findFirst().orElse(null);
	}

	public VirtualEdition getVirtualEditionByXmlId(String xmlId) {
		return getVirtualEditionsSet().stream().filter(ve -> ve.getXmlId().equals(xmlId)).findFirst().orElse(null);
	}

	@Atomic(mode = TxMode.WRITE)
	public void createTestUsers(PasswordEncoder passwordEncoder) {
		LdoD ldod = LdoD.getInstance();

		Role userRole = Role.getRole(RoleType.ROLE_USER);
		Role admin = Role.getRole(RoleType.ROLE_ADMIN);

		// the bcrypt generator
		// https://www.dailycred.com/blog/12/bcrypt-calculator
		for (int i = 0; i < 6; i++) {
			String username = "zuser" + Integer.toString(i + 1);
			if (LdoD.getInstance().getUser(username) == null) {
				LdoDUser user = new LdoDUser(ldod, username, passwordEncoder.encode(username), "zuser", "zuser",
						"zuser" + Integer.toString(i + 1) + "@teste.pt");

				user.setEnabled(true);
				user.addRoles(userRole);
			}
		}

	}
}
