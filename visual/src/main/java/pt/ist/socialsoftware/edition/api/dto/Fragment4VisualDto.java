package pt.ist.socialsoftware.edition.api.dto;


import pt.ist.socialsoftware.edition.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.api.virtualDto.VirtualEditionInterDto;

public class Fragment4VisualDto {
    private String interId;
    private FragmentMetaInfoDto meta;
    String text;

    public Fragment4VisualDto(ScholarInterDto inter, String text) {
        this.interId = inter.getExternalId();
        this.meta = new FragmentMetaInfoDto(inter);
        this.text = text;
    }

    public Fragment4VisualDto(VirtualEditionInterDto inter, String text) {
        this.interId = inter.getExternalId();
        this.meta = new FragmentMetaInfoDto(inter);
        this.text = text;
    }

    public String getInterId() {
        return this.interId;
    }

    public void setInterId(String interId) {
        this.interId = interId;
    }

    public FragmentMetaInfoDto getMeta() {
        return this.meta;
    }

    public void setMeta(FragmentMetaInfoDto meta) {
        this.meta = meta;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
