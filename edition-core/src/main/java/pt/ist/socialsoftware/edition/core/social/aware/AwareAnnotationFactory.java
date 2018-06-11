package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.core.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.domain.Range;
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.core.generators.PlainHtmlWriter4OneInter;

public class AwareAnnotationFactory {
	private static Logger logger = LoggerFactory.getLogger(AwareAnnotationFactory.class);

	public static void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	// local inner class inside the method
	public class JaroInfo {
		String wordFound;
		double jaroValue;

		public JaroInfo(String wordFound, double jaroValue) {
			this.wordFound = wordFound;
			this.jaroValue = jaroValue;
		}
	}

	// returns max jaro value between a word in the pattern and every word in the
	// text
	public static JaroInfo maxJaroValue(String text, String wordToFind) {
		JaroWinklerDistance jaro = new JaroWinklerDistance();
		double maxJaroValue = 0.0;
		String wordFound = "";
		for (String textWord : text.split("\\s+")) {
			// experiment: cleans "</p>" chars from textWord
			if (textWord.contains("</p>")) {
				textWord = textWord.substring(0, textWord.indexOf("</p>"));
			}
			if (jaro.apply(textWord, wordToFind) > maxJaroValue) {
				maxJaroValue = jaro.apply(textWord, wordToFind);
				wordFound = textWord;
			}
		}

		JaroInfo info = new AwareAnnotationFactory().new JaroInfo(wordFound, maxJaroValue);
		return info;
	}

	public static String cleanTweetText(String originalTweetText) {
		// regex
		String result = originalTweetText.toLowerCase().replaceAll("[\"*\\n;«»“”()]", "");

		// apagar apenas os hífenes e pontos que não fizerem parte de palavras
		int resultLen = result.length();
		int lastCharPos = resultLen - 1;
		String charSet = "-.,?q"; // 'q' porque muitas pessoas escrevem 'q' em vez de "que"
		for (int i = 0; i < resultLen; i++) {
			char c = result.charAt(i);
			// logger(result.charAt(i));
			if (charSet.indexOf(c) != -1) {
				// logger("entrei no primeiro if do CLEAN");
				result = cleanCharFromString(c, result, i, lastCharPos);
			}
		}
		return result;
	}

