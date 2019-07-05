package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class EditionFragmentsDto {
    List<FragmentViewDto> fragments = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public EditionFragmentsDto() {
    }

    public List<FragmentViewDto> getFragments() {
        return this.fragments;
    }

    public void setFragments(List<FragmentViewDto> fragments) {
        this.fragments = fragments;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
