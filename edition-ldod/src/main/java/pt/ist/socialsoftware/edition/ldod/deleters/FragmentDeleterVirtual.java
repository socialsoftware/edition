package pt.ist.socialsoftware.edition.ldod.deleters;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.text.deleters.FragmentDeleter;
import pt.ist.socialsoftware.edition.text.domain.Fragment;

import java.util.stream.Collectors;

public class FragmentDeleterVirtual extends FragmentDeleter {

    @Override
    public void remove(Fragment fragment) {
        fragment.getFragmentInterSet().stream()
                .filter(VirtualEditionInter.class::isInstance)
                .map(VirtualEditionInter.class::cast)
                .collect(Collectors.toList()).forEach(inter ->
            inter.remove()
        );
        fragment.getCitationSet().stream().forEach(c -> c.remove());
        super.remove(fragment);
    }
}
