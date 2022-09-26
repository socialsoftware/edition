package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class UploadFragmentDto {

    private final String xmlId;
    private final String title;

    private String externalId;

    private boolean uploaded = true;

    private boolean overwritten = false;

    public UploadFragmentDto(Fragment fragment, boolean uploaded, boolean overwritten) {
        this.externalId = fragment.getExternalId();
        this.xmlId = fragment.getXmlId();
        this.title = fragment.getTitle();
        this.overwritten = overwritten;
        this.uploaded = uploaded;
    }

    public String getXmlId() {
        return xmlId;
    }

    public String getTitle() {
        return title;
    }

    public String getExternalId() {
        return externalId;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public boolean isOverwritten() {
        return overwritten;
    }

    @Override
    public String toString() {
        return String.format("[%s(%s)] %s", xmlId, title, isOverwritten() ? "(overwritten)" : "");
    }
}
