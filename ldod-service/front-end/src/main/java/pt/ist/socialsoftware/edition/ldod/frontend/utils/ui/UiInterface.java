package pt.ist.socialsoftware.edition.ldod.frontend.utils.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;


import java.util.ArrayList;
import java.util.List;

// TO BE REMOVED SHOULD BE IN THE FRONTEND REQUIRES INTERFACES
public class UiInterface {
    private static final Logger logger = LoggerFactory.getLogger(UiInterface.class);

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

    public List<FragInterDto> getFragInterUsed(VirtualEditionInterDto inter) {
        List<FragInterDto> fragInterList = new ArrayList<>();
        while (inter.getUsesInter() != null) {
            fragInterList.add(new FragInterDto(inter.getUsesInter()));
            inter = inter.getUsesInter();
        }

        fragInterList.add(new FragInterDto(this.feTextRequiresInterface.getScholarInterByXmlId(inter.getUsesScholarInterId())));

        return fragInterList;
    }

    public boolean isExpertEdition(String acronym) {
//        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

        return this.feTextRequiresInterface.isExpertEdition(acronym);
    }

    public boolean isVirtualEdition(String acronym) {


        return new FeVirtualRequiresInterface().getVirtualEditionByAcronym(acronym) != null;
    }
}
