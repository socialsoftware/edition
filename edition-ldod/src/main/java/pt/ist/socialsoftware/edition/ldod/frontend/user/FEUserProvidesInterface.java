package pt.ist.socialsoftware.edition.ldod.frontend.user;

import pt.ist.socialsoftware.edition.ldod.frontend.user.security.UserModuleUserDetails;

public class FEUserProvidesInterface {

    public String getAuthenticatedUser() {
        UserModuleUserDetails userModuleUserDetails = UserModuleUserDetails.getAuthenticatedUser();
        return userModuleUserDetails != null ? userModuleUserDetails.getUsername() : null;
    }

}
