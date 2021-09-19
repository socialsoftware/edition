package pt.ist.socialsoftware.edition.notification.dtos.text;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DimensionsDto {
    private final float height;
    private final float width;

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
