package pt.ist.socialsoftware.edition.ldod.api.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.List;

public class UiInterface {
    private static final Logger logger = LoggerFactory.getLogger(UiInterface.class);

    public enum InterType {
        AUTHORIAL("authorial"), EDITORIAL("editorial"), VIRTUAL("virtual");

        private final String desc;

        InterType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

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

    public InterType getSourceTypeOfInter(String xmlId) {
        TextInterface textInterface = new TextInterface();
        ScholarInter inter = textInterface.getScholarInterUsed(xmlId);
        if (inter != null && inter.isExpertInter()) {
            return InterType.EDITORIAL;
        } else if (inter != null) {
            return InterType.AUTHORIAL;
        } else {
            return InterType.VIRTUAL;
        }
    }

    public String getHetetronymName(String xmlId) {
        TextInterface textInterface = new TextInterface();
        HeteronymDto heteronym = textInterface.getScholarInterHeteronym(xmlId);
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
