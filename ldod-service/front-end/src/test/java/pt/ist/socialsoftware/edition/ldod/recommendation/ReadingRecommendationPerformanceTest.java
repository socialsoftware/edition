package pt.ist.socialsoftware.edition.ldod.recommendation;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;

import pt.ist.socialsoftware.edition.ldod.frontend.reading.ReadingRecommendation;

import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.api.textdto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.textdto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

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
        TestLoadUtils.cleanDatabase();
    }

    // Assuming that the 4 expert editions, the archive edition and user ars are
    // in the database @BeforeAll
    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    protected void setUp() {

        VirtualModule virtualModule = VirtualModule.getInstance();

        VirtualEditionDto archiveEdition = new VirtualEditionDto(virtualModule.getArchiveEdition());
        List<VirtualEditionInterDto> archiveVirtualEditionInters = archiveEdition.getSortedVirtualEditionInterDtoList();

        VSMRecommender<VirtualEditionInterDto> recommender = new VSMVirtualEditionInterRecommender();

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1));
        properties.add(new DateProperty(1));
        properties.add(new TaxonomyProperty(1, VirtualEdition.ARCHIVE_EDITION_ACRONYM, Property.PropertyCache.ON));
        properties.add(new TextProperty(1));

        // warm the system in order to create all the caches
        recommender.getMostSimilarItemsAsList(archiveVirtualEditionInters.get(2),
                new HashSet<>(archiveVirtualEditionInters), properties);

    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testSeveralRecommendations() throws IOException, ParseException {
        ReadingRecommendation recommender = new ReadingRecommendation();

        recommender.setDateWeight(1);
        recommender.setHeteronymWeight(1);
        recommender.setTextWeight(1);
        recommender.setTaxonomyWeight(1);

        ExpertEditionDto pizarroEdition = (new FeTextRequiresInterface().getExpertEditionByAcronym("JP"));
        ScholarInterDto inter = pizarroEdition.getExpertEditionInters().stream().collect(Collectors.toList())
                .get(0);

        for (int i = 0; i < 100; i++) {
            Set<ScholarInterDto> nextInters = recommender.getNextRecommendations(inter.getExternalId());
            assertFalse(nextInters.isEmpty());
        }

    }

}
