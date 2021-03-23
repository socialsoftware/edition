package pt.ist.socialsoftware.edition.ldod;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.text.domain.TextModule;

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
        TestLoadUtils.loadFragments(fragmentsToLoad4Test());

        populate4Test();
    }

    @AfterEach
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {
        unpopulate4Test();

        TextModule.getInstance().getFragmentsSet().forEach(f -> f.remove());
    }

    protected abstract void populate4Test();

    protected abstract void unpopulate4Test();

    protected abstract String[] fragmentsToLoad4Test();

}
