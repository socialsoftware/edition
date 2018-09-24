package pt.ist.socialsoftware.edition.ldod.deleters;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.text.deleters.SourceInterDeleter;
import pt.ist.socialsoftware.edition.text.domain.SourceInter;

public class SourceInterDeleterVirtual extends SourceInterDeleter {

    @Override
    public void remove(SourceInter sourceInter) {
        super.remove(sourceInter);
        // it is necessary to remove all interpretations that use the expert
        // interpretation
        for (VirtualEditionInter inter : sourceInter.getIsUsedBySet()) {
            for (VirtualEditionInter interUses: DeleterVirtual.allUsesVirtualEdition(inter)) {
                interUses.remove();
            }
            inter.remove();
        }
    }
}
