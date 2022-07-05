package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.*;

public class InterDto {
    private String xmlId;
    private String urlId;
    private String title;
    private String interExternalId;
    private String shortName;
    private Source.SourceType sourceType;
    private String editor;
    private String fragTitle;
    private String fragExternalId;

    public InterDto() {
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInterExternalId() {
        return interExternalId;
    }

    public void setInterExternalId(String interExternalId) {
        this.interExternalId = interExternalId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public Source.SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(Source.SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getFragTitle() {
        return fragTitle;
    }

    public void setFragTitle(String fragTitle) {
        this.fragTitle = fragTitle;
    }

    public String getFragExternalId() {
        return fragExternalId;
    }

    public void setFragExternalId(String fragExternalId) {
        this.fragExternalId = fragExternalId;
    }

    public static final class InterDtoBuilder {
        private String xmlId;
        private String urlId;
        private String title;
        private String interExternalId;
        private String shortName;
        private Source.SourceType sourceType;
        private String editor;
        private String fragTitle;
        private String fragExternalId;

        public InterDtoBuilder() {
        }

        public InterDtoBuilder xmlId(String xmlId) {
            this.xmlId = xmlId;
            return this;
        }

        public InterDtoBuilder urlId(String urlId) {
            this.urlId = urlId;
            return this;
        }

        public InterDtoBuilder title(String title) {
            this.title = title != null ? title : "";
            return this;
        }


        public InterDtoBuilder interExternalId(String interExternalId) {
            this.interExternalId = interExternalId;
            return this;
        }

        public InterDtoBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }


        public InterDtoBuilder sourceType(FragInter inter) {
            if (inter instanceof SourceInter) this.sourceType = ((SourceInter) inter).getSource().getType();
            return this;
        }

        public InterDtoBuilder editor(FragInter inter) {
            if (inter instanceof ExpertEditionInter) this.editor = ((ExpertEdition) inter.getEdition()).getEditor();
            return this;
        }

        public InterDtoBuilder fragTitle(String fragTitle) {
            this.fragTitle = fragTitle;
            return this;
        }

        public InterDtoBuilder fragExternalId(String fragExternalId) {
            this.fragExternalId = fragExternalId;
            return this;
        }


        public InterDto build() {
            InterDto interDto = new InterDto();
            interDto.xmlId = this.xmlId;
            interDto.title = this.title;
            interDto.editor = this.editor;
            interDto.urlId = this.urlId;
            interDto.fragTitle = this.fragTitle;
            interDto.sourceType = this.sourceType;
            interDto.fragExternalId = this.fragExternalId;
            interDto.interExternalId = this.interExternalId;
            interDto.shortName = this.shortName;
            return interDto;
        }
    }
}
