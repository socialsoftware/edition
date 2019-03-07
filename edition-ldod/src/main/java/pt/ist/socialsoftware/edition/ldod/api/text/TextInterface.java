package pt.ist.socialsoftware.edition.ldod.api.text;

import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class TextInterface {

    public String getScholarInterTitle(String id){
        return LdoD.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id).getTitle()).findFirst().orElseThrow(LdoDException::new);
    }

    public ScholarInter getScholarInterUsed(String id) {
        return (ScholarInter) LdoD.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getFragInterByXmlId(id) != null)
                .map(fragment -> fragment.getFragInterByXmlId(id)).findFirst().orElseThrow(LdoDException::new);
    }
}
