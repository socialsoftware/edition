package pt.ist.socialsoftware.edition.ldod.api.ui;

import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.List;

public class UiInterface {

    public List<FragInterDto> getFragInterUsed(VirtualEditionInter inter) {
        List<FragInterDto> fragInterList = new ArrayList<>();
        while (inter.getUses() != null){
            fragInterList.add(0,new FragInterDto(inter.getUses()));
            inter = inter.getUses();
        }
        TextInterface textInterface = new TextInterface();
        ScholarInter scholarInterUsed = textInterface.getScholarInterUsed(inter.getUsesFragInter());

        fragInterList.add(0, new FragInterDto(scholarInterUsed));

        return fragInterList;
    }

}
