package pt.ist.socialsoftware.edition.ldod.deleters;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.text.deleters.FragInterDeleter;
import pt.ist.socialsoftware.edition.text.domain.FragInter;

public class FragInterDeleterVirtual extends FragInterDeleter {

    @Override
    public void remove(FragInter fragInter) {
        for (VirtualEditionInter inter : fragInter.getIsUsedBySet()) {
            fragInter.removeIsUsedBy(inter);
        }

        fragInter.getInfoRangeSet().forEach(infoRange -> infoRange.remove());

        super.remove(fragInter);
    }
}
