package pt.ist.socialsoftware.edition.ldod.api.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Objects;

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
}
