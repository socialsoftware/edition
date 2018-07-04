package pt.ist.socialsoftware.edition.ldod.social.aware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import pt.ist.fenixframework.FenixFramework;
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
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;

public class AwareAnnotationFactory {
	private static Logger logger = LoggerFactory.getLogger(AwareAnnotationFactory.class);

	public static void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	// main method of this Factory
	@Atomic(mode = TxMode.WRITE)
	public void generate() throws IOException {
		logger.debug("BEGINNIG OF AWARE FACTORY");

		BufferedWriter bw = null;
		FileWriter fw = null;
		File file;
		file = new File("C:/Users/dnf_o/projetoTese/ldod/social/annot/annots.txt");
		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);

		// SETUP DOS CRITÉRIOS
		VirtualEdition duarteEdit = LdoD.getInstance().getVirtualEdition("LdoD-Duarte");
		new MediaSource(duarteEdit, "Twitter");
		// new TimeWindow(duarteEdit, new LocalDate("2018-03-06"), new
		// LocalDate("2018-06-24"));
		// new GeographicLocation(duarteEdit, "Portugal", "Lisboa");
		// new Frequency(duarteEdit, 10);

		for (VirtualEdition ve : LdoD.getInstance().getVirtualEditionsSet()) {
			logger("VirtualEdition XMLID: " + ve.getXmlId());
			if (ve.isSAVE()) {
				searchForAwareAnnotations(ve, bw);
			}
			logger("+++++++++++++++++++++ NEXT VIRTUAL EDITION +++++++++++++++++++++++");
		}
		bw.close();
		fw.close();

		// testing code
		// populateWithAwareAnnotation(bw); bw.close(); fw.close();

