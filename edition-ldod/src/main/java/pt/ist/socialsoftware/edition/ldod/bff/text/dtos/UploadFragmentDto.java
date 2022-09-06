package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;

public class UploadFragmentDto {

    private final String xmlId;
    private final String title;

    private String externalId;

    private boolean uploaded = true;

    private boolean overwritten = false;

    public UploadFragmentDto(String xmlId, String title, boolean uploaded, boolean overwritten) {
        this.xmlId = xmlId;
        this.title = title;
        this.uploaded = uploaded;
        this.overwritten = overwritten;
    }

    public UploadFragmentDto(Fragment fragment) {
        this.externalId = fragment.getExternalId();
        this.xmlId = fragment.getXmlId();
        this.title = fragment.getTitle();
        this.uploaded = false;
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
