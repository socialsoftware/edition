package pt.ist.socialsoftware.edition.text.api.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.text.domain.Dimensions;

public class DimensionsDto {
    private final float height;
    private final float width;

    public DimensionsDto(Dimensions dimensions) {
        this.height = dimensions.getHeight();
        this.width = dimensions.getWidth();
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DimensionsDto(@JsonProperty("height") float height, @JsonProperty("width") float width) {
        this.height = height;
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }
}
