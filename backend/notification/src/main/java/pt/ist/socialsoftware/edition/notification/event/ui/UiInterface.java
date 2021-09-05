package pt.ist.socialsoftware.edition.notification.event.ui;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
////import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
////import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
////import pt.ist.socialsoftware.edition.text.domain.TextModule;
////import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//// TO BE REMOVED SHOULD BE IN THE FRONTEND REQUIRES INTERFACES
//public class UiInterface {
//    private static final Logger logger = LoggerFactory.getLogger(UiInterface.class);
//
//    public List<FragInterDto> getFragInterUsed(VirtualEditionInter inter) {
//        List<FragInterDto> fragInterList = new ArrayList<>();
//        while (inter.getUses() != null) {
//            fragInterList.add(new FragInterDto(inter.getUses()));
//            inter = inter.getUses();
//        }
//
//        fragInterList.add(new FragInterDto(TextModule.getInstance().getScholarInterByXmlId(inter.getUsesScholarInterId())));
//
//        return fragInterList;
//    }
//
//    public boolean isExpertEdition(String acronym) {
//        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
//
//        return textProvidesInterface.isExpertEdition(acronym);
//    }
//
//    public boolean isVirtualEdition(String acronym) {
//        VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();
//
//        return virtualProvidesInterface.getVirtualEdition(acronym) != null;
//    }
//}
