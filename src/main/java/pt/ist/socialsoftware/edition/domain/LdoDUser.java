package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.security.LdoDUserDetails;

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
		return null;
	}

	public void remove() {
		// TODO
	}

	public LdoDUser(LdoD ldoD, String username, String password, String firstName, String lastName, String email) {
		super();
		setLdoD(ldoD);
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
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

}
