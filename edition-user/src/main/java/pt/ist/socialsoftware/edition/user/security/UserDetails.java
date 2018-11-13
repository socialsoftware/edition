package pt.ist.socialsoftware.edition.user.security;

import org.springframework.social.security.SocialUserDetails;
import pt.ist.socialsoftware.edition.user.domain.User;

public abstract class UserDetails implements SocialUserDetails {

    public abstract User getUser();

}
