package pt.ist.socialsoftware.edition.ldod.controller;

import org.apache.catalina.User;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.user.domain.UserManager;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.io.*;
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class ControllerTest {

    protected static int NUMBER_OF_FRAGS = 15;

    protected MockMvc mvc;

    static private boolean configured = false;



    @BeforeAll
    public static void SetUp() throws SystemException, NotSupportedException {
        if (!configured) {
            FenixFramework.getTransactionManager().begin(false);

            Bootstrap.initializeSystem();

            loadTEICorpus();
            loadFragments();
            createVirtualEditionsForTest();

            configured =true;
        }
        else {
            return;
        }
    }

    private static void loadTEICorpus() {
        File frag = new File("frags/001.xml");
        LoadTEICorpus loader = new LoadTEICorpus();
        try {
            InputStream inputStream = new FileInputStream(frag);
            loader.loadTEICorpus(inputStream);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private static void loadFragments(){
        ArrayList<InputStream> files = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_FRAGS; i++) {
            try {
                files.add(new FileInputStream(new File("frags/" + String.format("%03d", i) + ".xml")));
            } catch (FileNotFoundException e) {
                System.err.print(e.getMessage());
            }
        }
        LoadTEIFragments loader = new LoadTEIFragments();
        for (InputStream file: files){

            loader.loadFragmentsStepByStep(file);
        }
    }


    private static void createVirtualEditionsForTest() {
        VirtualManager virtualManager = VirtualManager.getInstance();
        UserManager userManager = UserManager.getInstance();

//        LdoDUser user = userManager.getUser("user");
        LdoDUser ars = (LdoDUser) userManager.getUser("ars");

        VirtualEdition virtualEdition = new VirtualEdition(virtualManager,
                ars,
                "VE1",
                "Virtual Edition 1",
                new LocalDate(),
                false,
                null);

        virtualEdition.setPub(true);
        ars.addSelectedVirtualEditions(virtualEdition);
    }

    protected static RequestPostProcessor admin() {
        return user("admin").roles("ADMIN").password("admin");
    }


}
