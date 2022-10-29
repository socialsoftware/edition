package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.Category_Base;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

import java.util.List;
import java.util.stream.Collectors;


public class VeUserDto {

    private String username;
    private String firstname;
    private String lastname;
    private String email;

    private String externalId;

    private Boolean canSwitchRole;

    private Boolean canBeRemoved;
    private Boolean pending;
    private Boolean active;
    private Boolean admin;
    private Boolean canBeAdded;

    private List<VirtualEditionDto> publicVirtualEditions;
    private List<ClassGameDto> gameDtos;

    private List<FragInterDto> fragInters;
    private Double score;
    private Integer position;

    public VeUserDto() {
    }

    public VeUserDto(LdoDUser user, VirtualEdition ve, LdoDUser actor) {
        setExternalId(user.getExternalId());
        setUsername(user.getUsername());
        setFirstname(user.getFirstName());
        setLastname(user.getLastName());
        setEmail(user.getEmail());
        setActive(ve.getParticipantSet().contains(user));
        setPending(ve.getPendingSet().contains(user));
        setAdmin(ve.getAdminSet().contains(user));
        setCanBeAdded(!isActive() && !isPending());
        if (ve.getMember(user) != null && ve.getMember(actor) != null) {
            setCanSwitchRole(ve.canSwitchRole(actor, user));
            setCanBeRemoved(ve.canRemoveMember(actor, user));
        }

    }

    public VeUserDto(LdoDUser user) {
        setExternalId(user.getExternalId());
        setUsername(user.getUsername());
        setFirstname(user.getFirstName());
        setLastname(user.getLastName());
        setPublicVirtualEditions(user.getPublicEditionList().stream().map(VirtualEditionDto::new).collect(Collectors.toList()));
        setPosition(LdoD.getInstance().getOverallUserPosition(user.getUsername()));
        setFragInters(user.getFragInterSet()
                .stream()
                .map(inter -> VirtualEditionInterDto.VirtualEditionInterDtoBuilder.aVirtualEditionInterDto(inter)
                        .usedList(inter.getListUsed().stream().map(VirtualEditionInterDto::new).collect(Collectors.toList()))
                        .categories(inter.getAssignedCategories().stream().map(Category_Base::getName).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList()));
        if (user.getPlayer() != null) {
            setScore(user.getPlayer().getScore());
            setGameDtos(user.getPlayer()
                    .getClassificationGameParticipantSet()
                    .stream()
                    .map(game -> new ClassGameDto(game.getClassificationGame(), user))
                    .collect(Collectors.toList()));
        }


    }

    public Double getScore() {
        return score;
    }

    public List<FragInterDto> getFragInters() {
        return fragInters;
    }

    public void setFragInters(List<FragInterDto> fragInters) {
        this.fragInters = fragInters;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<VirtualEditionDto> getPublicVirtualEditions() {
        return publicVirtualEditions;
    }

    public void setPublicVirtualEditions(List<VirtualEditionDto> publicVirtualEditions) {
        this.publicVirtualEditions = publicVirtualEditions;
    }

    public List<ClassGameDto> getGameDtos() {
        return gameDtos;
    }

    public void setGameDtos(List<ClassGameDto> gameDtos) {
        this.gameDtos = gameDtos;
    }

    public String getExternalId() {
        return externalId;
    }

    public Boolean isCanSwitchRole() {
        return canSwitchRole;
    }

    public void setCanSwitchRole(Boolean canSwitchRole) {
        this.canSwitchRole = canSwitchRole;
    }

    public Boolean isCanBeRemoved() {
        return canBeRemoved;
    }

    public void setCanBeRemoved(Boolean canBeRemoved) {
        this.canBeRemoved = canBeRemoved;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean isPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "VeUserDto{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", externalId='" + externalId + '\'' +
                ", canSwitchRole=" + canSwitchRole +
                ", canBeRemoved=" + canBeRemoved +
                ", pending=" + pending +
                ", active=" + active +
                ", admin=" + admin +
                ", canBeAdded=" + canBeAdded +
                ", publicVirtualEditions=" + publicVirtualEditions +
                ", gameDtos=" + gameDtos +
                ", fragInters=" + fragInters +
                ", score=" + score +
                ", position=" + position +
                '}';
    }

    public Boolean isCanBeAdded() {
        return canBeAdded;
    }

    public void setCanBeAdded(Boolean canBeAdded) {
        this.canBeAdded = canBeAdded;
    }


    public static final class VeUserDtoBuilder {
        private VeUserDto veUserDto;

        private VeUserDtoBuilder() {
            veUserDto = new VeUserDto();
        }

        public static VeUserDtoBuilder aVeUserDto() {
            return new VeUserDtoBuilder();
        }

        public VeUserDtoBuilder username(String username) {
            veUserDto.setUsername(username);
            return this;
        }

        public VeUserDtoBuilder firstname(String firstname) {
            veUserDto.setFirstname(firstname);
            return this;
        }

        public VeUserDtoBuilder lastname(String lastname) {
            veUserDto.setLastname(lastname);
            return this;
        }

        public VeUserDto build() {
            return veUserDto;
        }
    }
}
