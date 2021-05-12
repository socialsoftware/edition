package pt.ist.socialsoftware.edition.virtual.utils;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.domain.Member;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import pt.ist.socialsoftware.edition.virtual.feature.topicmodeling.TopicModeler;

public class VirtualBootstrap {

    private static final String ARS = "ars";


    public static boolean initializeVirtualModule() {

        boolean virtualCreate = false;
        if (VirtualModule.getInstance() == null) {
            new VirtualModule();
            cleanTopicModeler();
            cleanLucene();
            createVirtualEditionsForTest();
            createLdoDArchiveVirtualEdition();
            virtualCreate = true;
        }
        VirtualRequiresInterface.getInstance();
        return virtualCreate;
    }

    private static void cleanTopicModeler() {
        TopicModeler topicModeler = new TopicModeler();
        topicModeler.cleanDirectory();
    }

    private static void cleanLucene() {
        VirtualRequiresInterface.getInstance().cleanLucene();
    }

    public static void createVirtualEditionsForTest() {
//        logger.debug("createVirtualEditionsForTest size{}", UserModule.getInstance().getUsersSet().size());

        // User ars = ldod.getUser("ars");
        // User diego = ldod.getUser("diego");
        // User mp = ldod.getUser("mp");
        // User tiago = ldod.getUser("tiago");
        // User nuno = ldod.getUser("nuno");
        // User luis = ldod.getUser("luis");
        // User andre = ldod.getUser("afs");
        // User daniela = ldod.getUser("daniela");
        // User joana = ldod.getUser("joana");
        // User bernardosoares = ldod.getUser("bernardosoares");
        // User rita = ldod.getUser("rita");
        // User osvaldo = ldod.getUser("osvaldo");
        // User jose = ldod.getUser("jose");
        //
        // VirtualEdition classX = new VirtualEdition(ldod, ars, "VirtualModule-ClassX", "VirtualModule
        // Edition of Class X", new LocalDate(),
        // false, null);
        // classX.addMember(luis, MemberRole.ADMIN, true);
        // classX.addMember(mp, MemberRole.ADMIN, true);
        // classX.addMember(diego, MemberRole.ADMIN, true);
        // classX.addMember(tiago, MemberRole.ADMIN, true);
        // classX.addMember(ars, MemberRole.ADMIN, true);
        // classX.addMember(andre, MemberRole.ADMIN, true);
        // classX.addMember(daniela, MemberRole.ADMIN, true);
        // classX.addMember(joana, MemberRole.ADMIN, true);
        // classX.addMember(bernardosoares, MemberRole.ADMIN, true);
        // classX.addMember(rita, MemberRole.ADMIN, true);
        // classX.addMember(osvaldo, MemberRole.ADMIN, true);
        // classX.addMember(jose, MemberRole.ADMIN, true);
        // luis.addSelectedVirtualEditions(classX);
        // mp.addSelectedVirtualEditions(classX);
        // ars.addSelectedVirtualEditions(classX);
        // diego.addSelectedVirtualEditions(classX);
        // tiago.addSelectedVirtualEditions(classX);
        // nuno.addSelectedVirtualEditions(classX);
        // andre.addSelectedVirtualEditions(classX);
        // bernardosoares.addSelectedVirtualEditions(classX);
        // rita.addSelectedVirtualEditions(classX);
        // osvaldo.addSelectedVirtualEditions(classX);
        // jose.addSelectedVirtualEditions(classX);
        //
        // VirtualEdition classY = new VirtualEdition(ldod, ars, "VirtualModule-ClassY", "VirtualModule
        // Edition of Class Y", new LocalDate(),
        // false, null);
        // classY.addMember(luis, MemberRole.ADMIN, true);
        // classY.addMember(mp, MemberRole.ADMIN, true);
        // classY.addMember(diego, MemberRole.ADMIN, true);
        // classY.addMember(tiago, MemberRole.ADMIN, true);
        // classY.addMember(ars, MemberRole.ADMIN, true);
        // luis.addSelectedVirtualEditions(classY);
        // mp.addSelectedVirtualEditions(classY);
        // ars.addSelectedVirtualEditions(classY);
        // diego.addSelectedVirtualEditions(classY);
        // tiago.addSelectedVirtualEditions(classY);
        // nuno.addSelectedVirtualEditions(classY);
        //
        // VirtualEdition classW = new VirtualEdition(ldod, ars, "VirtualModule-ClassW", "VirtualModule
        // Edition of Class W", new LocalDate(),
        // false, null);
        // classW.addMember(diego, MemberRole.ADMIN, true);
        // classW.addMember(mp, MemberRole.ADMIN, true);
        // classW.addMember(luis, MemberRole.ADMIN, true);
        // classW.addMember(andre, MemberRole.ADMIN, true);
        // classW.addMember(tiago, MemberRole.ADMIN, true);
        // classW.addMember(nuno, MemberRole.ADMIN, true);
        // mp.addSelectedVirtualEditions(classW);
        // ars.addSelectedVirtualEditions(classW);
        // diego.addSelectedVirtualEditions(classW);
        // tiago.addSelectedVirtualEditions(classW);
        // nuno.addSelectedVirtualEditions(classW);
    }

    private static void createLdoDArchiveVirtualEdition() {
        VirtualEdition ldoDArchiveEdition = new VirtualEdition(VirtualModule.getInstance(), ARS, VirtualEdition.ARCHIVE_EDITION_ACRONYM,
                VirtualEdition.ARCHIVE_EDITION_NAME, new LocalDate(), true, null);

        ldoDArchiveEdition.addMember(ARS, Member.MemberRole.ADMIN, true);
    }
}
