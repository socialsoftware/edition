package pt.ist.socialsoftware.edition.ldod.api.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Objects;
import java.util.Set;

public class TextInterface {
    private static Logger logger = LoggerFactory.getLogger(TextInterface.class);


    public String getScholarInterTitle(String id){
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(id) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(id).getTitle())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public Heteronym getScholarInterHeteronym(String id){
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(id) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(id).getHeteronym())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public LdoDDate getScholarInterDate(String id) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(id) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(id).getLdoDDate())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public ScholarInter getScholarInterUsed(String xmlId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(xmlId)).findFirst().orElse(null);
    }

    public ExpertEdition getExpertEdition(String acronym) {
        return Text.getInstance().getExpertEdition(acronym);
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
        return Text.getInstance().getFragmentsSet().stream().filter(f->f.getScholarInterByXmlId(xmlId) != null).findAny().orElse(null);
    }

    public boolean isSourceInter(String xmlId) {
        ScholarInter inter = Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId))
                .findFirst().orElse(null);
        return inter != null && inter.getSourceType().equals(Edition.EditionType.AUTHORIAL);
    }

    public Source getSourceOfInter(String xmlId) {
        return  Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId))
                .map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource()).findFirst().orElseThrow(LdoDException::new);
    }

    public boolean usesSourceType(String xmlId, Source.SourceType type) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId))
                .map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource().getType()).findFirst().orElseThrow(LdoDException::new) == type;
    }

    public String getEditionAcronymOfInter(String xmlId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId))
                .map(scholarInter -> scholarInter.getEdition().getAcronym()).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public boolean isExpertInter(String xmlId){
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId))
                .map(FragInter::getSourceType).findFirst().orElseThrow(LdoDException::new).equals(Edition.EditionType.EDITORIAL);
    }

    public String getHeteronymId(String xmlId){
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId).getHeteronym().getXmlId())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public String getInterSourceType(String xmlId) {
        return  Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId))
                .map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource().getType().toString()).findFirst().orElseThrow(LdoDException::new);
    }
}
