package pt.ist.socialsoftware.edition.notification.dtos.visual;

import java.util.ArrayList;
import java.util.List;

public class EditionFragmentsDto {
    List<Fragment4VisualDto> fragments = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public EditionFragmentsDto() {
    }

    public List<Fragment4VisualDto> getFragments() {
        return this.fragments;
    }

    public void setFragments(List<Fragment4VisualDto> fragments) {
        this.fragments = fragments;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
