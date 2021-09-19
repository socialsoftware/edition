package pt.ist.socialsoftware.edition.text.api.dto;


import pt.ist.socialsoftware.edition.text.domain.Surface;

public class SurfaceDto {

    private String xmlId;

    private String graphic;

    public SurfaceDto(Surface surface){
        setXmlId(surface.getXmlId());
        setGraphic(surface.getGraphic());
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getGraphic() {
        return graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }
}
