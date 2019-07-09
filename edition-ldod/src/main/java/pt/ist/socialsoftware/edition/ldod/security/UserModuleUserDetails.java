package pt.ist.socialsoftware.edition.ldod.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.User;

import java.util.Collection;
import java.util.HashSet;

public class UserModuleUserDetails implements SocialUserDetails {
    private static final Logger log = LoggerFactory.getLogger(UserModuleUserDetails.class);

    private static final long serialVersionUID = -6509897037222767090L;

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

    public User getUser() {
        // log.debug("getUser userId:{}", userId);
        return (User) FenixFramework.getDomainObject(this.userId);
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

    public boolean hasRole(String role) {
        return getAuthorities().stream().filter(a -> a.getAuthority().equals(role)).findFirst().isPresent();
    }

}