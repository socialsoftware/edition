package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.domain.Member;

public class MemberDto {

    private String user;
    private String role;

    public MemberDto(Member member) {
        this.user = member.getUser();
        this.role = member.getRole().name();
    }

    public String getUser() {
        return user;
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }

}
