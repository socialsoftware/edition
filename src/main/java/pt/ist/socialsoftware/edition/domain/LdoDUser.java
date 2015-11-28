package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class LdoDUser extends LdoDUser_Base {
	private static Logger log = LoggerFactory.getLogger(LdoDUser.class);

	public enum SocialMediaService {
		TWITTER, FACEBOOK, LINKEDIN, GOOGLE
	};

	static public LdoDUser getAuthenticatedUser() {
		log.debug("getAuthenticatedUser");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			LdoDUserDetails userDetails = null;
			Object principal = authentication.getPrincipal();
			if (principal instanceof LdoDUserDetails) {
				userDetails = (LdoDUserDetails) principal;
				return userDetails.getUser();
			}
		}
		log.debug("getAuthenticatedUser returns null");
		return null;
	}

	public void remove() {
		// TODO
		getToken().remove();
		getLdoD().getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(getUsername()))
				.forEach(uc -> uc.remove());
		setLdoD(null);

		getRolesSet().stream().forEach(r -> removeRoles(r));

		deleteDomainObject();
	}

	public LdoDUser(LdoD ldoD, String username, String password, String firstName, String lastName, String email) {
		setEnabled(false);
		setLdoD(ldoD);
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
	}

	@Override
	public void setUsername(String username) {
		checkUniqueUsername(username);
		super.setUsername(username);
	}

	private void checkUniqueUsername(String username) {
		if (getLdoD().getUsersSet().stream().filter(u -> u.getUsername() != null && u.getUsername().equals(username))
				.findFirst().isPresent()) {
			throw new LdoDDuplicateUsernameException(username);
		}

	}

	public Set<FragInter> getFragInterSet() {
		Set<FragInter> inters = new HashSet<FragInter>();

		for (Annotation annotation : getAnnotationSet()) {
			inters.add(annotation.getFragInter());
		}

		for (UserTagInFragInter userTagInFragInter : getUserTagInFragInterSet()) {
			if (!userTagInFragInter.getDeprecated())
				inters.add(userTagInFragInter.getFragInter());
		}

		return inters;
	}

	@Atomic(mode = TxMode.WRITE)
	public void removeVirtualEdition(VirtualEdition virtualEdition) {
		removeMyVirtualEditions(virtualEdition);
		removeSelectedVirtualEditions(virtualEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public void addToVirtualEdition(VirtualEdition virtualEdition) {
		if (!getMyVirtualEditionsSet().contains(virtualEdition)) {
			addMyVirtualEditions(virtualEdition);
		}
	}

	public RecommendationWeights getRecommendationWeights(VirtualEdition virtualEdition) {
		for (RecommendationWeights recommendationWeights : getRecommendationWeightsSet()) {
			if (recommendationWeights.getUser().getEmail().equals(getEmail())
					&& recommendationWeights.getVirtualEdition().getAcronym().equals(virtualEdition.getAcronym())) {
				return recommendationWeights;
			}
		}
		return LdoD.getInstance().createRecommendationWeights(this, virtualEdition);
	}

	@Atomic(mode = TxMode.WRITE)
	public RegistrationToken createRegistrationToken(String token) {
		return new RegistrationToken(token, this);
	}

	@Atomic(mode = TxMode.WRITE)
	public void enableUnconfirmedUser() {
		setEnabled(true);
		if (getToken() != null)
			getToken().remove();
	}

	@Atomic(mode = TxMode.WRITE)
	public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
		if (!passwordEncoder.matches(currentPassword, getPassword()))
			throw new LdoDException();

		setPassword(passwordEncoder.encode(newPassword));
	}

}
