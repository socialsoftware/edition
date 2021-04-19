package pt.ist.socialsoftware.edition.search.feature.options;

import pt.ist.socialsoftware.edition.search.api.dto.TypescriptSearchOptionDto;
import pt.ist.socialsoftware.edition.virtual.api.textdto.SourceDto;

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