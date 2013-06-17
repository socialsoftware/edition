package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class LdoDPermissionEvaluator implements PermissionEvaluator {

	public static final String PARTICIPANT = "participant";
	public static final String PRIVATE = "private";

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
				} else if (permissions[1].equals(PRIVATE)) {
					if (virtualEdition.getPub()) {
						hasPermission = true;
					} else {
						hasPermission = virtualEdition.getParticipantSet()
								.contains(LdoDUser.getUser());
					}
				}

				System.out.println("LdoDPermissionEvaluator:" + hasPermission);

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
