package pt.ist.socialsoftware.edition.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

@Component
public class LdoDPermissionEvaluator implements PermissionEvaluator {

    public static final String PARTICIPANT = "participant";
    public static final String PUBLIC = "public";

    @Override
    public boolean hasPermission(Authentication authentication,
            Object targetDomainObject, Object permission) {
        boolean hasPermission = false;

        String[] permissions = ((String) permission).split("\\.");

        System.out.println(
                "LdoDPermissionEvaluator:" + (String) targetDomainObject
                        + permissions[0] + permissions[1]);

        VirtualEdition virtualEdition = null;
        if (targetDomainObject instanceof String) {
            switch (permissions[0]) {
            case "virtualedition":
                virtualEdition = FenixFramework
                        .getDomainObject((String) targetDomainObject);
                break;
            case "fragInter":
                FragInter fragInter = FenixFramework
                        .getDomainObject((String) targetDomainObject);
                if (fragInter != null) {
                    virtualEdition = (VirtualEdition) fragInter.getEdition();
                }
                break;
            case "taxonomy":
                Taxonomy taxonomy = FenixFramework
                        .getDomainObject((String) targetDomainObject);
                if (taxonomy != null) {
                    virtualEdition = (VirtualEdition) taxonomy.getEdition();
                }
                break;
            case "category":
                Category category = FenixFramework
                        .getDomainObject((String) targetDomainObject);
                if (category != null) {
                    virtualEdition = (VirtualEdition) category.getTaxonomy()
                            .getEdition();
                }
                break;
            case "tag":
                Tag tag = FenixFramework
                        .getDomainObject((String) targetDomainObject);
                if (tag != null) {
                    virtualEdition = (VirtualEdition) tag.getCategory()
                            .getTaxonomy().getEdition();
                }
                break;
            default:
                assert false;
            }

            if (virtualEdition == null) {
                hasPermission = true;
            } else if (permissions[1].equals(PARTICIPANT)) {
                hasPermission = virtualEdition.getParticipantSet()
                        .contains(LdoDUser.getAuthenticatedUser());
            } else if (permissions[1].equals(PUBLIC)) {
                if (virtualEdition.getPub()) {
                    hasPermission = true;
                } else {
                    hasPermission = virtualEdition.getParticipantSet()
                            .contains(LdoDUser.getAuthenticatedUser());
                }
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
