package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.Surface;


public class SurfaceDto {
    private String graphic;
    private String pbText;

    public SurfaceDto(Surface surface) {
        setGraphic(surface.getGraphic());
        setPbText(surface.getPbTextSet().stream().filter(pbText -> pbText.getSurface().equals(surface)).findFirst().orElse(null));
    }


    public String getPbText() {
        return pbText;
    }

    public void setPbText(PbText pbText) {
        this.pbText = pbText != null ? pbText.getExternalId() : "";
    }

    public String getGraphic() {
        return graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }

}

