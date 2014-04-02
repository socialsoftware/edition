package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class LdoDPermissionEvaluator implements PermissionEvaluator {

	public static final String PARTICIPANT = "participant";
	public static final String PUBLIC = "public";

	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		boolean hasPermission = false;

		String[] permissions = ((String) permission).split("\\.");

		System.out
				.println("LdoDPermissionEvaluator:"
						+ (String) targetDomainObject + permissions[0]
						+ permissions[1]);

		if (targetDomainObject instanceof String) {
			switch (permissions[0]) {
			case "virtualedition":
				VirtualEdition virtualEdition = FenixFramework
						.getDomainObject((String) targetDomainObject);
				if (virtualEdition == null)
					hasPermission = true;
				else if (permissions[1].equals(PARTICIPANT)) {
					hasPermission = virtualEdition.getParticipantSet()
							.contains(LdoDUser.getUser());
				} else if (permissions[1].equals(PUBLIC)) {
					if (virtualEdition.getPub()) {
						hasPermission = true;
					} else {
						hasPermission = virtualEdition.getParticipantSet()
								.contains(LdoDUser.getUser());
					}
				}

				System.out.println("LdoDPermissionEvaluator:" + hasPermission);

				break;
			case "taxonomy":
				Taxonomy taxonomy = FenixFramework
						.getDomainObject((String) targetDomainObject);
				if (taxonomy == null)
					hasPermission = true;
				else if (permissions[1].equals(PUBLIC)) {
					virtualEdition = (VirtualEdition) taxonomy.getEdition();
					if (virtualEdition.getPub()) {
						hasPermission = true;
					} else {
						hasPermission = virtualEdition.getParticipantSet()
								.contains(LdoDUser.getUser());
					}
				}
				break;
			case "category":
				Category category = FenixFramework
						.getDomainObject((String) targetDomainObject);
				if (category == null)
					hasPermission = true;
				else if (permissions[1].equals(PUBLIC)) {
					virtualEdition = (VirtualEdition) category.getTaxonomy()
							.getEdition();
					if (virtualEdition.getPub()) {
						hasPermission = true;
					} else {
						hasPermission = virtualEdition.getParticipantSet()
								.contains(LdoDUser.getUser());
					}
				}
				break;
			default:
				assert false;
			}
		}

		return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