	public static String cleanCharFromString(char charToClean, String s, int position, int lastCharPos) {
		// !=lastCharPos serve para prevenior um IndexOutOfBound
		// logger("string s : " + s);
		// logger("position : " + position);
		// logger("lastCharPos : " + lastCharPos);

		// limpar hífenes que tenham espaços em branco à esquerda ou à direita
		if (charToClean == '-') {
			// logger("entrei no if do hífen");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' || (position != lastCharPos && s.charAt(position + 1) == ' ')) {
					s = s.substring(0, position) + ' ' + s.substring(position + 1);
				}
			}
		}
		// limpar pontos que tenham espaços em branco à esquerda e à direita
		else if (charToClean == '.') {
			// logger("entrei no if do ponto");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && (position != lastCharPos && s.charAt(position + 1) == ' ')) {
					s = s.substring(0, position) + ' ' + s.substring(position + 1);
				}
			}
		}
		// limpar vírgulas que tenham espaços em branco à esquerda e à direita
		else if (charToClean == ',') {
			// logger("entrei no if da vírgula");
			if (s.charAt(position - 1) == ' ' && (position != lastCharPos && s.charAt(position + 1) == ' ')) {
				s = s.substring(0, position) + ' ' + s.substring(position + 1);
			}
		}
		// limpar pontos de interrogação que tenham espaços em branco à esquerda e à
		// direita
		else if (charToClean == '?') {
			// logger("entrei no if do ponto de interrogação");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && (position != lastCharPos && s.charAt(position + 1) == ' ')) {
					s = s.substring(0, position) + ' ' + s.substring(position + 1);
				}
			}
		}
		// substituir as ocorrências da letra 'q' com espaços à esquerda e à direita por
		// "que"
		else if (charToClean == 'q') {
			// logger("entrei no if do \"q\"");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && (position != lastCharPos && s.charAt(position + 1) == ' ')) {
					s = s.substring(0, position) + "que" + s.substring(position + 1);
				}
			}
		}
		return s;
	}

	// main method of this Factory
	@Atomic
	public void create() throws IOException {
		logger.debug("BEGINNIG OF FACTORY");

		BufferedWriter bw = null;
		FileWriter fw = null;
		File file;
		file = new File("C:/Users/dnf_o/projetoTese/ldod/social/annot/annots.txt");
		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);

		// algoritmo do Annotation Factory
		LdoD ldoD = LdoD.getInstance();
		// allTwitterCitations - all twitter citations in the archive
		List<TwitterCitation> allTwitterCitations = ldoD.getCitationSet().stream()
				.filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
				.collect(Collectors.toList());

		logger("All Twitter Citations size: " + allTwitterCitations.size());
		bw.write("All Twitter Citations size: " + allTwitterCitations.size());
		bw.write("\n");
		for (VirtualEdition ve : ldoD.getVirtualEditionsSet()) {
			logger("VirtualEdition XMLID: " + ve.getXmlId());
			if (ve.isSAVE()) {
				searchForAwareAnnotations(ve, bw, allTwitterCitations);
			}
			logger(" +++++++++++++++++++++ NEXT VIRTUAL EDITION +++++++++++++++++++++++");
		}
		bw.close();
		fw.close();

		// testing code
		// populateWithAwareAnnotation(bw); bw.close(); fw.close();

		// pattern finding code was here
		// caso seja preciso fazer debug ao algoritmo, é só ir buscar ao backup e testar
		// aqui
		// com o ciclo for q percorre todas as twitter citations do sistema
		logger.debug("END OF FACTORY");
	}

	// método invocado também quando se cria uma nova SAVE
	@Atomic
	private void searchForAwareAnnotations(VirtualEdition ve, BufferedWriter bw,
			List<TwitterCitation> allTwitterCitations) throws IOException {
		logger(" ++++++++++++++++++++++++++ SAVE +++++++++++++++++++++++++++ ");
		logger(ve.getXmlId() + " is SAVE");
		logger("All Depth - Inters size: " + ve.getAllDepthVirtualEditionInters().size());
		logger("            Inters size: " + ve.getIntersSet().size());

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

			bw.write(" +++++++++++++++++++++ VE Inter+++++++++++++++++++++++++");
			bw.write("\n");
			bw.write("Inter external ID: " + inter.getExternalId());
			bw.write("\n");
			bw.write("Inter title: " + inter.getTitle());
			bw.write("\n");

			// currentTwitterCitations - current twitter citations from a certain
			// fragInter
			// totalTwitterCitations - total twitter citations from a certain fragInter
			// ^baseados nas anotações que já foram criadas para cada fraginter
			// pq para cada fraginter vai-se ver as anotações que foram criadas e
			// daí é q se extraem estes dois Sets
			List<TwitterCitation> currentTwitterCitations = getCurrentTwitterCitationsByInter(inter);
			logger("CurrentTwitterCitations set size: " + currentTwitterCitations.size());
			bw.write("CurrentTwitterCitations set size: " + currentTwitterCitations.size());
			bw.write("\n");

			List<TwitterCitation> totalTwitterCitations = getTotalTwitterCitationsByInter(allTwitterCitations, inter);
			logger("TotalTwitterCitations set size: " + totalTwitterCitations.size());
			bw.write(" TotalTwitterCitations set size: " + totalTwitterCitations.size());
			bw.write("\n");
			bw.write("\n");

			PlainHtmlWriter4OneInter htmlWriter = new PlainHtmlWriter4OneInter(inter);
			htmlWriter.write(false);
			String htmlTransc = htmlWriter.getTranscription();
			bw.write("htmlTransc: " + htmlTransc);
			bw.write("\n");
			bw.write("\n");

			// *********************** REMOVAL ****************************

			bw.write("TotalTwitterCitations set size BEFORE REMOVAL: " + totalTwitterCitations.size());
			bw.write("\n");
			totalTwitterCitations.removeAll(currentTwitterCitations);
			bw.write("TotalTwitterCitations set size AFTER REMOVAL: " + totalTwitterCitations.size());
			bw.write("\n");
			bw.write("\n");

			// debug do total já removido
			bw.write("Content of new citations to create for this frag inter:");
			bw.write("\n");
			for (TwitterCitation tc : totalTwitterCitations) {
				bw.write(" Tweet ID: " + tc.getTweetID() + " " + tc.getDate());
				bw.write("\n");
			}
			bw.write("----------------------");
			bw.write("\n");
			bw.write("\n");

			for (TwitterCitation newCitation : totalTwitterCitations) {
				logger(" ------------ TwitterCitation------");
				logger("Date: " + newCitation.getDate());
				logger("Tweet ID: " + newCitation.getTweetID());
				logger("Tweet Text: " + newCitation.getTweetText());
				logger("Frag external ID: " + newCitation.getFragment().getExternalId());
				logger("Frag Title: " + newCitation.getFragment().getTitle());
				logger("Frag Text: " + newCitation.getFragText());
				bw.write(" ----------------- Twitter Citation--------");
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

				createAwareAnnotation(inter, newCitation, bw);
				count++;
			}
		}
	}

	// método responsável por criar aware annotation no vei com meta informação
	// contida na tc
	@Atomic(mode = TxMode.WRITE)
	public void createAwareAnnotation(VirtualEditionInter vei, TwitterCitation tc, BufferedWriter bw)
			throws IOException {
		bw.write("Entrei no create aware annotation");
		bw.write("\n");

		PlainHtmlWriter4OneInter htmlWriter = new PlainHtmlWriter4OneInter(vei);
		htmlWriter.write(false);
		String htmlTransc = htmlWriter.getTranscription();
		logger("htmlTransc: " + htmlTransc);

		String tempTweetText = "Poderei ir buscar riqueza ao Oriente, mas não riqueza de alma, porque a riqueza de minha alma sou eu, e eu estou onde estou, sem Oriente ou com ele.\r\n"
				+ "\r\n"
				+ "Compreendo que viaje quem é incapaz de sentir. Por isso são tão pobres sempre como livros de experiência os livros de viagens, valendo somente pela imaginação de quem os escreve";
		// List<String> result = patternFinding(htmlTransc, tempTweetText); //para
		// testar
		List<String> result = patternFinding(htmlTransc, tc.getTweetText(), bw); // descomentar

		// ********************************** CREATE ANNOTATION AND RANGE
		// ********************************
		String annotQuote = result.get(0); // string matching algorithm
		int htmlStart = Integer.parseInt(result.get(1));
		int htmlEnd = Integer.parseInt(result.get(2));
		int numOfPStart = Integer.parseInt(result.get(3));
		int numOfPEnd = Integer.parseInt(result.get(4));

		if (htmlStart != -1 && htmlEnd != -1 && annotQuote != "") {
			bw.write("GOING TO CREATE AN AWARE ANNOTATION!!!");
			bw.write("\n");

			LdoDUser user = LdoD.getInstance().getUser("ars");
			String annotText = "tweet meta information"; // meta information inside citation object

			bw.write("htmlTransc (repeated): " + htmlTransc);
			bw.write("\n");
			bw.write("\n");

			bw.write("------------Tweet Text (repeated): " + tc.getTweetText());
			bw.write("\n");

			bw.write("------------Annotation quote: " + annotQuote);
			bw.write("\n");
			bw.write("\n");

			bw.write("Número de <p START: " + numOfPStart);
			bw.write("\n");
			bw.write("Número de <p END: " + numOfPEnd);
			bw.write("\n");

			bw.write("Índice do htmlStart: " + htmlStart);
			bw.write("\n");
			bw.write("Índice do htmlEnd: " + htmlEnd);
			bw.write("\n");
			bw.write("\n");

			if (htmlStart > htmlEnd && numOfPStart == numOfPEnd) {
				bw.write("start is bigger than end!!");
				bw.write("\n");
				bw.write("\n");
			}

			// HumanAnnotation annotation = new HumanAnnotation(vei, null, null, annotQuote,
			// annotText, user);
			// AwareAnnotation annotation = new AwareAnnotation(vei, annotQuote, annotText,
			// null); //para testar
			AwareAnnotation annotation = new AwareAnnotation(vei, annotQuote, annotText, tc); // descomentar

			new Range(annotation, "/div[1]/div[1]/p[" + numOfPStart + "]", htmlStart,
					"/div[1]/div[1]/p[" + numOfPEnd + "]", htmlEnd);

			logger("numOfP START: " + numOfPStart);
			logger("numOfP END: " + numOfPEnd);
		}
	}

	public List<String> patternFinding(String text, String tweet, BufferedWriter bw) throws IOException {
		logger("------------------------------ PATTERN FINDING ALGORITHM -------------------------");

		// used for debugging
		/*
		 * BufferedWriter bw = null; FileWriter fw = null; File file; //file = new File(
		 * "C:/Users/dnf_o/projetoTese/ldod/social/citationsPositions/positions.txt");
		 * file = new File(
		 * "C:/Users/dnf_o/projetoTese/ldod/social/citationsPositions/htmlPositions.txt"
		 * ); fw = new FileWriter(file); bw = new BufferedWriter(fw);
		 */

		// é chato pôr o text é lowercase pq estamos a adulterar a informação original,
		// experimentar outra distance em vez do Jaro
		text = text.toLowerCase();
		// o "clean" já mete o tweet em lowerCase
		tweet = cleanTweetText(tweet);

		// variables updated over iteration
		int start = -1; // -1 means that the pattern was not found, either for start and end
		int end = -1;
		int offset = 0;
		String patternFound = "";

		// parameters that can be adjusted
		int window = 10;
		double jaroThreshold = 0.9;
		int startCorrectParam = 3; // parâmetro utilizado na correção da start position

		// algorithm
		int count = 0; // aux counter to check if we reach the minimum value set by "window" variable
		outerloop: for (String initialWord : tweet.split("\\s+")) {
			for (String word : initialWord.split(",")) {
				logger("--------------------------------------------------");
				offset = Math.max(start, end);
				if (offset == -1) {
					offset = 0;
				}
				logger("offset: " + offset);

				JaroInfo info = maxJaroValue(text.substring(offset), word);
				logger("tweet word: " + word);
				logger("text word: " + info.wordFound);

				// a palavra tem de existir no texto e estar à frente do offset!
				// primeira palavra encontrada
				if (info.jaroValue > jaroThreshold && text.indexOf(info.wordFound, offset) != -1) {
					logger("	text contains this word");
					logger(text.indexOf(info.wordFound, offset));
					logger(info.jaroValue);
					// é só updated uma vez e é quando o início começa bem
					if (count == 0) {
						// é só updated uma vez e é quando o início começa bem
						start = text.indexOf(info.wordFound, offset);
						logger("	dei update do start para: " + start);
						logger("	a palavra encontrada no Texto foi: " + info.wordFound);
						patternFound += info.wordFound + " ";
						count = 1;
					}
					// restantes palavras encontradas
					// vai sendo constantemente updated enquanto corre bem
					else {
						// entra neste if para dar o update exato do start
						// pq a primeira palavra do padrão pode ocorrer várias vezes no texto antes de
						// ocorrer no padrão
						// o mais correto seria fazer quando count==1 (pq é quando já recolhemos pelo
						// menos uma palavra)
						// mas como o offset só é updated no início de cada ciclo temos de esperar uma
						// iteração
						if (count == startCorrectParam) {
							// este update ao start dá bug quando as palavras iniciais do padrão aparecem
							// antes do padrão
							logger("	padrão até agora: " + patternFound);

							String[] splits = patternFound.split(" ");
							String firstWordOfPatternFound = splits[0];
							logger("	primeira palavra: " + firstWordOfPatternFound);
							String lastWordOfPatternFound = splits[splits.length - 1];
							logger("	última palavra: " + lastWordOfPatternFound);

							start = text.lastIndexOf(firstWordOfPatternFound, offset - lastWordOfPatternFound.length());
							logger("	dei update do start para: " + start);
						}
						end = text.indexOf(info.wordFound, offset) + info.wordFound.length();
						logger("	dei update do end para: " + end);
						logger("	a palavra encontrada no Texto foi: " + info.wordFound);
						patternFound += info.wordFound + " ";
						count++;
					}
				}
				// caso em q a palavra não existe no texto
				else {
					logger("	text DOES NOT contains this word");
					logger(info.jaroValue);
					if (count < window) { // significa que não fizémos o número mínimo de palavras seguidas, logo é dar
											// reset!!
						count = 0;
						start = -1;
						end = -1;
						patternFound = "";
						logger("	dei reset ao count, next word!");
					} else {
						logger("	vou dar break pq já garanti a window");
						break outerloop;
					}
				}
				logger("	count: " + count);
			} // for interno
		} // for externo

		// writes de debug
		/*
		 * bw.write("***************** CITATION **************************");
		 * bw.write("\n");
		 * 
		 * bw.write("Start index: " + start); bw.write("\n"); bw.write("End index: " +
		 * end); bw.write("\n"); bw.write("Pattern found: " + patternFound);
		 * bw.write("\n"); bw.write("Pattern clean: " + tweet); bw.write("\n");
		 */

		if (patternFound.equals("")) {
			// bw.write(" Pattern does not exist!");
			// bw.write("\n");
		}

		if (count < window && start != -1) { // caso em que o padrão até existe mas não respeita a window
			logger("	Pattern may exist but does not respect window size!");
			start = -1;
			end = -1;
			patternFound = "";
		}

		// já não deve ser preciso pq já faço esta verificação no método maxJaroValue
		// assim, por norma já não meto lixo no patternFound, logo não é preciso fazer
		// esta limpeza no final
		/*
		 * //experiment, must continuously checking for bugs //removes </p> and <p in
		 * the end of the patternFound if(patternFound.indexOf("</p>") != -1) {
		 * if(patternFound.indexOf("<p") != -1) { //entra neste if se o padrão contive a
		 * palavra "</p><p" end-=2; //-2 pq é o comprimento da palavra "<p" }
		 * logger("entrei no if do contaitns </p>"); patternFound =
		 * patternFound.substring(0, patternFound.indexOf("</p>")); end-=4; //para
		 * adicionalmente ao -2, os 4 caracteres a mais da palavra "</p>"
		 * 
		 * }
		 */

		if (start == -1 || end == -1) {
			// bw.write(" start or end is -1!!");
			// bw.write("\n");
		}

		int numOfPStart = -1;
		int numOfPEnd = -1;
		int htmlStart = -1;
		int htmlEnd = -1;
		if (start != -1 && end != -1) {
			// HTML treatment
			numOfPStart = 1 + countOccurencesOfSubstring(text, "<p", start); // +1 porque o getTranscription não traz o
																				// primeiro <p
			logger("Número de <p START: " + numOfPStart);
			numOfPEnd = 1 + countOccurencesOfSubstring(text, "<p", end); // +1 porque o getTranscription não traz o
																			// primeiro <p
			logger("Número de <p END: " + numOfPEnd);

			// logger("Índice do último para o start '>': " + text.lastIndexOf("\">",
			// start));
			// logger("Índice do último para o end '>': " + text.lastIndexOf("\">", end));

			htmlStart = start - text.lastIndexOf("\">", start) - 2; // -2, para compensar
			htmlEnd = end - text.lastIndexOf("\">", end) - 2; // -2, para compensar
			logger("Índice do htmlStart: " + htmlStart);
			logger("Índice do htmlEnd: " + htmlEnd);

			// *************** writing in file html stuff **************
			/*
			 * //HTML treatment bw.write("Número de <p START: " + numOfPStart);
			 * bw.write("\n"); bw.write("Número de <p END: " + numOfPEnd); bw.write("\n");
			 * 
			 * 
			 * bw.write("Índice do último '>': " + text.lastIndexOf(">", start));
			 * bw.write("\n"); bw.write("Índice do htmlStart: " + htmlStart);
			 * bw.write("\n"); bw.write("Índice do htmlEnd: " + htmlEnd); bw.write("\n");
			 * bw.write("\n");
			 * 
			 * 
			 * bw.write(text); bw.write("\n");
			 */
		}

		// bw.close();
		// fw.close();

		List<String> result = new ArrayList<String>();
		result.add(patternFound);
		result.add(String.valueOf(htmlStart));
		result.add(String.valueOf(htmlEnd));
		result.add(String.valueOf(numOfPStart));
		result.add(String.valueOf(numOfPEnd));

		return result;
	}

	// método de teste utilizado para criar uma aware annotation
	public void populateWithAwareAnnotation(BufferedWriter bw) throws IOException {
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
		// createAwareAnnotation(vei, null); //para testar

		/*
		 * logger("Categories: "); Set<Category> categories =
		 * duarteEdit.getTaxonomy().getCategoriesSet(); for(Category c: categories) {
		 * logger("Category name: " + c.getName()); for(Tag t: c.getTagSet()) {
		 * logger("Tag: "); logger("Título do fraginter da tag: " +
		 * t.getInter().getTitle()); logger("Quote: " + t.getAnnotation().getQuote());
		 * logger("Text: " + t.getAnnotation().getText()); logger("User: " +
		 * t.getAnnotation().getUser().getUsername());
		 * 
		 * for(Range r: t.getAnnotation().getRangeSet()) { logger("Range: ");
		 * logger("Start: " + r.getStart()); logger("Start Offset: " +
		 * r.getStartOffset()); logger("End: " + r.getEnd()); logger("End Offset: " +
		 * r.getEndOffset()); } } }
		 */
	}

	// debug method - writes on annotsDetails.txt details about annotations
	private void annotsDetails(VirtualEdition ve, BufferedWriter bw) throws IOException {
		List<TwitterCitation> allTwitterCitations = LdoD.getInstance().getCitationSet().stream()
				.filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
				.collect(Collectors.toList());

		int count = 0;
		for (VirtualEditionInter inter : ve.getAllDepthVirtualEditionInters()) {
			bw.write("Inter title: " + inter.getTitle());
			bw.write("\n");

			List<TwitterCitation> totalTwitterCitations = getTotalTwitterCitationsByInter(allTwitterCitations, inter);
			bw.write("	TotalTwitterCitations set size: " + totalTwitterCitations.size());
			bw.write("\n");

			for (TwitterCitation tc : totalTwitterCitations) {
				bw.write("	Count = " + count);
				bw.write("\n");
				bw.write("		Tweet ID: " + tc.getTweetID() + " " + tc.getDate());
				bw.write("\n");
				count++;
			}
			bw.write("--------------------------------------");
			bw.write("\n");
			bw.write("\n");
		}
	}

	public List<TwitterCitation> getTotalTwitterCitationsByInter(List<TwitterCitation> allTwitterCitations,
			VirtualEditionInter inter) {
		List<TwitterCitation> totalTwitterCitations = new ArrayList<TwitterCitation>();

		for (TwitterCitation tc : allTwitterCitations) {
			if (tc.getFragment().getFragmentInterSet().contains(inter))
				totalTwitterCitations.add(tc);
		}

		return totalTwitterCitations;
	}

	public List<TwitterCitation> getCurrentTwitterCitationsByInter(VirtualEditionInter inter) {
		List<TwitterCitation> twitterCitations = new ArrayList<TwitterCitation>();
		List<AwareAnnotation> awareAnnotations = inter.getAnnotationSet().stream()
				.filter(AwareAnnotation.class::isInstance).map(AwareAnnotation.class::cast)
				.collect(Collectors.toList());

		logger(" #################################### ");
		logger("Estou dentro do getCurrentTwitterCitationsByInter");
		logger("Aware annotations size: " + awareAnnotations.size());

		for (AwareAnnotation aa : awareAnnotations) {
			logger("aa external id: " + aa.getExternalId());
			twitterCitations.add((TwitterCitation) aa.getCitation());
			logger("twitterCitations size: " + twitterCitations.size());

		}

		logger("twitterCitations size: " + twitterCitations.size());
		logger(" #################################### ");
		return twitterCitations;
	}

	public int countOccurencesOfSubstring(final String string, final String substring, final int subsStartPos) {
		int count = 0;
		int idx = 0;

		while ((idx = string.indexOf(substring, idx)) != -1 && idx < subsStartPos) {
			idx++;
			count++;
		}

		return count;
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
