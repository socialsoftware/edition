package pt.ist.socialsoftware.edition.ldod.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.io.Serializable;

@Component
public class LdoDPermissionEvaluator implements PermissionEvaluator {
    private static final Logger log = LoggerFactory.getLogger(LdoDPermissionEvaluator.class);

    public static final String ADMIN = "admin";
    public static final String PARTICIPANT = "participant";
    public static final String PUBLIC = "public";
    public static final String ANNOTATION = "annotation";
    public static final String TAXONOMY = "taxonomy";
    private static final String LOGGED = "logged";

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;

        User loggedUser = User.getAuthenticatedUser();

        String[] permissions = ((String) permission).split("\\.");

        log.debug("hasPermission {}, {}, {}", targetDomainObject, permissions[0], permissions[1]);

        VirtualEdition virtualEdition = null;
        User user = null;
        if (targetDomainObject instanceof String) {
            switch (permissions[0]) {
                case "edition":
                    DomainObject edition = FenixFramework.getDomainObject((String) targetDomainObject);
                    if (edition instanceof VirtualEdition) {
                        virtualEdition = (VirtualEdition) edition;
                    } else {
                        virtualEdition = null;
                    }
                    break;
                case "editionacronym":
                    edition = VirtualModule.getInstance().getVirtualEdition((String) targetDomainObject);
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
                    DomainObject object = FenixFramework.getDomainObject((String) targetDomainObject);
                    if (object instanceof VirtualEditionInter) {
                        virtualEdition = ((VirtualEditionInter) object).getEdition();
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
                    user = UserModule.getInstance().getUser((String) targetDomainObject);
                    break;
                default:
                    assert false;
            }

            if (virtualEdition == null) {
                hasPermission = true;
            } else if (permissions[1].equals(ADMIN)) {
                hasPermission = virtualEdition.getAdminSet().contains(loggedUser.getUsername());
            } else if (permissions[1].equals(PARTICIPANT)) {
                hasPermission = virtualEdition.getParticipantSet().contains(loggedUser.getUsername());
            } else if (permissions[1].equals(PUBLIC)) {
                hasPermission = virtualEdition.isPublicOrIsParticipant();
            } else if (permissions[1].equals(ANNOTATION)) {
                hasPermission = virtualEdition.getTaxonomy().canManipulateAnnotation(loggedUser.getUsername());
            } else if (permissions[1].equals(TAXONOMY)) {
                hasPermission = virtualEdition.getTaxonomy().canManipulateTaxonomy(loggedUser.getUsername());
            }

            if (user != null) {
                if (permissions[1].equals(LOGGED)) {
                    hasPermission = loggedUser.equals(user.getUsername());
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
        log.debug("hasPermission {}, {}", targetId, targetType);


        TextInterface textInterface = new TextInterface();
        FragmentDto fragmentDto = textInterface.getFragmentByXmlId((String) targetId);

        if (fragmentDto == null) {
            return false;
        }

        ScholarInterDto scholarInterDto = fragmentDto.getScholarInterDtoByUrlId(targetType);
        if (scholarInterDto != null) {
            return true;
        }

        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByUrlId(targetType);
        if (virtualEditionInter != null) {
            return virtualEditionInter.getEdition().isPublicOrIsParticipant();
        } else {
            return false;
        }
    }

}
