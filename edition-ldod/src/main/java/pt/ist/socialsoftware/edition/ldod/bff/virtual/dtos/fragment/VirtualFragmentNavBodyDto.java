package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment;

import java.util.List;

public class VirtualFragmentNavBodyDto {
    private String currentInterId;

    private String urlId;
    private List<String> veIds;

    private String veId;

    private String interId;

    public VirtualFragmentNavBodyDto() {
    }

    public String getVeId() {
        return veId;
    }

    public void setVeId(String veId) {
        this.veId = veId;
    }

    public String getInterId() {
        return interId;
    }

    public void setInterId(String interId) {
        this.interId = interId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getCurrentInterId() {
        return currentInterId;
    }

    public void setCurrentInterId(String currentInterId) {
        this.currentInterId = currentInterId;
    }

    public List<String> getVeIds() {
        return veIds;
    }

    public void setVeIds(List<String> veIds) {
        this.veIds = veIds;
    }
}
