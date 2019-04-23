package pt.ist.socialsoftware.edition.ldod.api.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.ArrayList;
import java.util.List;

public class UiInterface {
    private static final Logger logger = LoggerFactory.getLogger(UiInterface.class);

    public List<FragInterDto> getFragInterUsed(VirtualEditionInter inter) {
        List<FragInterDto> fragInterList = new ArrayList<>();
        while (inter.getUses() != null) {
            fragInterList.add(0, new FragInterDto(inter.getUses()));
            inter = inter.getUses();
        }
        TextInterface textInterface = new TextInterface();
        ScholarInter scholarInterUsed = textInterface.getScholarInterUsed(inter.getUsesFragInter());

        fragInterList.add(0, new FragInterDto(scholarInterUsed));

        return fragInterList;
    }

    public Edition.EditionType getSourceTypeOfInter(String xmlId) {
        TextInterface textInterface = new TextInterface();
        ScholarInter inter = textInterface.getScholarInterUsed(xmlId);
        if (inter != null) {
            return inter.getSourceType();
        }
        VirtualEditionInter vei = LdoD.getInstance().getVirtualEditionInterByXmlId(xmlId);
        return vei.getSourceType();
    }

    public String getHetetronymName(String xmlId) {
        TextInterface textInterface = new TextInterface();
        Heteronym heteronym = textInterface.getScholarInterHeteronym(xmlId);
        if (heteronym != null) {
            return heteronym.getName();
        }
        VirtualEditionInter vei = LdoD.getInstance().getVirtualEditionInterByXmlId(xmlId);
        return vei.getHeteronym().getName();
    }

    public boolean isExpertEdition(String acronym) {
        TextInterface textInterface = new TextInterface();

        return textInterface.getExpertEdition(acronym) != null;
    }

    public boolean isVirtualEdition(String acronym) {
        LdoDInterface ldoDInterface = new LdoDInterface();

        return ldoDInterface.getVirtualEdition(acronym) != null;
    }
}