		// pattern finding code was here
		// caso seja preciso fazer debug ao algoritmo, é só ir buscar ao backup e testar
		// aqui
		// com o ciclo for q percorre todas as twitter citations do sistema
		logger.debug("END OF AWARE FACTORY");
	}

	// método invocado também quando se cria uma nova SAVE
	public void searchForAwareAnnotations(VirtualEdition ve, BufferedWriter bw) throws IOException {
		logger(" ++++++++++++++++++++++++++ SAVE +++++++++++++++++++++++++++ ");
		logger(ve.getXmlId() + " is SAVE");
		logger("All Depth - Inters size: " + ve.getAllDepthVirtualEditionInters().size());
		logger("            Inters size: " + ve.getIntersSet().size());

		// allTwitterCitations - all twitter citations in the archive - debugging
		Set<TwitterCitation> allTwitterCitations = LdoD.getInstance().getAllTwitterCitation();
		bw.write("All Twitter Citations size: " + allTwitterCitations.size());
		bw.write("\n");

		Set<SocialMediaCriteria> criteria = ve.getCriteriaSet();

		// debug method for annotation details
		// annotsDetails(ve, bw);

		// perguntar ao stor se é este o método correto
		// de obter os Inters a partire uma VE
		// ou se basta o getIntersSet
		int count = 0;
		for (VirtualEditionInter inter : ve.getAllDepthVirtualEditionInters()) {
			logger(" +++++++++++++++++ VE Inter +++++++++++++++++++");
			logger("Inter external ID: " + inter.getExternalId());
			logger("Inter title: " + inter.getTitle());

			bw.write(" +++++++++++++++++++++ VE Inter +++++++++++++++++++++++");
			bw.write("\n");
			bw.write("Inter external ID: " + inter.getExternalId());
			bw.write("\n");
			bw.write("Inter title: " + inter.getTitle());
			bw.write("\n");

			if (!validateFrequency(criteria, inter)) {
				continue;
			}

			// currentTwitterCitations - current twitter citations from a certain
			// fragInter
			// totalTwitterCitations - total twitter citations from a certain fragInter
			// ^baseados nas anotações que já foram criadas para cada fraginter
			// pq para cada fraginter vai-se ver as anotações que foram criadas e
			// daí é q se extraem estes dois Sets

			// Set<TwitterCitation> currentTwitterCitations =
			// getCurrentTwitterCitationsByInter(inter);
			// logger("CurrentTwitterCitations set size: " +
			// currentTwitterCitations.size());
			// bw.write("CurrentTwitterCitations set size: " +
			// currentTwitterCitations.size());
			// bw.write("\n");

			Set<TwitterCitation> totalTwitterCitations = getTotalTwitterCitationsByInterAndCriteria(inter, criteria,
					bw);
			logger("TotalTwitterCitations set size: " + totalTwitterCitations.size());
			bw.write("TotalTwitterCitations set size: " + totalTwitterCitations.size());
			bw.write("\n");
			bw.write("\n");

			// debug
			// String htmlTransc = getHtmlTransc(inter);
			// bw.write("htmlTransc: " + htmlTransc);
			// bw.write("\n");
			// bw.write("\n");

			// *********************** REMOVAL ****************************

			// bw.write("TotalTwitterCitations set size BEFORE REMOVAL: " +
			// totalTwitterCitations.size());
			// bw.write("\n");
			// totalTwitterCitations.removeAll(currentTwitterCitations);
			// bw.write("TotalTwitterCitations set size AFTER REMOVAL: " +
			// totalTwitterCitations.size());
			// bw.write("\n");
			// bw.write("\n");
			//
			// // debug do total já removido
			// bw.write("Content of new citations to create for this frag inter:");
			// bw.write("\n");
			// for (TwitterCitation tc : totalTwitterCitations) {
			// bw.write(" Tweet ID: " + tc.getTweetID() + " " + tc.getDate());
			// bw.write("\n");
			// }
			// bw.write("----------------------");
			// bw.write("\n");
			// bw.write("\n");

			removeAllAwareAnnotationsFromVEInter(inter);

			for (TwitterCitation newCitation : totalTwitterCitations) {
				bw.write(" ----------------- Twitter Citation --------");
				bw.write("\n");
				bw.write("Count = " + count);
				bw.write("\n");
				bw.write("\n");

				bw.write("Date: " + newCitation.getDate());
				bw.write("\n");
				bw.write("Tweet ID: " + newCitation.getTweetID());
				bw.write("\n");
				bw.write("Tweet Text: " + newCitation.getTweetText());
				bw.write("\n");
				bw.write("Frag external ID: " + newCitation.getFragment().getExternalId());
				bw.write("\n");
				bw.write("Frag Title: " + newCitation.getFragment().getTitle());
				bw.write("\n");
				bw.write("Frag Text: " + newCitation.getFragText());
				bw.write("\n");
				bw.write("\n");

				bw.write("INFO RANGE SET IS NOT EMPTY!!\n");
				createAwareAnnotation(inter, newCitation, bw);
				count++;
			}
		}
	}

	// método responsável por criar aware annotation no vei com meta informação
	// contida na tc
	public void createAwareAnnotation(VirtualEditionInter vei, TwitterCitation tc, BufferedWriter bw)
			throws IOException {
		bw.write("Entrei no create aware annotation");
		bw.write("\n");

		// ********************************** CREATE ANNOTATION AND RANGE
		// ********************************

		InfoRange infoRange = getInfoRangeByVirtualEditionInter(vei, tc, bw);

		bw.write("GOING TO CREATE AN AWARE ANNOTATION BASED ON AN INFO RANGE!!!");
		bw.write("\n");

		String htmlTransc = getHtmlTransc(vei);
		bw.write("htmlTransc: " + htmlTransc);
		bw.write("\n");
		bw.write("\n");

		bw.write("------------Tweet Text: " + tc.getTweetText());
		bw.write("\n");

		bw.write("------------Info Range quote: " + infoRange.getQuote());
		bw.write("\n");
		bw.write("\n");

		bw.write(" INFO RANGE - START: " + infoRange.getStart());
		bw.write("\n");
		bw.write(" INFO RANGE - END: " + infoRange.getEnd());
		bw.write("\n");

		bw.write(" INFO RANGE - Índice do htmlStart: " + infoRange.getStartOffset());
		bw.write("\n");
		bw.write(" INFO RANGE - Índice do htmlEnd: " + infoRange.getEndOffset());
		bw.write("\n");
		bw.write("\n");

		bw.write(" VE inter id: " + vei.getExternalId() + "\n");
		bw.write(" XML id: " + vei.getXmlId() + "\n");
		bw.write(" Title: " + vei.getTitle() + "\n");
		bw.write("\n");

		AwareAnnotation annotation = new AwareAnnotation(vei, infoRange.getQuote(), infoRange.getText(), tc);

		new Range(annotation, infoRange.getStart(), infoRange.getStartOffset(), infoRange.getEnd(),
				infoRange.getEndOffset());
	}

	private String getHtmlTransc(FragInter inter) {
		PlainHtmlWriter4OneInter htmlWriter = new PlainHtmlWriter4OneInter(inter);
		htmlWriter.write(false);
		String htmlTransc = htmlWriter.getTranscription();
		return htmlTransc;
	}

	// TODO: apagar este método e chamar o da classe Citation
	// a citation has several info ranges
	// this method finds out the appropriate info range for the specific vei
	private InfoRange getInfoRangeByVirtualEditionInter(VirtualEditionInter vei, TwitterCitation tc, BufferedWriter bw)
			throws IOException {
		// bw.write("ENTREI NO GET INFO RANGE BY VE INTER:");
		// bw.write("\n");

		// richard zenith's fraginter
		FragInter lastUsed = vei.getLastUsed();

		// bw.write(" LAST USED - FRAG INTER ID: " + lastUsed.getExternalId());
		// bw.write("\n");
		InfoRange infoRange = null;
		for (InfoRange ir : tc.getInfoRangeSet()) {
			// bw.write(" INFO RANGE - FRAG INTER ID: " +
			// ir.getFragInter().getExternalId());
			// bw.write("\n");
			if (lastUsed == ir.getFragInter()) {
				infoRange = ir;
				break;
			}
		}
		return infoRange;
	}

	// TODO: removeAllAwareAnnotationsFromVEInter
	private void removeAllAwareAnnotationsFromVEInter(VirtualEditionInter inter) {
		Set<AwareAnnotation> awareAnnotations = inter.getAnnotationSet().stream()
				.filter(AwareAnnotation.class::isInstance).map(AwareAnnotation.class::cast).collect(Collectors.toSet());
		for (AwareAnnotation aa : awareAnnotations) {
			aa.remove();
		}
	}

	// TODO: upgrade to support criteria
	private Set<TwitterCitation> getTotalTwitterCitationsByInterAndCriteria(VirtualEditionInter inter,
			Set<SocialMediaCriteria> criteria, BufferedWriter bw) throws IOException {
		Set<TwitterCitation> totalTwitterCitations = new HashSet<TwitterCitation>();
		for (Citation tc : inter.getFragment().getCitationSet()) {
			if (tc instanceof TwitterCitation /* && !tc.getInfoRangeSet().isEmpty() */
					&& getInfoRangeByVirtualEditionInter(inter, (TwitterCitation) tc, null) != null
							& validateCriteria(tc, criteria, bw)) {
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

	private boolean validateCriteria(Citation tc, Set<SocialMediaCriteria> criteria, BufferedWriter bw)
			throws IOException {
		boolean isValid = true;
		for (SocialMediaCriteria criterion : criteria) {
			if (criterion instanceof MediaSource) {
				bw.write("		ENTREI NO MEDIA SOURCE \n");
				if (tc instanceof TwitterCitation && !((MediaSource) criterion).getName().equals("Twitter")) {
					bw.write("			ENTREI NO IF DO MEDIA SOURCE \n");
					bw.write("			MEDIA SOURCE NAME: " + ((MediaSource) criterion).getName() + "\n");
					isValid = false;
				}
			} else if (criterion instanceof TimeWindow) {
				bw.write("		ENTREI NO TIME WINDOW \n");
				DateTimeFormatter formatter = DateTimeFormat.forPattern("d-MMM-yyyy");

				// estilo: "16-Aug-2016"
				String date = tc.getDate().split(" ")[0];
				bw.write("			Original date: " + date + "\n");

				// converter para estilo universal localdate: "2016-08-16"
				LocalDate localDate = LocalDate.parse(date, formatter);
				bw.write("			Universal localdate: " + localDate + "\n");

				LocalDate beginDate = ((TimeWindow) criterion).getBeginDate();
				LocalDate endDate = ((TimeWindow) criterion).getEndDate();

				if (!(((localDate.isAfter(beginDate) || localDate.isEqual(beginDate)))
						&& (localDate.isBefore(endDate) || localDate.isEqual(endDate)))) {
					bw.write("			ENTREI NO IF DO TIME WINDOW \n");
					isValid = false;
				}
			} else if (criterion instanceof GeographicLocation) {
				bw.write("		ENTREI NO GEOGRAPHIC LOCATION \n");
				if (tc instanceof TwitterCitation && !(((TwitterCitation) tc).getCountry()
						.equals(((GeographicLocation) criterion).getCountry()))) {
					bw.write("			ENTREI NO IF DO GEOGRAPHIC LOCATION \n");
					isValid = false;
				}

			} else if (criterion instanceof Frequency) {
				bw.write("		ENTREI NO FREQUENCY \n");
				// do nothing ...
			}
		}

		// just for debug
		if (tc instanceof TwitterCitation) {
			bw.write("		ENTREI NO TIME WINDOW FORA DO FOR \n");
			DateTimeFormatter formatter = DateTimeFormat.forPattern("d-MMM-yyyy");

			// estilo: "16-Aug-2016"
			String date = tc.getDate().split(" ")[0];
			bw.write("			Original date: " + date + "\n");

			// converter para estilo universal localdate: "2016-08-16"
			LocalDate localDate = LocalDate.parse(date, formatter);
			bw.write("			Universal localdate: " + localDate + "\n");

			bw.write("		ENTREI NO GEOGRAPHIC LOCATION FORA DO FOR \n");
			bw.write("			Country: " + ((TwitterCitation) tc).getCountry() + "\n");
			bw.write("			Location: " + ((TwitterCitation) tc).getLocation() + "\n");

		}

		return isValid;
	}

	// TODO: apagar, porque provavelmente não será necessário
	private Set<TwitterCitation> getCurrentTwitterCitationsByInter(VirtualEditionInter inter) {
		Set<TwitterCitation> twitterCitations = new HashSet<TwitterCitation>();
		Set<AwareAnnotation> awareAnnotations = inter.getAnnotationSet().stream()
				.filter(AwareAnnotation.class::isInstance).map(AwareAnnotation.class::cast).collect(Collectors.toSet());

		for (AwareAnnotation aa : awareAnnotations) {
			twitterCitations.add((TwitterCitation) aa.getCitation());
		}
		return twitterCitations;
	}

	// método de teste simples utilizado para criar uma aware annotation
	private void populateWithAwareAnnotation(BufferedWriter bw) throws IOException {
		// testing code
		// ********************** POPULATE DB WITH AWARE ANNOTATION
		// ********************************************
		VirtualEdition duarteEdit = LdoD.getInstance().getVirtualEdition("LdoD-EditDuarte");
		// duarteEdit.addCriteria(new TimeWindow());
		logger("IS SAVE: " + duarteEdit.isSAVE());
		logger("Size of criteria set: " + duarteEdit.getCriteriaSet().size());

		Set<FragInter> inters = duarteEdit.getIntersSet();
		logger("Size of inters: " + inters.size());
		// 844639678496964 - virtual inter external id
		// LENDA IMPERIAL - virtual inter title
		VirtualEditionInter vei = FenixFramework.getDomainObject("844639678496875");
		// 844639678496818
		// Prefiro a prosa ao verso
		// 844639678496958
		// MÁXIMAS
		// 844639678496875
		// Há uma erudição do conhecimento

		logger(vei.getExternalId());
		logger(vei.getTitle());

		logger(vei);
		logger(vei.getLastUsed());

		// setup example
		long tweetID = 997186535761039360L;
		TwitterCitation twitterCitation = LdoD.getInstance().getTwitterCitationByTweetID(tweetID);
		logger("Date: " + twitterCitation.getDate());
		logger("Fragment text: " + twitterCitation.getFragText());
		createAwareAnnotation(vei, twitterCitation, bw); // descomentar
	}

	// debug method - writes on annotsDetails.txt details about annotations
	private void annotsDetails(VirtualEdition ve, BufferedWriter bw) throws IOException {
		// int count = 0;
		// for (VirtualEditionInter inter : ve.getAllDepthVirtualEditionInters()) {
		// bw.write("Inter title: " + inter.getTitle());
		// bw.write("\n");
		//
		// Set<TwitterCitation> totalTwitterCitations =
		// getTotalTwitterCitationsByInter(inter, null);
		// bw.write(" TotalTwitterCitations set size: " + totalTwitterCitations.size());
		// bw.write("\n");
		//
		// for (TwitterCitation tc : totalTwitterCitations) {
		// bw.write(" Count = " + count);
		// bw.write("\n");
		// bw.write(" Tweet ID: " + tc.getTweetID() + " " + tc.getDate());
		// bw.write("\n");
		// count++;
		// }
		// bw.write("--------------------------------------");
		// bw.write("\n");
		// bw.write("\n");
		// }
	}

	/**************************************
	 * DEBUG DE IDS
	 *************************************************/
	/*
	 * BufferedWriter bw = null; FileWriter fw = null; File file; file = new File(
	 * "C:/Users/dnf_o/projetoTese/ldod/social/citationsPositions/positions.txt");
	 * fw = new FileWriter(file); bw = new BufferedWriter(fw);
	 * 
	 * Set<TwitterCitation> allTwitterCitations =
	 * LdoD.getInstance().getCitationsSet()
	 * .stream().filter(TwitterCitation.class::isInstance).map(TwitterCitation.class
	 * ::cast) .collect(Collectors.toSet());
	 * 
	 * for (TwitterCitation c : allTwitterCitations) {
	 * System.out.println("+++++++++++ CITATIONS ++++++++++++++++");
	 * System.out.println(c.getSourceLink()); System.out.println(c.getDate());
	 * System.out.println(c.getFragText()); System.out.println(c.getExternalId());
	 * 
	 * bw.write("***************** CITATIONS **************************");
	 * bw.write("\n"); bw.write("Date: " + c.getDate()); bw.write("\n");
	 * bw.write("External ID: " + c.getExternalId()); bw.write("\n");
	 * bw.write("Tweet ID: " + c.getTweetID()); bw.write("\n"); }
	 * 
	 * 
	 * for (Fragment frag : LdoD.getInstance().getFragmentsSet()) {
	 * System.out.println("++++++++++++ FRAGMENTS +++++++++++++++");
	 * System.out.println("Title: " + frag.getTitle());
	 * System.out.println("External ID: " + frag.getExternalId());
	 * 
	 * bw.write("***************** FRAGMENTS **************************");
	 * bw.write("\n"); bw.write("Title: " + frag.getTitle()); bw.write("\n");
	 * bw.write("External ID: " + frag.getExternalId()); bw.write("\n");
	 * bw.write("XML ID: " + frag.getXmlId()); bw.write("\n"); }
	 * 
	 * bw.close(); fw.close();
	 */
}
