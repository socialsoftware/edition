package pt.ist.socialsoftware.edition.ldod.social.aware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.InfoRange;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.search.IgnoreDiacriticsAnalyzer;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class CitationDetecter {

	private static Logger logger = LoggerFactory.getLogger(CitationDetecter.class);

	private static final String ID = "id";
	private static final String TEXT = "text";

	private static Path docDir;
	private static Analyzer analyzer;
	private static QueryParserBase queryParser;

	// just for checking score in cited fragments
	private static BufferedWriter bw;
	private static FileWriter fw;
	private static File toWriteFile;

	public static void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	public CitationDetecter() throws IOException {
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		docDir = Paths.get(path);
		analyzer = new IgnoreDiacriticsAnalyzer(); // experimentar outros analyzers e testar
		queryParser = new QueryParser(TEXT, analyzer);

		// just for checking score in cited fragments
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		toWriteFile = new File("C:/Users/dnf_o/projetoTese/ldod/social/awareGeneric/" + "citations-" + "generic" + "-"
				+ timeStamp + ".txt");
		fw = new FileWriter(toWriteFile);
		bw = new BufferedWriter(fw);
	}

	@Atomic(mode = TxMode.WRITE)
	public void detect() throws IOException {
		logger("STARTING CITATION DETECTER!!");
		// // serve só para testar melhor pq dá reset ao id na base de dados:
		// //LdoD.getInstance().getLastTwitterID().resetTwitterIDS();
		citationDetection();
		logger("FINISHED DETECTING CITATIONS!!!");

		// indetify ranges here
		logger("STARTED IDENTIFYING RANGES!!!");
		BufferedWriter bw = null;
		FileWriter fw = null;
		File file;
		file = new File("C:/Users/dnf_o/projetoTese/ldod/social/infoRanges/infoRanges.txt");
		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);

		bw.write("teste de info ranges!!!\n");

		for (Citation citation : LdoD.getInstance().getCitationSet()) {
			if (citation.getInfoRangeSet().isEmpty()) {
				bw.write("----------------- CITATION---------------------\n");
				bw.write("Tweet ID: " + citation.getId() + "\n");
				Fragment citationFragment = citation.getFragment();
				Set<FragInter> inters = new HashSet<FragInter>(citationFragment.getFragmentInterSet());
				bw.write("Todos os frag inters:\n");
				for (FragInter inter : inters) {
					bw.write(" FragInter id: " + inter.getExternalId() + "\n");
					bw.write(" XML id: " + inter.getXmlId() + "\n");
					bw.write(" Title: " + inter.getTitle() + "\n");
					bw.write("\n");

				}
				inters.removeAll(citationFragment.getVirtualEditionInters());

				bw.write("Excepto os virtual:\n");
				for (FragInter inter : inters) {
					bw.write(" FragInter id: " + inter.getExternalId() + "\n");
					bw.write(" XML id: " + inter.getXmlId() + "\n");
					bw.write(" Title: " + inter.getTitle() + "\n");
					bw.write("\n");
				}

				int editionCount = 0;
				for (FragInter inter : inters) {
					bw.write("------------ ENTREI NO INFO RANGE ------------------\n");
					editionCount++;
					createInfoRange(inter, citation, bw);
				}
				bw.write("Potential edition count = " + editionCount + "\n");
			}
		}

		bw.close();
		fw.close();
		logger("FINISHED IDENTIFYING RANGES!!!");
	}

	private void createInfoRange(FragInter inter, Citation citation, BufferedWriter bw) throws IOException {
		String htmlTransc = getHtmlTransc(inter);

		if (citation instanceof TwitterCitation) {
			List<String> result = patternFinding(htmlTransc, ((TwitterCitation) citation).getTweetText(), bw);

			String infoQuote = result.get(0);
			int htmlStart = Integer.parseInt(result.get(1));
			int htmlEnd = Integer.parseInt(result.get(2));
			int numOfPStart = Integer.parseInt(result.get(3));
			int numOfPEnd = Integer.parseInt(result.get(4));

			if (htmlStart != -1 && htmlEnd != -1 && infoQuote != ""
					&& !startBiggerThanEnd(htmlStart, htmlEnd, numOfPStart, numOfPEnd)) {
				bw.write("GOING TO CREATE A INFO RANGE!!!");
				bw.write("\n");

				String infoText = createInfoText(citation, bw);

				bw.write("htmlTransc: " + htmlTransc);
				bw.write("\n");
				bw.write("\n");

				bw.write("------------Tweet Text: " + ((TwitterCitation) citation).getTweetText());
				bw.write("\n");

				bw.write("------------Info Range quote: " + infoQuote);
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

				// NEW INFO RANGE
				new InfoRange(citation, inter, "/div[1]/div[1]/p[" + numOfPStart + "]", htmlStart,
						"/div[1]/div[1]/p[" + numOfPEnd + "]", htmlEnd, infoQuote, infoText);
			}
		}
	}

	private String createInfoText(Citation citation, BufferedWriter bw) throws IOException {
		// exemplo inicial
		String infoText = "tweet meta information"; // meta information inside citation object

		// TODO concatenate citation meta information into infoText
		String sourceLink = citation.getSourceLink();
		String date = citation.getDate();
		String tweetID = Long.toString(citation.getId());
		String location = ((TwitterCitation) citation).getLocation();
		String country = ((TwitterCitation) citation).getCountry();
		String username = ((TwitterCitation) citation).getUsername();
		String userProfileURL = ((TwitterCitation) citation).getUserProfileURL();

		infoText = "SOURCE LINK: " + sourceLink + "\n" + "DATE: " + date + "\n" + "TWEET ID: " + tweetID + "\n"
				+ "COUNTRY: " + country + "\n" + "LOCATION: " + location + "\n" + "USERNAME: " + username + "\n"
				+ "USER PROFILE: " + userProfileURL;
		bw.write(infoText + "\n");

		return infoText;
	}

	private String getHtmlTransc(FragInter inter) {
		PlainHtmlWriter4OneInter htmlWriter = new PlainHtmlWriter4OneInter(inter);
		htmlWriter.write(false);
		String htmlTransc = htmlWriter.getTranscription();
		return htmlTransc;
	}

	private boolean startBiggerThanEnd(int htmlStart, int htmlEnd, int numOfPStart, int numOfPEnd) {
		return (htmlStart > htmlEnd && numOfPStart == numOfPEnd);
	}

	private List<String> patternFinding(String text, String tweet, BufferedWriter bw) throws IOException {
		logger("------------------------------ PATTERN FINDING ALGORITHM -------------------------");

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

				List<String> info = maxJaroValue(text.substring(offset), word);
				String wordFound = info.get(0);
				double jaroValue = Double.parseDouble(info.get(1));

				logger("tweet word: " + word);
				logger("text word: " + wordFound);

				// a palavra tem de existir no texto e estar à frente do offset!
				// primeira palavra encontrada
				if (jaroValue > jaroThreshold && text.indexOf(wordFound, offset) != -1) {
					logger("	text contains this word");
					logger(text.indexOf(wordFound, offset));
					logger(jaroValue);
					// é só updated uma vez e é quando o início começa bem
					if (count == 0) {
						// é só updated uma vez e é quando o início começa bem
						start = text.indexOf(wordFound, offset);
						logger("	dei update do start para: " + start);
						logger("	a palavra encontrada no Texto foi: " + wordFound);
						patternFound += wordFound + " ";
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
						end = text.indexOf(wordFound, offset) + wordFound.length();
						logger("	dei update do end para: " + end);
						logger("	a palavra encontrada no Texto foi: " + wordFound);
						patternFound += wordFound + " ";
						count++;
					}
				}
				// caso em q a palavra não existe no texto
				else {
					logger("	text DOES NOT contains this word");
					logger(jaroValue);
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

	private void citationDetection() throws IOException, FileNotFoundException {
		File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
		for (File fileEntry : folder.listFiles()) {
			System.out.println("+++++++++++++++++++++++++++++++++++ JSON ++++++++++++++++++++++++++++++++++++");
			System.out.println(fileEntry.getName());
			bw.write("+++++++++++++++++++++++++++++++++++ JSON ++++++++++++++++++++++++++++++++++++");
			bw.write("\n");
			bw.write(fileEntry.getName());
			bw.write("\n");

			try {
				JSONObject obj = new JSONObject();
				String line = null;

				BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));

				// criar um tempMaxID que guarda o valor de
				// LdoD.getInstance().getLastTwitterID()
				// pq é preciso darmos set na base de dados do valor antes do while, pq vem logo
				// na primeira linha
				long tempMaxID = LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName());

				int lineNum = 0;
				while ((line = bufferedReader.readLine()) != null) {
					obj = (JSONObject) new JSONParser().parse(line);

					if (lineNum == 0) {
						// prints e writes para debug
						logger("----------- PRIMEIRA LINHA ---------------");
						logger("LdoD last twitter ID: "
								+ LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName()));
						logger("Date: " + (String) obj.get("date"));
						logger("----------- RESTANTES LINHAS ---------------");

						bw.write("----------- PRIMEIRA LINHA ---------------");
						bw.write("\n");
						bw.write("LdoD last twitter ID: "
								+ LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName()));
						bw.write("\n");
						bw.write("Date: " + (String) obj.get("date"));
						bw.write("\n");
						bw.write("----------- RESTANTES LINHAS ---------------");
						bw.write("\n");

						if ((long) obj.get("tweetID") > LdoD.getInstance().getLastTwitterID()
								.getLastTwitterID(fileEntry.getName())) {
							LdoD.getInstance().getLastTwitterID().updateLastTwitterID(fileEntry.getName(),
									(long) obj.get("tweetID"));
						}
					}
					lineNum++;
					logger("tempMaxID: " + tempMaxID);

					// por este if depois de dar update: updateLastTwitterID(fileEntry.getName()
					if (obj.containsKey("isRetweet") && (boolean) obj.get("isRetweet")) {
						continue;
					}

					if ((long) obj.get("tweetID") > tempMaxID) {
						System.out.println("Date: " + (String) obj.get("date"));
						String tweetTextWithoutHttp = removeHttpFromTweetText(obj);

						// System.out.println("JSON Text: " + tweetTextWithoutHttp);
						// System.out.println("++++++++++++++++++++++++++++++++++++++++");
						bw.write("\n");
						bw.write("Date: " + (String) obj.get("date"));
						bw.write("\n");
						bw.write("TweetID: " + (long) obj.get("tweetID"));
						bw.write("\n");
						bw.write("JSON Text: " + tweetTextWithoutHttp);
						bw.write("\n");
						bw.write("++++++++++++++++++++++++++++++++++++++++");
						bw.write("\n");

						// count++;

						if (!tweetTextWithoutHttp.equals("")) {
							searchQueryParserJSON(tweetTextWithoutHttp, obj);
							// searchQueryParser(absoluteSearch(tweetTextWithoutHttp)); //demasiado rígida,
							// nao funciona no nosso caso
						}

						System.out.println(
								"-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
						bw.write(
								"-------------------------------- NEXT!!!!!!!!!!!!!!!!!! -----------------------------------------");
						bw.write("\n");
					} else {
						break;
					}
				} // chaveta do while
				bufferedReader.close();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}
		} // chaveta do for
		logger.debug("LdoD BookLastTwitterID:{}", LdoD.getInstance().getLastTwitterID().getBookLastTwitterID());
		logger.debug("LdoD BernardoLastTwitterID:{}", LdoD.getInstance().getLastTwitterID().getBernardoLastTwitterID());
		logger.debug("LdoD VicenteLastTwitterID:{}", LdoD.getInstance().getLastTwitterID().getVicenteLastTwitterID());
		logger.debug("LdoD PessoaLastTwitterID:{}", LdoD.getInstance().getLastTwitterID().getPessoaLastTwitterID());

		bw.close();
		fw.close();
	}

	public void searchQueryParserJSON(String query, JSONObject obj)
			throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
		Query parsedQuery = queryParser.parse(QueryParser.escape(query)); // escape foi a solução porque ele stressava
																			// com o EOF

		System.out.println("ParsedQuery: " + parsedQuery.toString());
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		bw.write("ParsedQuery: " + parsedQuery.toString());
		bw.write("\n");
		bw.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		bw.write("\n");

		searchIndexAndDisplayResultsJSON(parsedQuery, obj);
	}

	public void searchIndexAndDisplayResultsJSON(Query query, JSONObject obj) {
		try {
			int hitsPerPage = 5;
			Directory directory = new NIOFSDirectory(docDir);
			IndexReader idxReader = DirectoryReader.open(directory);
			IndexSearcher idxSearcher = new IndexSearcher(idxReader);

			ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
			if (hits.length > 0) {
				int docId = hits[0].doc;
				float score = hits[0].score;
				if (score > 30) {
					Document d = idxSearcher.doc(docId);
					bw.write("SCORE IS HIGH ENOUGH: " + score);
					bw.write("\n");
					System.out.println("Text: " + d.get(TEXT) + "\n" + "DocID: " + docId + "\t" + "FragInter ID: "
							+ d.get(ID) + "\t" + " Score: " + score);
					System.out.println("--------------------------------------------------------\n");
					bw.write("Text: " + d.get(TEXT) + "\n" + "DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID)
							+ "\t" + "Document Class: " + d.getClass() + "\t" + " Score: " + score);

					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");

					// ******************************** CREATE CITATIONS
					// ***************************************/

					// necessary because the same tweet was collected using different keywords in
					// FetchCitationsFromTwitter class
					// check if twitter ID already exists in the list of Citations
					// if it does idExists=true, therefore we don't create a citation for it!
					Set<TwitterCitation> allTwitterCitations = LdoD.getInstance().getCitationSet().stream()
							.filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
							.collect(Collectors.toSet());
					boolean twitterIDExists = false;
					for (TwitterCitation tc : allTwitterCitations) {
						if (tc.getTweetID() == (long) obj.get("tweetID")) {
							twitterIDExists = true;
							// bw.write("TWITTER ID ALREADY EXISTS!!\n");
							break;
						}
					}
					if (!twitterIDExists) {
						// obtain Fragment
						// using external id
						FragInter inter = FenixFramework.getDomainObject(d.get(ID));
						bw.write("---------- USING EXTERNAL ID----------------\n");
						Fragment fragment = inter.getFragment();
						bw.write("Fragment itself (using ExternalID): " + fragment);
						bw.write("\n");
						if (fragment != null) {
							bw.write("Fragment was not null!!");
							bw.write("\n");
							bw.write("Fragment External ID: " + fragment.getExternalId());
							bw.write("\n");
						}

						String tweetTextWithoutHttp = removeHttpFromTweetText(obj);

						bw.write("CREATED A NEW TWITTER CITATION!!");
						bw.write("\n");
						new TwitterCitation(fragment, (String) obj.get("tweetURL"), (String) obj.get("date"),
								d.get(TEXT), tweetTextWithoutHttp, (long) obj.get("tweetID"),
								(String) obj.get("location"), (String) obj.get("country"), (String) obj.get("username"),
								(String) obj.get("profURL"), (String) obj.get("profImg"));
					}

				} else {
					bw.write("SCORE IS TOO LOW: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				}
			}
			directory.close();
			// idxReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// returns max jaro value between a word in the pattern and every word in the
	// text
	private List<String> maxJaroValue(String text, String wordToFind) {
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

		// JaroInfo info = new AwareAnnotationFactory().new JaroInfo(wordFound,
		// maxJaroValue);
		List<String> info = new ArrayList<String>();
		info.add(wordFound);
		info.add(String.valueOf(maxJaroValue));
		return info;
	}

	private String cleanTweetText(String originalTweetText) {
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

	private String cleanCharFromString(char charToClean, String s, int position, int lastCharPos) {
		// !=lastCharPos serve para prevenior um IndexOutOfBound
		// logger("string s : " + s);
		// logger("position : " + position);
		// logger("lastCharPos : " + lastCharPos);

		// limpar hífenes que tenham espaços em branco à esquerda ou à direita
		if (charToClean == '-') {
			// logger("entrei no if do hífen");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' || position != lastCharPos && s.charAt(position + 1) == ' ') {
					s = s.substring(0, position) + ' ' + s.substring(position + 1);
				}
			}
		}
		// limpar pontos que tenham espaços em branco à esquerda e à direita
		else if (charToClean == '.') {
			// logger("entrei no if do ponto");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
					s = s.substring(0, position) + ' ' + s.substring(position + 1);
				}
			}
		}
		// limpar vírgulas que tenham espaços em branco à esquerda e à direita
		else if (charToClean == ',') {
			// logger("entrei no if da vírgula");
			if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
				s = s.substring(0, position) + ' ' + s.substring(position + 1);
			}
		}
		// limpar pontos de interrogação que tenham espaços em branco à esquerda e à
		// direita
		else if (charToClean == '?') {
			// logger("entrei no if do ponto de interrogação");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
					s = s.substring(0, position) + ' ' + s.substring(position + 1);
				}
			}
		}
		// substituir as ocorrências da letra 'q' com espaços à esquerda e à direita por
		// "que"
		else if (charToClean == 'q') {
			// logger("entrei no if do \"q\"");
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
					s = s.substring(0, position) + "que" + s.substring(position + 1);
				}
			}
		}
		return s;
	}

	private String removeHttpFromTweetText(JSONObject obj) {
		// Nota: o tweet text que é passado ao construtor tem os https ainda!
		// (String)obj.get("text") - está errado, temos de limpar os https!!
		String tweetText = (String) obj.get("text");
		String tweetTextWithoutHttp = tweetText; // caso não tenha o "http"

		// removing "http" from tweet text
		if (tweetText.contains("http")) {
			int httpIndex = tweetText.indexOf("http");
			tweetTextWithoutHttp = tweetText.substring(0, httpIndex);
		}
		return tweetTextWithoutHttp;
	}

	public void searchQueryParser(String query)
			throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
		// QueryParser parser = new QueryParser(TEXT, analyzer);
		Query parsedQuery = queryParser.parse(QueryParser.escape(query)); // escape foi a solução porque ele stressava
																			// com o EOF

		System.out.println("ParsedQuery: " + parsedQuery.toString());
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		bw.write("ParsedQuery: " + parsedQuery.toString());
		bw.write("\n");
		bw.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		bw.write("\n");

		searchIndexAndDisplayResults(parsedQuery);
	}

	public void searchIndexAndDisplayResults(Query query) {
		try {
			int hitsPerPage = 5;
			Directory directory = new NIOFSDirectory(docDir);
			IndexReader idxReader = DirectoryReader.open(directory);
			IndexSearcher idxSearcher = new IndexSearcher(idxReader);

			ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
			if (hits.length > 0) {
				int docId = hits[0].doc;
				float score = hits[0].score;
				if (score > 30) {
					Document d = idxSearcher.doc(docId);
					System.out.println("Text: " + d.get(TEXT) + "\n" + "DocID: " + docId + "\t" + "FragInter ID: "
							+ d.get(ID) + "\t" + " Score: " + score);
					System.out.println("--------------------------------------------------------\n");
					bw.write("Text: " + d.get(TEXT) + "\n" + "DocID: " + docId + "\t" + "FragInter ID: " + d.get(ID)
							+ "\t" + " Score: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				} else {
					bw.write("SCORE IS TOO LOW: " + score);
					bw.write("\n");
					bw.write("--------------------------------------------------------\n");
					bw.write("\n");
				}
			}
			directory.close();
			// idxReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Fuzzy Query - multiple terms! pesquisa tem de ser exata
	public void searchSpanQuery(String words) {
		String[] split = words.split("\\s+");
		int len = split.length;
		SpanQuery[] clauses = new SpanQuery[len];
		for (int i = 0; i < len; i++) {
			clauses[i] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term(TEXT, split[i])));
		}
		SpanNearQuery query = new SpanNearQuery(clauses, 0, true);

		searchIndexAndDisplayResults(query);
	}

	// Fuzzy Search - Pesquisa tem de ser exata
	// Search for fragments with a set of words similar to input
	// Fuzzy set for a minimum edition edition of 1
	public void fuzzySearch(String words)
			throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
		String[] split = words.split("\\s+");
		double fuzzy = 1; // default = 0.5
		String query = "" + split[0] + "~" + fuzzy;
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i] + "~" + fuzzy;
		}
		searchQueryParser(query);
	}

	// Search for fragments with a set of equal to inputs
	private String absoluteSearch(String words) {
		String[] split = words.split("\\s+");
		String query = "" + split[0];
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i];
		}
		return query;
	}

	private void searchSingleTerm(String field, String termText) {
		Term term = new Term(field, termText);
		TermQuery termQuery = new TermQuery(term);

		searchIndexAndDisplayResults(termQuery);
	}

	private int countOccurencesOfSubstring(final String string, final String substring, final int subsStartPos) {
		int count = 0;
		int idx = 0;

		while ((idx = string.indexOf(substring, idx)) != -1 && idx < subsStartPos) {
			idx++;
			count++;
		}

		return count;
	}
}
