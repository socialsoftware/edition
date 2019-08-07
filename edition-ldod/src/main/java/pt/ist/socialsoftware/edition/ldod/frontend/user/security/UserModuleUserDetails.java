package pt.ist.socialsoftware.edition.ldod.frontend.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.SocialUserDetails;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;

import java.util.Collection;
import java.util.HashSet;

public class UserModuleUserDetails implements SocialUserDetails {
    private static final Logger log = LoggerFactory.getLogger(UserModuleUserDetails.class);

    private static final long serialVersionUID = -6509897037222767090L;

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();

    public static UserModuleUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserModuleUserDetails) {
                return (UserModuleUserDetails) principal;
            }
        }
        return null;
    }

    private Collection<GrantedAuthority> authorities = new HashSet<>();
    private final String password;
    private final String username;
    private final String userId;

    public UserModuleUserDetails(User user, String username, String password, Collection<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.userId = user.getExternalId();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUserId() {
        return getUser().getSocialMediaId();
    }

    public UserDto getUser() {
        return this.feUserRequiresInterface.getUser(this.username);
    }

    public boolean hasRole(String role) {
        return getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

}