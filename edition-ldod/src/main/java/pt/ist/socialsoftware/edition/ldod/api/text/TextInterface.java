package pt.ist.socialsoftware.edition.ldod.api.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TextInterface {
    private static Logger logger = LoggerFactory.getLogger(TextInterface.class);


    public String getScholarInterTitle(String id){
        return LdoD.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getTitle())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public Heteronym getScholarInterHeteronym(String id){
        return LdoD.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getHeteronym())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public LdoDDate getScholarInterDate(String id) {
        return LdoD.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getLdoDDate())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public ScholarInter getScholarInterUsed(String id) {
        return (ScholarInter) LdoD.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id)).findFirst().orElseThrow(LdoDException::new);
    }

    public Edition getExpertEdition(String acronym) {
        return Text.getInstance().getEdition(acronym);
    }

    public LdoDUser getUser(String username){
        return LdoD.getInstance().getUser(username);
    }

    public Set<VirtualEditionInter> getVirtualEditionIntersForFragment(String id) {
        Fragment frag = LdoD.getInstance().getFragmentsSet().stream().filter(fragment -> fragment.getXmlId().equals(id))
                .findFirst().orElseThrow(LdoDException::new);

        return frag.getFragmentInterSet().stream().filter(VirtualEditionInter.class::isInstance)
                .map(VirtualEditionInter.class::cast).collect(Collectors.toSet());
    }

}
