package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import pt.ist.socialsoftware.edition.ldod.frontend.search.dto.TypescriptSearchOptionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;

public final class TypescriptSearchOption extends AuthoralSearchOption {
    public static final String TYPESCRIPT = "datil";

    public TypescriptSearchOption(TypescriptSearchOptionDto typescriptSearchOptionDto) {
        super(typescriptSearchOptionDto.getHasLdoD(), typescriptSearchOptionDto.getDateSearchOption().createSearchOption());
    }

    @Override
    protected boolean isOfDocumentType(SourceDto source) {
        return source.hasTypeNoteSet();
    }

}