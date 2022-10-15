package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;


public class VeUserDto {

    private String username;
    private String firstname;
    private String lastname;
    private String email;

    private String externalId;

    private boolean canSwitchRole;

    private boolean canBeRemoved;
    private boolean pending;
    private boolean active;
    private boolean admin;
    private boolean canBeAdded;


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

    public String getExternalId() {
        return externalId;
    }

    public boolean isCanSwitchRole() {
        return canSwitchRole;
    }

    public void setCanSwitchRole(boolean canSwitchRole) {
        this.canSwitchRole = canSwitchRole;
    }

    public boolean isCanBeRemoved() {
        return canBeRemoved;
    }

    public void setCanBeRemoved(boolean canBeRemoved) {
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


    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    public boolean isCanBeAdded() {
        return canBeAdded;
    }

    public void setCanBeAdded(boolean canBeAdded) {
        this.canBeAdded = canBeAdded;
    }


}
