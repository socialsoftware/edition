package pt.ist.socialsoftware.edition.ldod;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;


import java.io.FileNotFoundException;

public abstract class TestWithFragmentsLoading {

    @BeforeAll
    @Atomic(mode = TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();
    }

    @AfterAll
    @Atomic(mode = TxMode.WRITE)
    public static void tearDownAll() {
        TestLoadUtils.cleanDatabase();
    }

    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    public void setUp() throws FileNotFoundException {

        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
        FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

        feTextRequiresInterface.cleanFragmentMapCache();
        feTextRequiresInterface.cleanScholarInterMapCache();
        feVirtualRequiresInterface.cleanVirtualEditionInterMapByUrlIdCache();
        feVirtualRequiresInterface.cleanVirtualEditionInterMapByXmlIdCache();
        feVirtualRequiresInterface.cleanVirtualEditionMapCache();


        TestLoadUtils.loadFragments(fragmentsToLoad4Test());

        populate4Test();
    }

    @AfterEach
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {
        unpopulate4Test();

        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
        feTextRequiresInterface.getFragmentDtoSet().forEach(fragmentDto ->  feTextRequiresInterface.removeFragmentByExternalId(fragmentDto.getExternalId()));
//        TextModule.getInstance().getFragmentsSet().forEach(f -> f.remove());
    }

    protected abstract void populate4Test();

    protected abstract void unpopulate4Test();

    protected abstract String[] fragmentsToLoad4Test();

}
