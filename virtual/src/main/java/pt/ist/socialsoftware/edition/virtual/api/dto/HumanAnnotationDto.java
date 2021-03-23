package pt.ist.socialsoftware.edition.virtual.api.dto;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.virtual.domain.Tag;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.List;

public class HumanAnnotationDto extends AnnotationDto {

    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    private String text;
    private List<TagDto> tags;

    public HumanAnnotationDto(HumanAnnotation annotation, VirtualEditionInter inter) {
        super(annotation);

        setText(annotation.getText());
        List<TagDto> tags = new ArrayList<>();
        for (Tag tag : annotation.getTagSet()) {
            tags.add(new TagDto(tag, inter));
        }
        setTags(tags);

    }

    public static boolean canCreate(String acronym, String username) {
        VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();
        return virtualProvidesInterface.canCreateHumanAnnotationOnVirtualEdition(acronym, username);
    }

    public boolean canUpdate(String id, String username) {
        return this.virtualProvidesInterface.canUserUpdateHumanAnnotation(id, username);
    }

    public void update(String text, List<String> tags) {
        this.virtualProvidesInterface.updateHumanAnnotation(getExternalId(), text, tags);
    }

    public boolean canDelete(String username) {
       return this.virtualProvidesInterface.canUserDeleteHumanAnnotation(getExternalId(), username);
    }

    public void remove() {
        this.virtualProvidesInterface.removeHumanAnnotation(getExternalId());
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TagDto> getTags() {
        return this.tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean isHumanAnnotation() {
        return true;
    }
}
