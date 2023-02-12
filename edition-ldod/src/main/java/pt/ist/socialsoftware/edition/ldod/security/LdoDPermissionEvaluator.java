package pt.ist.socialsoftware.edition.ldod.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.io.Serializable;

@Component
public class LdoDPermissionEvaluator implements PermissionEvaluator {
    private static Logger log = LoggerFactory.getLogger(LdoDPermissionEvaluator.class);

    public static final String ADMIN = "admin";
    public static final String PARTICIPANT = "participant";
    public static final String PUBLIC = "public";
    public static final String ANNOTATION = "annotation";
    public static final String ANNOTATION_UPDATE = "annotation-update";
    public static final String ANNOTATION_DELETE = "annotation-delete";

    public static final String TAXONOMY = "taxonomy";
    private static final String LOGGED = "logged";

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;

        LdoDUser loggedUser = LdoDUser.getAuthenticatedUser();

        String[] permissions = ((String) permission).split("\\.");

        log.debug("hasPermission {}, {}", targetDomainObject, permission);

        if (permissions[0].equals(ADMIN)) {
            return loggedUser.getRolesSet().stream().map(Role::getType).anyMatch(roleType -> roleType.equals(Role.RoleType.ROLE_ADMIN));
        }

        VirtualEdition virtualEdition = null;
        LdoDUser user = null;
        HumanAnnotation annotation = null;
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
                case "user":
                    user = LdoD.getInstance().getUser((String) targetDomainObject);
                    break;
                case "annotation":
                    annotation = FenixFramework.getDomainObject((String) targetDomainObject);
                    break;
                default:
                    assert false;
            }

            if (annotation != null && loggedUser != null) {
                if (permissions[1].equals((ANNOTATION_UPDATE)))
                    hasPermission = annotation.canUpdate(loggedUser);
                if (permissions[1].equals((ANNOTATION_DELETE)))
                    hasPermission = annotation.canDelete(loggedUser);
            }

            if (virtualEdition == null) {
                hasPermission = true;
            } else if (permissions[1].equalsIgnoreCase(ADMIN)) {
                System.out.println(loggedUser);
                hasPermission = virtualEdition.getAdminSet().contains(loggedUser);
            } else if (permissions[1].equals(PARTICIPANT)) {
                hasPermission = virtualEdition.getParticipantSet().contains(loggedUser);
            } else if (permissions[1].equals(PUBLIC)) {
                hasPermission = virtualEdition.checkAccess();
            } else if (permissions[1].equals(ANNOTATION)) {
                hasPermission = virtualEdition.getTaxonomy().canManipulateAnnotation(loggedUser);
            } else if (permissions[1].equals(TAXONOMY)) {
                hasPermission = virtualEdition.getTaxonomy().canManipulateTaxonomy(loggedUser);
            }

            if (user != null) {
                if (permissions[1].equals(LOGGED)) {
                    hasPermission = loggedUser == user;
                }
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
