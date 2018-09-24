package pt.ist.socialsoftware.edition.text.deleters;

import pt.ist.socialsoftware.edition.text.domain.FragInter;
import pt.ist.socialsoftware.edition.text.domain.Fragment;
import pt.ist.socialsoftware.edition.text.domain.RefText;
import pt.ist.socialsoftware.edition.text.domain.Source;

public class FragmentDeleter {
    public void remove(Fragment fragment) {
        fragment.setCollectionManager(null);

        fragment.getTextPortion().remove();

        for (FragInter inter : fragment.getFragmentInterSet()) {
            inter.remove();
        }

        for (Source source : fragment.getSourcesSet()) {
            source.remove();
        }

        for (RefText ref : fragment.getRefTextSet()) {
            // the reference is removed
            ref.remove();
        }

    }
}
