package pt.ist.socialsoftware.edition.ldod.api.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class UserInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserInterface.class);

    public LdoDUser getUser(String username) {
        return LdoD.getInstance().getUser(username);
    }

}
