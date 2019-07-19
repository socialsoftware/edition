package pt.ist.socialsoftware.edition.ldod.recommendation

import org.slf4j.LoggerFactory
import pt.ist.socialsoftware.edition.ldod.SpockRollbackTestAbstractClass
import pt.ist.socialsoftware.edition.ldod.domain.*
import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationProvidesInterface
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.WeightsDto
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto

import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

class GetIntersByDistanceMethodTest extends SpockRollbackTestAbstractClass {
    def logger = LoggerFactory.getLogger(GetIntersByDistanceMethodTest.class);

    def virtualEdition
    def virtualEditionInter
    def weights
    def recommendationProvidesInterface

    final AtomicInteger count = new AtomicInteger();


    @Override
    def populate4Test() {
        String[] fragments = ['001.xml', '002.xml', '003.xml', '181.xml', '593.xml']
        loadFragments(fragments)

        virtualEdition = VirtualModule.getInstance().getVirtualEdition(VirtualEdition.ARCHIVE_EDITION_ACRONYM)

        virtualEditionInter = virtualEdition.getAllDepthVirtualEditionInters().stream().findFirst().get()

        // set all null
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(null)
            getLastUsedScholarEditionInter(virtualInter).getSource().setHeteronym(NullHeteronym.getNullHeteronym())
        }

        weights = new WeightsDto()

        recommendationProvidesInterface = new RecommendationProvidesInterface()
    }

    def 'test all inters have consecutive year dates'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and: 'set different dates'
        String[] dates = ["1913-12-12", "1914-12-12", "1915-12-12", "1916-12-12", "1917-12-12"]
        and: 'set dates to source inters'
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            def date_pos = count.incrementAndGet() % dates.length
            getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(new LdoDDate(dates[date_pos], Fragment.PrecisionType.HIGH))
        }

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then: 'all different'
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 5

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test all inters have non consecutive year dates'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and: 'set different dates'
        String[] dates = ["1913-12-12", "1918-12-12", "1923-12-12", "1928-12-12", "1933-12-12"]
        and: 'set dates to source inters'
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            def date_pos = count.incrementAndGet() % dates.length
            getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(new LdoDDate(dates[date_pos], Fragment.PrecisionType.HIGH))
        }

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then: 'all different'
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 5

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test 3 inters have same year date'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and: 'set different dates'
        String[] dates = ["1913-12-12", "1918-12-12", "1933-12-12", "1933-12-12", "1933-12-12"]
        and: 'set dates to source inters'
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            def date_pos = count.incrementAndGet() % dates.length
            getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(new LdoDDate(dates[date_pos], Fragment.PrecisionType.HIGH))
        }

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then:
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 3

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test all inters have same date'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and: 'set different dates'
        and: 'set dates to source inters'
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(new LdoDDate("1913-12-12", Fragment.PrecisionType.HIGH))
        }

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then:
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 1

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test only virtualEditionInter has date'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and:
        getLastUsedScholarEditionInter(virtualEditionInter).getSource().setLdoDDate(new LdoDDate('1923-12-12', Fragment.PrecisionType.HIGH))

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then:
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 2

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test only virtualEditionInter has no date'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and: 'set different dates'
        String[] dates = ["1913-12-12", "1918-12-12", "1923-12-12", "1928-12-12", "1933-12-12"]
        and: 'set dates to source inters'
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            def date_pos = count.incrementAndGet() % dates.length
            getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(new LdoDDate(dates[date_pos], Fragment.PrecisionType.HIGH))
        }
        and: 'virtualEditionInter has no date'
        getLastUsedScholarEditionInter(virtualEditionInter).getSource().setLdoDDate(null)


        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then: 'all different'
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 2

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test two have no date'() {
        given: 'a date property'
        weights.setDateWeight(1.0)
        and: 'set different dates'
        String[] dates = ["1913-12-12", "1918-12-12", "1923-12-12", "1928-12-12", "1933-12-12"]
        and: 'set dates to source inters'
        def date_pos = 0
        for (def virtualInter : virtualEdition.getAllDepthVirtualEditionInters()) {
            if (virtualInter != virtualEditionInter && date_pos < 2)
                getLastUsedScholarEditionInter(virtualInter).getSource().setLdoDDate(new LdoDDate(dates[date_pos++], Fragment.PrecisionType.HIGH))
        }
        and: 'virtualEditionInter has no date'
        getLastUsedScholarEditionInter(virtualEditionInter).getSource().setLdoDDate(new LdoDDate("1923-12-12", Fragment.PrecisionType.HIGH))


        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then: 'all different'
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 4

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test none has date'() {
        given: 'a date property'
        weights.setDateWeight(1.0)

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then:
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 1

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test only virtualEditionInter has heteronym'() {
        given: 'a heteronym property'
        weights.setHeteronymWeight(1.0)
        and:
        getLastUsedScholarEditionInter(virtualEditionInter).getSource().setHeteronym(new Heteronym(TextModule.getInstance(), 'Bernardo Soares'))

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then:
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 2

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }

    def 'test none has heteronym'() {
        given: 'a heteronym property'
        weights.setHeteronymWeight(1.0)

        when:
        def results = recommendationProvidesInterface.getIntersByDistance(new VirtualEditionInterDto(virtualEditionInter), weights)

        then:
        results.size() == 5
        results.stream().map({ r -> r.getDistance() }).collect(Collectors.toSet()).size() == 1

        results.stream().forEach({ r -> logger.debug('{}: {}', r.getInterId(), r.getDistance()) })
    }


    // TODO: to be addressed when the recommendations become a module on their own
    protected SourceInter getLastUsedScholarEditionInter(VirtualEditionInter virtualEditionInter) {
        return TextModule.getInstance().getScholarInterByXmlId(virtualEditionInter.getLastUsed().getXmlId())
    }


}
