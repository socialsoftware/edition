package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Surface;


public class SurfaceDto {
    private String graphic;

    public SurfaceDto(Surface surface) {
        this.setGraphic(surface.getGraphic());
    }

    public String getGraphic() {
        return graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }
}

