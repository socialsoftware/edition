package pt.ist.socialsoftware.edition.ldod.visual.api.dto;

import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.ArrayList;
import java.util.List;

public class FragmentMetaInfoDto {
    private String title;
    private String heteronym;
    private String date;
    private boolean hasLdoDLabel;
    private List<String> categories = new ArrayList<>();

    public FragmentMetaInfoDto(ScholarInterDto inter) {
        this.title = inter.getFragmentDto().getTitle();

        if (inter.getLdoDDate() != null) {
            this.date = inter.getLdoDDate().getDate().toString();
        }

        if (inter.getHeteronym() != null && !inter.getHeteronym().isNullHeteronym()) {
            this.heteronym = inter.getHeteronym().getName();
        }

        if (inter.isSourceInter()) {
            SourceDto sourceDto = inter.getSourceDto();
            if (sourceDto.getType() == Source.SourceType.MANUSCRIPT) {
                this.hasLdoDLabel = sourceDto.getHasLdoDLabel();
            }
        }
    }

    public FragmentMetaInfoDto(VirtualEditionInterDto inter) {
        this(inter.getLastUsed());

        this.categories = inter.getSortedCategories();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeteronym() {
        return this.heteronym;
    }

    public void setHeteronym(String heteronym) {
        this.heteronym = heteronym;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHasLdoDLabel() {
        return this.hasLdoDLabel;
    }

    public void setHasLdoDLabel(boolean hasLdoDLabel) {
        this.hasLdoDLabel = hasLdoDLabel;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
