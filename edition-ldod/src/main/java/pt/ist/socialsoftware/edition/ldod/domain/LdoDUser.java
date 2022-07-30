package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.LdoDUserDto;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.dto.ldodMfes.SignupDto;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateUsernameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LdoDUser extends LdoDUser_Base {
    private static Logger logger = LoggerFactory.getLogger(LdoDUser.class);

    public enum SocialMediaService {
        TWITTER, FACEBOOK, LINKEDIN, GOOGLE
    }

    ;

    @Override
    public void setFirstName(String firstName) {
        if (!firstName.matches("^[\\p{L}\\s]+$")) {
            throw new LdoDException(firstName);
        }
        super.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        if (!lastName.matches("^[\\p{L}\\s]+$")) {
            throw new LdoDException(lastName);
        }
        super.setLastName(lastName);
    }

    @Override
    public void setUsername(String username) {
        if (!username.matches("^[A-Za-z0-9_\\s\\-]+$")) {
            throw new LdoDException(username);
        }
        checkUniqueUsername(username);
        super.setUsername(username);
    }

    public static LdoDUser getAuthenticatedUser() {
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

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        getMemberSet().stream().forEach(m -> m.remove());
        getSelectedVirtualEditionsSet().stream().forEach(ve -> removeSelectedVirtualEditions(ve));
        getTagSet().stream().forEach(ut -> ut.remove());
        getAnnotationSet().stream().forEach(a -> a.remove());
        getRecommendationWeightsSet().stream().forEach(rw -> rw.remove());

        getLdoD().getUserConnectionSet().stream().filter(uc -> uc.getUserId().equals(getUsername()))
                .forEach(uc -> uc.remove());
        if (getToken() != null) {
            getToken().remove();
        }
        getRolesSet().stream().forEach(r -> removeRoles(r));
        setLdoD(null);

        if (getPlayer() != null) {
            getPlayer().remove();
        }
        getResponsibleForGamesSet().stream().forEach(g -> g.remove());

        deleteDomainObject();
    }

    public LdoDUser(LdoD ldoD, String username, String password, String firstName, String lastName, String email) {
        setEnabled(false);
        setActive(true);
        setLdoD(ldoD);
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public LdoDUser(LdoD ldoD, String password, SignupDto signupDto) {
        setEnabled(false);
        setActive(true);
        setLdoD(ldoD);
        setUsername(signupDto.getUsername());
        setPassword(password);
        setFirstName(signupDto.getFirstName());
        setLastName(signupDto.getLastName());
        setEmail(signupDto.getEmail());
    }

    private void checkUniqueUsername(String username) {
        if (getLdoD()
                .getUsersSet()
                .stream()
                .anyMatch(ldoDUser -> ldoDUser.getUsername() != null && ldoDUser.getUsername().equals(username))) {
            throw new LdoDDuplicateUsernameException(username);
        }
    }

    public List<VirtualEditionInter> getFragInterSet() {
        Set<VirtualEditionInter> inters = new HashSet<>();

        for (Annotation annotation : getAnnotationSet()) {
            if (annotation.getVirtualEditionInter().getVirtualEdition().checkAccess()) {
                inters.add(annotation.getVirtualEditionInter());
            }
        }

        for (Tag tag : getTagSet()) {
            if (tag.getInter().getVirtualEdition().checkAccess()) {
                inters.add(tag.getInter());
            }
        }

        return inters.stream().sorted((i1, i2) -> i1.getTitle().compareTo(i2.getTitle())).collect(Collectors.toList());
    }

    public List<VirtualEdition> getPublicEditionList() {
        return getMemberSet().stream().map(Member_Base::getVirtualEdition).filter(Edition_Base::getPub).distinct()
                .sorted(Comparator.comparing(VirtualEdition::getTitle)).collect(Collectors.toList());
    }

    public RecommendationWeights getRecommendationWeights(VirtualEdition virtualEdition) {
        for (RecommendationWeights recommendationWeights : getRecommendationWeightsSet()) {
            if (recommendationWeights.getVirtualEdition() == virtualEdition) {
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
        if (getToken() != null) {
            getToken().remove();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void updatePassword(PasswordEncoder passwordEncoder, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, getPassword())) {
            throw new LdoDException();
        }

        setPassword(passwordEncoder.encode(newPassword));
    }

    public String getListOfRolesAsStrings() {
        return getRolesSet().stream().map(r -> r.getType().name()).collect(Collectors.joining(", "));
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchActive() {
        if (getActive()) {
            setActive(false);
        } else {
            setActive(true);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void update(PasswordEncoder passwordEncoder, String oldUsername, String newUsername, String firstName,
                       String lastName, String email, String newPassword, boolean isUser, boolean isAdmin, boolean isEnabled) {

        if (!oldUsername.equals(newUsername)) {
            changeUsername(oldUsername, newUsername);
        }

        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);

        getRolesSet().clear();

        if (isUser) {
            addRoles(Role.getRole(RoleType.ROLE_USER));
        }

        if (isAdmin) {
            addRoles(Role.getRole(RoleType.ROLE_ADMIN));
        }

        setEnabled(isEnabled);

        if (newPassword != null && !newPassword.trim().equals("")) {
            setPassword(passwordEncoder.encode(newPassword));
        }

    }

    @Atomic(mode = TxMode.WRITE)
    public void update(PasswordEncoder passwordEncoder, LdoDUserDto user) {

        if (!user.getOldUsername().equals(user.getNewUsername())) {
            changeUsername(user.getOldUsername(), user.getNewUsername());
        }

        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());

        getRolesSet().clear();

        if (user.isUser()) {
            addRoles(Role.getRole(RoleType.ROLE_USER));
        }

        if (user.isAdmin()) {
            addRoles(Role.getRole(RoleType.ROLE_ADMIN));
        }

        setEnabled(user.isEnabled());

        if (user.getNewPassword() != null && !user.getNewPassword().trim().equals("")) {
            setPassword(passwordEncoder.encode(user.getNewPassword()));
        }

    }

    private void changeUsername(String oldUsername, String newUsername) {
        setUsername(newUsername);

        UserConnection userConnection = getLdoD().getUserConnectionSet().stream()
                .filter(uc -> uc.getUserId().equals(oldUsername)).findFirst().orElse(null);

        if (userConnection != null) {
            userConnection.setUserId(newUsername);
        }

    }

}
