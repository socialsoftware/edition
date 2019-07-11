package pt.ist.socialsoftware.edition.ldod.api.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.TextModule;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

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
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
        ScholarInterDto scholarInterDto = textProvidesInterface.getScholarInter(xmlId);
        if (scholarInterDto != null) {
            return scholarInterDto.getType();
        } else {
            return FragInterDto.InterType.VIRTUAL;
        }
    }

    public String getHeteronymName(String xmlId) {
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
        HeteronymDto heteronym = textProvidesInterface.getScholarInterHeteronym(xmlId);
        if (heteronym != null) {
            return heteronym.getName();
        }
        VirtualEditionInter vei = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);
        return vei.getHeteronym().getName();
    }

    public boolean isExpertEdition(String acronym) {
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

        return textProvidesInterface.isExpertEdition(acronym);
    }

    public boolean isVirtualEdition(String acronym) {
        VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

        return virtualProvidesInterface.getVirtualEdition(acronym) != null;
    }
}
