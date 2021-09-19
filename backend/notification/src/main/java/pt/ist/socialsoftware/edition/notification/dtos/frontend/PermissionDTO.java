package pt.ist.socialsoftware.edition.notification.dtos.frontend;//package pt.ist.socialsoftware.edition.ldod.frontend.utils;

import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> read = new ArrayList<>();
    private List<String> admin = new ArrayList<>();
    private List<String> update = new ArrayList<>();
    private List<String> delete = new ArrayList<>();

    public PermissionDTO() {
    }

    public PermissionDTO(VirtualEditionDto virtualEditionDto, String user) {
        if (!virtualEditionDto.getPub()) {
            for (String participant: virtualEditionDto.getParticipantSet()) {
                this.read.add(participant);
            }
        }

        if (virtualEditionDto.getParticipantSet().contains(user)) {
            this.update.add(user);
        }

        this.delete.add(user);
    }


    public List<String> getRead() {
        return this.read;
    }

    public void setRead(List<String> read) {
        this.read = read;
    }

    public List<String> getAdmin() {
        return this.admin;
    }

    public void setAdmin(List<String> admin) {
        this.admin = admin;
    }

    public List<String> getUpdate() {
        return this.update;
    }

    public void setUpdate(List<String> update) {
        this.update = update;
    }

    public List<String> getDelete() {
        return this.delete;
    }

    public void setDelete(List<String> delete) {
        this.delete = delete;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
