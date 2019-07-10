package pt.ist.socialsoftware.edition.ldod.api.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.api.text.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TextModule;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.List;

public class UiInterface {
    private static final Logger logger = LoggerFactory.getLogger(UiInterface.class);

    public List<FragInterDto> getFragInterUsed(VirtualEditionInter inter) {
        List<FragInterDto> fragInterList = new ArrayList<>();
        while (inter.getUses() != null) {
            fragInterList.add(new FragInterDto(inter.getUses()));
            inter = inter.getUses();
        }

        fragInterList.add(new FragInterDto(TextModule.getInstance().getScholarInterByXmlId(inter.getUsesScholarInterId())));

        return fragInterList;
    }

    public FragInterDto.InterType getSourceTypeOfInter(String xmlId) {
        TextInterface textInterface = new TextInterface();
        ScholarInterDto scholarInterDto = textInterface.getScholarInter(xmlId);
        if (scholarInterDto != null) {
            return scholarInterDto.getType();
        } else {
            return FragInterDto.InterType.VIRTUAL;
        }
    }

    public String getHeteronymName(String xmlId) {
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

        return textInterface.isExpertEdition(acronym);
    }

    public boolean isVirtualEdition(String acronym) {
        LdoDInterface ldoDInterface = new LdoDInterface();

        return ldoDInterface.getVirtualEdition(acronym) != null;
    }
}
