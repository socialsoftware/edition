package pt.ist.socialsoftware.edition.ldod.recommendation;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.ReadingRecommendation;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.VSMRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.*;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.Property.PropertyCache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ReadingRecommendationPerformanceTest {

    @BeforeAll
    @Atomic(mode = TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);
    }

    @AfterAll
    @Atomic(mode = TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    // Assuming that the 4 expert editions, the archive edition and user ars are
    // in the database @BeforeAll
    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    protected void setUp() {

        VirtualModule virtualModule = VirtualModule.getInstance();

        VirtualEdition archiveEdition = virtualModule.getArchiveEdition();
        List<VirtualEditionInter> archiveVirtualEditionInters = archiveEdition.getIntersSet().stream()
                .map(VirtualEditionInter.class::cast).collect(Collectors.toList());

        VSMRecommender<VirtualEditionInter> recommender = new VSMVirtualEditionInterRecommender();

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));
        properties.add(new DateProperty(1.0));
        properties.add(new TaxonomyProperty(1.0, archiveEdition.getTaxonomy(), PropertyCache.ON));
        properties.add(new TextProperty(1.0));

        // warm the system in order to create all the caches
        recommender.getMostSimilarItemsAsList(archiveVirtualEditionInters.get(2),
                new HashSet<>(archiveVirtualEditionInters), properties);

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testSeveralRecommendations() throws IOException, ParseException {
        ReadingRecommendation recommender = new ReadingRecommendation();

        recommender.setDateWeight(1.0);
        recommender.setHeteronymWeight(1.0);
        recommender.setTextWeight(1.0);
        recommender.setTaxonomyWeight(1.0);

        ExpertEdition pizarroEdition = TextModule.getInstance().getJPEdition();
        ExpertEditionInter inter = pizarroEdition.getExpertEditionIntersSet().stream().collect(Collectors.toList())
                .get(0);

        for (int i = 0; i < 100; i++) {
            Set<ExpertEditionInter> nextInters = recommender.getNextRecommendations(inter.getExternalId());
            assertFalse(nextInters.isEmpty());
        }

    }

}
