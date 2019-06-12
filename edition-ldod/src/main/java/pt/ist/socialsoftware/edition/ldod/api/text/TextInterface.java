package pt.ist.socialsoftware.edition.ldod.api.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.LdoDDateDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Objects;
import java.util.Set;

public class TextInterface {
    private static final Logger logger = LoggerFactory.getLogger(TextInterface.class);


    public String getScholarInterTitle(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(scholarInterId).getTitle())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public HeteronymDto getScholarInterHeteronym(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(scholarInterId).getHeteronym())
                .filter(Objects::nonNull).map(HeteronymDto::new).findFirst().orElse(null);
    }

    public LdoDDateDto getScholarInterDate(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(scholarInterId).getLdoDDate())
                .filter(Objects::nonNull).findFirst().map(LdoDDateDto::new).orElse(null);
    }

    public ScholarInter getScholarInterUsed(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(scholarInterId)).findFirst().orElse(null);
    }

    public ExpertEdition getExpertEdition(String acronym) {
        return Text.getInstance().getExpertEdition(acronym);
    }

    public LdoDUser getUser(String username) {
        return LdoD.getInstance().getUser(username);
    }

    public Set<Fragment> getFragmentsSet() {
        return Text.getInstance().getFragmentsSet();
    }

    //Get fragment knowing its xml id
    public Fragment getFragmentByXmlId(String id) {
        return Text.getInstance().getFragmentByXmlId(id);
    }

    //Get Fragment knowing the xml id of one of it's scholar interpretations
    public Fragment getFragmentByInterXmlId(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream().filter(f -> f.getScholarInterByXmlId(scholarInterId) != null).findAny().orElse(null);
    }

    public boolean isSourceInter(String scholarInterId) {
        ScholarInter inter = Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId))
                .findFirst().orElse(null);
        return inter != null && !inter.isExpertInter();
    }

    public Source getSourceOfInter(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId))
                .map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource()).findFirst().orElseThrow(LdoDException::new);
    }

    public boolean usesSourceType(String scholarInterId, Source.SourceType type) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId))
                .map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource().getType()).findFirst().orElseThrow(LdoDException::new) == type;
    }

    public String getEditionAcronymOfInter(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId))
                .map(scholarInter -> scholarInter.getEdition().getAcronym()).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public boolean isExpertInter(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId))
                .findFirst().orElseThrow(LdoDException::new).isExpertInter();
    }

    public String getHeteronymId(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId).getHeteronym().getXmlId())
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    public String getInterSourceType(String scholarInterId) {
        return Text.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null).map(fragment -> fragment.getScholarInterByXmlId(scholarInterId))
                .map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource().getType().toString()).findFirst().orElseThrow(LdoDException::new);
    }

    public String getExpertEditionEditor(String expertEditionInterId) {
        return Text.getInstance().getExpertEditionsSet().stream().filter(expertEdition -> expertEdition.getFragInterByXmlId(expertEditionInterId) != null)
                .findFirst().orElseThrow(LdoDException::new).getEditor();
    }

    public boolean acronymExists(String acronym) {
        return Text.getInstance().getExpertEditionsSet().stream().map(ExpertEdition::getAcronym)
                .anyMatch(s -> s.equals(acronym));
    }
}
