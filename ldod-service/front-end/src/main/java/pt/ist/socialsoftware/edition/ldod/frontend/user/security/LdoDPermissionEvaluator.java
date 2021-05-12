package pt.ist.socialsoftware.edition.ldod.frontend.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;

import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;


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

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;

        UserModuleUserDetails userModuleUserDetails = UserModuleUserDetails.getAuthenticatedUser();
        String loggedUsername = userModuleUserDetails != null ? userModuleUserDetails.getUsername() : null;

        String[] permissions = ((String) permission).split("\\.");

        log.debug("hasPermission {}, {}, {}", targetDomainObject, permissions[0], permissions[1]);

        VirtualEditionDto virtualEdition = null;
        UserDto user = null;
        if (targetDomainObject instanceof String) {
            switch (permissions[0]) {
                case "edition":
                    virtualEdition = this.feUserRequiresInterface.getVirtualEditionByExternalId((String) targetDomainObject);
                    break;
                case "editionacronym":
                    virtualEdition = this.feUserRequiresInterface.getVirtualEditionByAcronym((String) targetDomainObject);
                    break;
                case "virtualedition":
                    virtualEdition = this.feUserRequiresInterface.getVirtualEditionByExternalId((String) targetDomainObject);
                    break;
                case "fragInter":
                    VirtualEditionInterDto virtualEditionInter = this.feUserRequiresInterface.getVirtualEditionInterByExternalId((String) targetDomainObject);
                    if (virtualEditionInter != null) {
                        virtualEdition = virtualEditionInter.getVirtualEditionDto();
                    }
                    break;
                case "taxonomy":
                    virtualEdition = this.feUserRequiresInterface.getVirtualEditionOfTaxonomyByExternalId((String) targetDomainObject);
                    break;
                case "category":
                    virtualEdition = this.feUserRequiresInterface.getVirtualEditionOfCategoryByExternalId((String) targetDomainObject);
                    break;
                case "tag":
                    virtualEdition = this.feUserRequiresInterface.getVirtualEditionOfTagByExternalId((String) targetDomainObject);
                    break;
                case "user":
                    user = this.feUserRequiresInterface.getUser((String) targetDomainObject);
                    break;
                default:
                    assert false;
            }

            if (virtualEdition == null) {
                hasPermission = true;
            } else if (permissions[1].equals(ADMIN)) {
                hasPermission = virtualEdition.getAdminSet().contains(loggedUsername);
            } else if (permissions[1].equals(PARTICIPANT)) {
                hasPermission = virtualEdition.getParticipantSet().contains(loggedUsername);
            } else if (permissions[1].equals(PUBLIC)) {
                hasPermission = virtualEdition.isPublicOrIsParticipant(loggedUsername);
            } else if (permissions[1].equals(ANNOTATION)) {
                hasPermission = virtualEdition.canManipulateAnnotation(loggedUsername);
            } else if (permissions[1].equals(TAXONOMY)) {
                hasPermission = virtualEdition.canManipulateTaxonomy(loggedUsername);
            }

            if (user != null) {
                if (permissions[1].equals(LOGGED)) {
                    hasPermission = loggedUsername.equals(user.getUsername());
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

        UserModuleUserDetails userModuleUserDetails = UserModuleUserDetails.getAuthenticatedUser();
        String loggedUsername = userModuleUserDetails != null ? userModuleUserDetails.getUsername() : null;

        FragmentDto fragmentDto = this.feUserRequiresInterface.getFragmentByXmlId((String) targetId);

        if (fragmentDto == null) {
            return false;
        }

        ScholarInterDto scholarInterDto = fragmentDto.getScholarInterDtoByUrlId(targetType);
        if (scholarInterDto != null) {
            return true;
        }

        VirtualEditionInterDto virtualEditionInter = this.feUserRequiresInterface.getVirtualEditionInterByUrlId(targetType);
        if (virtualEditionInter != null) {
            return virtualEditionInter.getVirtualEditionDto().isPublicOrIsParticipant(loggedUsername);
        } else {
            return false;
        }
    }

}
