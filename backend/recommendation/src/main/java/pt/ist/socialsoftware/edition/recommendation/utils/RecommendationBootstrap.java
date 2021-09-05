package pt.ist.socialsoftware.edition.recommendation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecommendationBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationBootstrap.class);
    public static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";



    public static void initializeRecommendationModule() {
        boolean recommendationCreate = false;
        if (RecommendationModule.getInstance() == null) {
            new RecommendationModule();
            recommendationCreate = true;
        } else {
            loadRecommendationCache();
        }
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void loadRecommendationCache() {
        //RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();
        Set<FragmentDto> fragments = RecommendationRequiresInterface.getInstance().getFragments();

        if (fragments.size() > 800) {
            List<Property> properties = new ArrayList<>();
            properties.add(new TextProperty(1.0));
            properties.add(new HeteronymProperty(1.0));
            properties.add(new DateProperty(1.0));
            properties.add(new TaxonomyProperty(1.0, ARCHIVE_EDITION_ACRONYM,
                    Property.PropertyCache.ON));

            VSMFragmentRecommender recommender = new VSMFragmentRecommender();
            for (FragmentDto fragment : fragments) {
                logger.debug("loadRecommendationCache xmlId:{}", fragment.getXmlId());
                recommender.getMostSimilarItem(fragment, fragments, properties);
            }
        }

        RecommendationRequiresInterface.getInstance().cleanTermsTFIDFCache();
    }

}
