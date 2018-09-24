package pt.ist.socialsoftware.edition.text.deleters;

import pt.ist.socialsoftware.edition.text.domain.*;

public class FragInterDeleter {

    public void remove(FragInter fragInter) {
        fragInter.setFragment(null);
        fragInter.setHeteronym(null);

        if (fragInter.getLdoDDate() != null) {
            fragInter.getLdoDDate().remove();
        }

        for (RdgText rdg : fragInter.getRdgSet()) {
            fragInter.removeRdg(rdg);
        }

        for (LbText lb : fragInter.getLbTextSet()) {
            fragInter.removeLbText(lb);
        }

        for (PbText pb : fragInter.getPbTextSet()) {
            fragInter.removePbText(pb);
        }

        for (AnnexNote annexNote : fragInter.getAnnexNoteSet()) {
            annexNote.remove();
        }

        for (RefText ref : fragInter.getRefTextSet()) {
            ref.setFragInter(null);
        }
    }
}
