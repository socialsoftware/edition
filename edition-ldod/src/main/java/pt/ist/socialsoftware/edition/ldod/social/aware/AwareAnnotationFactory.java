package pt.ist.socialsoftware.edition.ldod.social.aware;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Frequency;
import pt.ist.socialsoftware.edition.ldod.domain.GeographicLocation;
import pt.ist.socialsoftware.edition.ldod.domain.InfoRange;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.MediaSource;
import pt.ist.socialsoftware.edition.ldod.domain.Range;
import pt.ist.socialsoftware.edition.ldod.domain.SocialMediaCriteria;
import pt.ist.socialsoftware.edition.ldod.domain.TimeWindow;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class AwareAnnotationFactory {
	private static Logger logger = LoggerFactory.getLogger(AwareAnnotationFactory.class);

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

	// método invocado também quando se edita uma nova SAVE
	@Atomic(mode = TxMode.WRITE)
	public void searchForAwareAnnotations(VirtualEdition ve) {
		Set<SocialMediaCriteria> criteria = ve.getCriteriaSet();

		for (VirtualEditionInter inter : ve.getAllDepthVirtualEditionInters()) {

			if (!validateFrequency(criteria, inter)) {
				continue;
			}

			Set<TwitterCitation> totalTwitterCitations = getTotalTwitterCitationsByInterAndCriteria(inter, criteria);

			// *********************** REMOVAL ****************************

			removeAllAwareAnnotationsFromVEInter(inter);

			for (TwitterCitation newCitation : totalTwitterCitations) {

				createAwareAnnotation(inter, newCitation);
			}
		}
	}

	// método responsável por criar aware annotation no vei com meta informação
	// contida na tc
	public void createAwareAnnotation(VirtualEditionInter vei, TwitterCitation tc) {

		InfoRange infoRange = getInfoRangeByVirtualEditionInter(vei, tc);

		logger.debug("GOING TO CREATE AN AWARE ANNOTATION!!");

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
		Set<TwitterCitation> totalTwitterCitations = new HashSet<TwitterCitation>();
		for (Citation tc : inter.getFragment().getCitationSet()) {
			if (tc instanceof TwitterCitation /* && !tc.getInfoRangeSet().isEmpty() */
					&& getInfoRangeByVirtualEditionInter(inter, (TwitterCitation) tc) != null
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
				String date = tc.getDate().split(" ")[0];

				// converter para estilo universal localdate: "2016-08-16"
				LocalDate localDate = LocalDate.parse(date, formatter);

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
				// old code
				// if (tc instanceof TwitterCitation
				// && !((TwitterCitation) tc).getCountry().equals(((GeographicLocation)
				// criterion).getCountry())) {
				// isValid = false;
				// }

				// TODO: new code that splits countries
				if (!(tc instanceof TwitterCitation
						&& ((GeographicLocation) criterion).containsCountry(((TwitterCitation) tc).getCountry()))) {
					isValid = false;
				}

			} else if (criterion instanceof Frequency) {
				// do nothing, already verfied ...
			}
		}

		return isValid;
	}

}
