package pt.ist.socialsoftware.edition.ldod.bff.user.dtos;

import java.util.List;

public class ManageUsersDto {
    private boolean ldoDAdmin;
    private List<LdoDUserDto> userList;
    private List<SessionDto> sessionList;

    private ManageUsersDto() {
    }

    public boolean isLdoDAdmin() {
        return ldoDAdmin;
    }

    public void setLdoDAdmin(boolean ldoDAdmin) {
        this.ldoDAdmin = ldoDAdmin;
    }

    public List<LdoDUserDto> getUserList() {
        return userList;
    }

    public void setUserList(List<LdoDUserDto> userList) {
        this.userList = userList;
    }

    public List<SessionDto> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<SessionDto> sessionList) {
        this.sessionList = sessionList;
    }


    public static final class ManageUsersDtoBuilder {
        private ManageUsersDto manageUsersDto;

        private ManageUsersDtoBuilder() {
            manageUsersDto = new ManageUsersDto();
        }

        public static ManageUsersDtoBuilder aManageUsersDto() {
            return new ManageUsersDtoBuilder();
        }

        public ManageUsersDtoBuilder ldoDAdmin(boolean ldoDAdmin) {
            manageUsersDto.setLdoDAdmin(ldoDAdmin);
            return this;
        }

        public ManageUsersDtoBuilder userList(List<LdoDUserDto> userList) {
            manageUsersDto.setUserList(userList);
            return this;
        }

        public ManageUsersDtoBuilder sessionList(List<SessionDto> sessionList) {
            manageUsersDto.setSessionList(sessionList);
            return this;
        }

        public ManageUsersDto build() {
            return manageUsersDto;
        }
    }
}
