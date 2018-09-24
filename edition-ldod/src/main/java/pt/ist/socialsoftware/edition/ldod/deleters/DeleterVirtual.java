package pt.ist.socialsoftware.edition.ldod.deleters;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.LinkedList;
import java.util.List;

public class DeleterVirtual {

    public static List<VirtualEditionInter> allUsesVirtualEdition(VirtualEditionInter virtualEditionInter) {
        List<VirtualEditionInter> allUses = new LinkedList<>();
        for (VirtualEditionInter inter : virtualEditionInter.getIsUsedBySet()) {
            allUses.add(inter);
            allUses.addAll(allUsesVirtualEdition(inter));
        }
        return allUses;
    }
}
