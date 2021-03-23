package pt.ist.socialsoftware.edition.text.api.dto;


import pt.ist.socialsoftware.edition.text.domain.Dimensions;

public class DimensionsDto {
    private final float height;
    private final float width;

    public DimensionsDto(Dimensions dimensions) {
        this.height = dimensions.getHeight();
        this.width = dimensions.getWidth();
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }
}
