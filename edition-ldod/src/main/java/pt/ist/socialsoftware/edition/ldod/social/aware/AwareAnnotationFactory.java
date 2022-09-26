package pt.ist.socialsoftware.edition.ldod.social.aware;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AwareAnnotationFactory {
    private static final Logger logger = LoggerFactory.getLogger(AwareAnnotationFactory.class);

    // main method of this Factory
    @Atomic(mode = TxMode.WRITE)
    public void generate() throws IOException {
        logger.debug("BEGINNIG OF AWARE FACTORY");

        for (VirtualEdition ve : LdoD.getInstance().getVirtualEditionsSet()) {
            if (ve.isSAVE()) {
                searchForAwareAnnotations(ve);
            }
        }

        logger.debug("END OF AWARE FACTORY");
    }

    @Atomic(mode = TxMode.WRITE)
    public void searchForAwareAnnotations(VirtualEdition ve) {
        logger.debug("STARTED AWARE FACTORY");

        Set<SocialMediaCriteria> criteria = ve.getCriteriaSet();

        for (VirtualEditionInter inter : ve.getAllDepthVirtualEditionInters()) {

            if (validateFrequency(criteria, inter)) {
                Set<TwitterCitation> totalTwitterCitations = getTotalTwitterCitationsByInterAndCriteria(inter, criteria);

                // *********************** REMOVAL ****************************

                removeAllAwareAnnotationsFromVEInter(inter);

                for (TwitterCitation newCitation : totalTwitterCitations) {

                    createAwareAnnotation(inter, newCitation);
                }
            }
        }

        logger.debug("ENDED AWARE FACTORY");
    }

    // método responsável por criar aware annotation no vei com meta informação
    // contida na tc
    private void createAwareAnnotation(VirtualEditionInter vei, TwitterCitation tc) {
        InfoRange infoRange = getInfoRangeByVirtualEditionInter(vei, tc);

//        if (vei.getVirtualEdition().getAcronym().equals(LdoD.TWITTER_EDITION_ACRONYM)) {
//            logger.debug("createAwareAnnotation title: {}, start: {}, end: {}, startOffset: {}, endOffset: {}, quote: {}, text: {}",
//                    vei.getTitle(), infoRange.getStart(), infoRange.getEnd(), infoRange.getStartOffset(), infoRange.getEndOffset(), infoRange.getQuote(), infoRange.getText());
//        }

        AwareAnnotation annotation = new AwareAnnotation(vei, infoRange.getQuote(), infoRange.getText(), tc);

        new Range(annotation, infoRange.getStart(), infoRange.getStartOffset(), infoRange.getEnd(),
                infoRange.getEndOffset());
    }

    // TODO: utilizar o método da classe Citation em vez deste
    // a citation has several info ranges.
    // this method finds out the appropriate info range for the specific vei
    private InfoRange getInfoRangeByVirtualEditionInter(VirtualEditionInter vei, TwitterCitation tc) {

        // used fraginter
        FragInter lastUsed = vei.getLastUsed();

        InfoRange infoRange = null;
        for (InfoRange ir : tc.getInfoRangeSet()) {

            if (lastUsed == ir.getFragInter()) {
                infoRange = ir;
                break;
            }
        }
        return infoRange;
    }

    public void removeAllAwareAnnotationsFromVEInter(VirtualEditionInter inter) {
        Set<AwareAnnotation> awareAnnotations = inter.getAnnotationSet().stream()
                .filter(AwareAnnotation.class::isInstance).map(AwareAnnotation.class::cast).collect(Collectors.toSet());
        for (AwareAnnotation aa : awareAnnotations) {
            aa.remove();
        }
    }

    // upgraded to support criteria
    private Set<TwitterCitation> getTotalTwitterCitationsByInterAndCriteria(VirtualEditionInter inter,
                                                                            Set<SocialMediaCriteria> criteria) {
        Set<TwitterCitation> totalTwitterCitations = new HashSet<>();
        for (Citation tc : inter.getFragment().getCitationSet()) {
            if (tc instanceof TwitterCitation && getInfoRangeByVirtualEditionInter(inter, (TwitterCitation) tc) != null
                    & validateCriteria(tc, criteria)) {
                totalTwitterCitations.add((TwitterCitation) tc);
            }
        }
        return totalTwitterCitations;
    }

    private boolean validateFrequency(Set<SocialMediaCriteria> criteria, VirtualEditionInter inter) {
        boolean isValid = true;
        for (SocialMediaCriteria criterion : criteria) {
            if (criterion instanceof Frequency) {
                if (((Frequency) criterion).getFrequency() > inter.getNumberOfTimesCitedIncludingRetweets()) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private boolean validateCriteria(Citation tc, Set<SocialMediaCriteria> criteria) {
        boolean isValid = true;

        for (SocialMediaCriteria criterion : criteria) {
            if (criterion instanceof MediaSource) {
                if (tc instanceof TwitterCitation && !((MediaSource) criterion).getName().equals("Twitter")) {
                    isValid = false;
                }
            } else if (criterion instanceof TimeWindow) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("d-MMM-yyyy");

                // estilo: "16-Aug-2016"
                //String date = tc.getDate().split(" ")[0];
                LocalDate localDate = org.joda.time.LocalDate.parse(tc.getFormatedDate().toLocalDate().toString());
                // converter para estilo universal localdate: "2016-08-16"
                //LocalDate localDate = LocalDate.parse(date, formatter);

                LocalDate beginDate = ((TimeWindow) criterion).getBeginDate();
                LocalDate endDate = ((TimeWindow) criterion).getEndDate();

                // new code that supports null format for date input by user
                if (beginDate != null) {
                    if (!(localDate.isAfter(beginDate) || localDate.isEqual(beginDate))) {
                        isValid = false;
                    }
                }
                if (endDate != null) {
                    if (!(localDate.isBefore(endDate) || localDate.isEqual(endDate))) {
                        isValid = false;
                    }
                }

            } else if (criterion instanceof GeographicLocation) {
                if (tc instanceof TwitterCitation && !((GeographicLocation) criterion).containsEveryCountry()
                        && !((GeographicLocation) criterion).containsCountry(((TwitterCitation) tc).getCountry())) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

}
