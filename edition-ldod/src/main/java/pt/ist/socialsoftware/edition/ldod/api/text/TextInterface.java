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
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getTitle())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public Heteronym getScholarInterHeteronym(String id){
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getHeteronym())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public LdoDDate getScholarInterDate(String id) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getLdoDDate())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public ScholarInter getScholarInterUsed(String xmlId) {
        return (ScholarInter) Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(xmlId) != null)
                .map(fragment -> fragment.getFragInterByXmlId(xmlId)).findFirst().orElseThrow(LdoDException::new);
    }

    public Edition getExpertEdition(String acronym) {
        return Text.getInstance().getEdition(acronym);
    }

    public LdoDUser getUser(String username){
        return LdoD.getInstance().getUser(username);
    }

    public Set<Fragment> getFragmentsSet(){
        return Text.getInstance().getFragmentsSet();
    }

    //Get fragment knowing its xml id
    public Fragment getFragmentByXmlId(String id){
        return Text.getInstance().getFragmentByXmlId(id);
    }

    //Get Fragment knowing the xml id of one of it's scholar interpretations
    public Fragment getFragmentByInterXmlId(String xmlId) {
        return Text.getInstance().getFragmentsSet().stream().filter(f->f.getFragInterByXmlId(xmlId) != null).findAny().orElse(null);
    }
}
