package pt.ist.socialsoftware.edition.ldod.api.user.dto;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class LdoDUserDto {
    private String username;

    public LdoDUserDto(LdoDUser ldoDUser) {
        setUsername(ldoDUser.getUsername());
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
