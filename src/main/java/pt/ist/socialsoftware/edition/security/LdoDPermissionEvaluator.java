package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

@Component
public class LdoDPermissionEvaluator implements PermissionEvaluator {
	private static Logger log = LoggerFactory.getLogger(LdoDPermissionEvaluator.class);

	public static final String ADMIN = "admin";
	public static final String PARTICIPANT = "participant";
	public static final String PUBLIC = "public";
	public static final String ANNOTATION = "annotation";
	public static final String TAXONOMY = "taxonomy";

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		boolean hasPermission = false;

		String[] permissions = ((String) permission).split("\\.");

		log.debug("hasPermission {}, {}, {}", targetDomainObject, permissions[0], permissions[1]);

		VirtualEdition virtualEdition = null;
		if (targetDomainObject instanceof String) {
			switch (permissions[0]) {
			case "edition":
				Edition edition = FenixFramework.getDomainObject((String) targetDomainObject);
				if (edition instanceof VirtualEdition) {
					virtualEdition = (VirtualEdition) edition;
				} else {
					virtualEdition = null;
				}
				break;
			case "editionacronym":
				edition = LdoD.getInstance().getEdition((String) targetDomainObject);
				if (edition instanceof VirtualEdition) {
					virtualEdition = (VirtualEdition) edition;
				} else {
					virtualEdition = null;
				}
				break;
			case "virtualedition":
				virtualEdition = FenixFramework.getDomainObject((String) targetDomainObject);
				break;
			case "fragInter":
				FragInter fragInter = FenixFramework.getDomainObject((String) targetDomainObject);
				if (fragInter instanceof VirtualEditionInter) {
					virtualEdition = (VirtualEdition) fragInter.getEdition();
				} else {
					virtualEdition = null;
				}
				break;
			case "taxonomy":
				Taxonomy taxonomy = FenixFramework.getDomainObject((String) targetDomainObject);
				if (taxonomy != null) {
					virtualEdition = taxonomy.getEdition();
				}
				break;
			case "category":
				Category category = FenixFramework.getDomainObject((String) targetDomainObject);
				if (category != null) {
					virtualEdition = category.getTaxonomy().getEdition();
				}
				break;
			case "tag":
				Tag tag = FenixFramework.getDomainObject((String) targetDomainObject);
				if (tag != null) {
					virtualEdition = tag.getCategory().getTaxonomy().getEdition();
				}
				break;
			default:
				assert false;
			}

			LdoDUser user = LdoDUser.getAuthenticatedUser();
			if (virtualEdition == null) {
				hasPermission = true;
			} else if (permissions[1].equals(ADMIN)) {
				hasPermission = virtualEdition.getAdminSet().contains(user);
			} else if (permissions[1].equals(PARTICIPANT)) {
				hasPermission = virtualEdition.getParticipantSet().contains(user);
			} else if (permissions[1].equals(PUBLIC)) {
				hasPermission = virtualEdition.checkAccess();
			} else if (permissions[1].equals(ANNOTATION)) {
				hasPermission = virtualEdition.getTaxonomy().canManipulateAnnotation(user);
			} else if (permissions[1].equals(TAXONOMY)) {
				hasPermission = virtualEdition.getTaxonomy().canManipulateTaxonomy(user);
			}
		}

		return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// it is only implementing "hasPermission(#xmlId, #urlId,
		// 'fragInter.public')"

		Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId((String) targetId);

		if (fragment == null) {
			return false;
		}

		FragInter inter = fragment.getFragInterByUrlId(targetType);

		if (inter == null) {
			return false;
		}

		if (inter instanceof VirtualEditionInter) {
			return ((VirtualEdition) inter.getEdition()).checkAccess();
		} else {
			return true;
		}
	}

}
