package pt.ist.socialsoftware.edition.virtual.feature.socialaware;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
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

import pt.ist.socialsoftware.edition.notification.dtos.text.CitationDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;

import pt.ist.socialsoftware.edition.virtual.domain.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CitationDetecter {
    private final Logger logger = LoggerFactory.getLogger(CitationDetecter.class);


    private final int LUCENE_THRESHOLD = 20;
    private final int WINDOW_THRESHOLD = 13;

    private final String ID = "id";
    private final String TEXT = "text";

    private final Path docDir;
    private final Analyzer analyzer;
    private final QueryParserBase queryParser;

    public CitationDetecter() throws IOException {
        String path = PropertiesManager.getProperties().getProperty("indexer.dir");
        this.docDir = Paths.get(path);
        this.analyzer = new IgnoreDiacriticsAnalyzer();
        this.queryParser = new QueryParser(this.TEXT, this.analyzer);
    }

    public void detect() throws IOException {
        this.logger.debug("STARTING CITATION DETECTER!!");
        // resets last twitter IDs
        citationDetection();
        this.logger.debug("FINISHED DETECTING CITATIONS!!!");

        // identify ranges
        this.logger.debug("STARTED IDENTIFYING RANGES!!!");
        createInfoRanges();
        this.logger.debug("FINISHED IDENTIFYING RANGES!!!");

        printNumberOfCitationsWithInfoRanges();

        this.logger.debug("STARTED REMOVING TWEETS WITHOUT CITATIONS!!!");
        removeTweetsWithoutCitationsWithInfoRange();
        this.logger.debug("FINISHED REMOVING TWEETS WITHOUT CITATIONS!!!");

        printNumberOfCitationsWithInfoRanges();
    }

    @Atomic(mode = TxMode.WRITE)
    private void removeTweetsWithoutCitationsWithInfoRange() {
        VirtualModule.getInstance().removeTweetsWithoutCitationsWithInfoRange();
    }

    @Atomic
    private void printNumberOfCitationsWithInfoRanges() {
        this.logger.debug(
                "Number of Citations with Info Ranges: " + VirtualModule.getInstance().getNumberOfCitationsWithInfoRanges());
    }

    @Atomic(mode = TxMode.WRITE)
    private void resetLastTwitterIds() {
        VirtualModule.getInstance().getLastTwitterID().resetTwitterIDS();
    }

    private void citationDetection() throws IOException, FileNotFoundException {
        File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
        // get just files, not directories
        File[] files = folder.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);

        String[] sources = {"fp", "livro", "bernardo", "vicente"};

        for (String source : sources) {
            String lastFileName = getLastFilenameForSource(source);

            List<File> sourceFiles = Arrays.stream(files)
                    .filter(f -> f.getName().contains(source) && f.getName().compareTo(lastFileName) >= 0)
                    .collect(Collectors.toList());

            for (File fileEntry : sourceFiles) {
                fileCitationDetection(fileEntry);

            }
        }

        printLastTwitterIdsAndFiles();

    }

    @Atomic
    private String getLastFilenameForSource(String source) {
        String lastFileName = VirtualModule.getInstance().getLastTwitterID().getLastParsedFile(source) != null
                ? VirtualModule.getInstance().getLastTwitterID().getLastParsedFile(source)
                : "";
        return lastFileName;
    }

    private void fileCitationDetection(File fileEntry) throws IOException {
        this.logger.debug("JSON file name: " + fileEntry.getName());

        JSONObject obj = new JSONObject();
        String line = null;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));

        // criar um tempMaxID que guarda o valor de
        // VirtualModule.getInstance().getLastTwitterID()
        // pq é preciso darmos set na base de dados do valor antes do while, pq vem logo
        // na primeira linha
        long tempMaxID = getLastTwitterId(fileEntry);

        int lineNum = 0;
        while ((line = bufferedReader.readLine()) != null) {
            // logger.debug(line);

            try {
                obj = (JSONObject) new JSONParser().parse(line);

                if (lineNum == 0) {
                    updateLastTwitterId(fileEntry, obj);
                }
                lineNum++;

                if (obj.containsKey("isRetweet") && (boolean) obj.get("isRetweet")) {
                    continue;
                }

                if ((long) obj.get("tweetID") > tempMaxID) {
                    String tweetTextWithoutHttp = removeHttpFromTweetText(obj);

                    if (!tweetTextWithoutHttp.equals("")) {
                        searchQueryParserJSON(tweetTextWithoutHttp, obj);
                        // searchQueryParser(absoluteSearch(tweetTextWithoutHttp)); //demasiado rígida,
                        // nao funciona no nosso caso
                    }

                } else {
                    break;
                }
            } catch (ParseException |
                    org.apache.lucene.queryparser.classic.ParseException |
                    org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }
        bufferedReader.close();
    }

    private void searchQueryParserJSON(String query, JSONObject obj)
            throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        Query parsedQuery = this.queryParser.parse(QueryParser.escape(query)); // escape foi a solução porque ele
        // stressava

//		try {
//			FenixFramework.getTransactionManager().begin();
//		} catch (NotSupportedException | SystemException e1) {
//			throw new LdoDException("Fail a transaction begin");
//		}

        searchIndexAndDisplayResultsJSON(parsedQuery, obj);

//		try {
//			FenixFramework.getTransactionManager().commit();
//		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
//				| HeuristicRollbackException | SystemException e) {
//			this.logger.debug("Miss the creation of a citation due to the info it contains");
//
//		}

    }

    @Atomic(mode = TxMode.WRITE)
    private void searchIndexAndDisplayResultsJSON(Query query, JSONObject obj) {
        try {
            int hitsPerPage = 5;
            Directory directory = new NIOFSDirectory(this.docDir);
            IndexReader idxReader = DirectoryReader.open(directory);
            IndexSearcher idxSearcher = new IndexSearcher(idxReader);

            ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
            if (hits.length > 0) {
                int docId = hits[0].doc;
                float score = hits[0].score;
                if (score > this.LUCENE_THRESHOLD) {
                    Document d = idxSearcher.doc(docId);

                    // necessary because the same tweet was collected using different keywords in
                    // FetchCitationsFromTwitter class
                    // check if twitter ID already exists in the list of Citations
                    // if it does idExists=true, therefore we don't create a citation for it!
//                    Set<TwitterCitation> allTwitterCitations = VirtualModule.getInstance().getCitationSet().stream()
//                            .filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
//                            .collect(Collectors.toSet());
                    Set<TwitterCitation> allTwitterCitations = VirtualRequiresInterface.getInstance().getCitationSet().stream()
                            .map(c -> (TwitterCitation) FenixFramework.getDomainObject(c.getExternalId()))
                            .collect(Collectors.toSet());
                    boolean twitterIDExists = false;
                    for (TwitterCitation tc : allTwitterCitations) {
                        if (tc.getTweetID() == (long) obj.get("tweetID")) {
                            twitterIDExists = true;
                            break;
                        }
                    }
                    if (!twitterIDExists) {
                        // obtain Fragment
                        // using external id
//                        ScholarInter inter = FenixFramework.getDomainObject(d.get(this.ID));
//                        Fragment fragment = inter.getFragment();
                        ScholarInterDto inter = VirtualRequiresInterface.getInstance().getScholarInterbyExternalId(d.get(this.ID));
                        FragmentDto fragment  = VirtualRequiresInterface.getInstance().getFragmentByXmlId(inter.getFragmentXmlId());

                        String tweetTextWithoutHttp = removeHttpFromTweetText(obj);

                        // remove emojis, etc
                        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
                        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);

                        Matcher matcher = pattern.matcher(tweetTextWithoutHttp);
                        String cleanTweetText = matcher.replaceAll("");
                        cleanTweetText = removeEmojiAndSymbolFromString(cleanTweetText);

                        matcher = pattern.matcher((String) obj.get("location"));
                        String cleanTeeetLocation = matcher.replaceAll("");
                        cleanTeeetLocation = removeEmojiAndSymbolFromString(cleanTeeetLocation);

                        matcher = pattern.matcher((String) obj.get("country"));
                        String cleanTeeetCountry = matcher.replaceAll("");
                        cleanTeeetCountry = removeEmojiAndSymbolFromString(cleanTeeetCountry);

                        new TwitterCitation(fragment, (String) obj.get("tweetURL"), (String) obj.get("date"),
                                d.get(this.TEXT), cleanTweetText, (long) obj.get("tweetID"), cleanTeeetLocation,
                                cleanTeeetCountry, (String) obj.get("username"), (String) obj.get("profURL"),
                                (String) obj.get("profImg"));
                    }

                }
            }
            idxReader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void updateLastTwitterId(File fileEntry, JSONObject obj) {
        if ((long) obj.get("tweetID") > VirtualModule.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName())) {
            VirtualModule.getInstance().getLastTwitterID().updateLastTwitterID(fileEntry.getName(), (long) obj.get("tweetID"));
        }
    }

    private String removeHttpFromTweetText(JSONObject obj) {
        String tweetText = (String) obj.get("text");
        String tweetTextWithoutHttp = tweetText;

        // removing "http" from tweet text
        if (tweetText.contains("http")) {
            int httpIndex = tweetText.indexOf("http");
            tweetTextWithoutHttp = tweetText.substring(0, httpIndex);
        }
        return tweetTextWithoutHttp;
    }

    private String removeEmojiAndSymbolFromString(String content) throws UnsupportedEncodingException {
        String utf8tweet = "";

        byte[] utf8Bytes = content.getBytes("UTF-8");
        utf8tweet = new String(utf8Bytes, "UTF-8");

        Pattern unicodeOutliers =
                Pattern.compile(
                        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE |
                                Pattern.CANON_EQ |
                                Pattern.CASE_INSENSITIVE
                );
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8tweet);

        utf8tweet = unicodeOutlierMatcher.replaceAll(" ");
        return utf8tweet;
    }

    @Atomic
    private void printLastTwitterIdsAndFiles() {
        this.logger.debug("VirtualModule Book:{}, {}", VirtualModule.getInstance().getLastTwitterID().getLastBookParsedFile(),
                VirtualModule.getInstance().getLastTwitterID().getBookLastTwitterID());
        this.logger.debug("VirtualModule Bernardo:{}, {}", VirtualModule.getInstance().getLastTwitterID().getLastBernardoParsedFile(),
                VirtualModule.getInstance().getLastTwitterID().getBernardoLastTwitterID());
        this.logger.debug("VirtualModule Vicente:{}, {}", VirtualModule.getInstance().getLastTwitterID().getLastVicenteParsedFile(),
                VirtualModule.getInstance().getLastTwitterID().getVicenteLastTwitterID());
        this.logger.debug("VirtualModule Pessoa:{}, {}", VirtualModule.getInstance().getLastTwitterID().getLastPessoaParsedFile(),
                VirtualModule.getInstance().getLastTwitterID().getPessoaLastTwitterID());
    }

//    @Atomic(mode = TxMode.WRITE)
//    private void createInfoRanges() {
//        for (Citation citation : VirtualModule.getInstance().getCitationSet()) {
//            if (citation.getInfoRangeSet().isEmpty()) {
//                Fragment citationFragment = citation.getFragment();
//                Set<ScholarInter> inters = new HashSet<>(citationFragment.getScholarInterSet());
//
//                for (ScholarInter inter : inters) {
//                    createInfoRange(inter, citation);
//                }
//            }
//        }
//    }

    @Atomic(mode = TxMode.WRITE)
    private void createInfoRanges() {
        for (CitationDto citation : VirtualRequiresInterface.getInstance().getCitationSet()) {
            if (citation.isHasNoInfoRange()) {
                FragmentDto citationFragment = VirtualRequiresInterface.getInstance().getFragmentByXmlId(citation.getFragmentXmlId());
                Set<ScholarInterDto> inters = new HashSet<>(citationFragment.getScholarInterDtoSet());

                for (ScholarInterDto inter : inters) {
                    createInfoRange(inter, citation);
                }
            }
        }
    }

    private void createInfoRange(ScholarInterDto inter, CitationDto citation) {
        String htmlTransc = getHtmlTransc(inter);

        if (citation instanceof CitationDto) {
//            List<String> result = patternFinding(htmlTransc, ((TwitterCitationDto) citation).getTweetText());
            TwitterCitation twitterCitation = new TwitterCitation(citation);
            List<String> result = patternFinding(htmlTransc, twitterCitation.getTweetText());


            String infoQuote = result.get(0);
            int htmlStart = Integer.parseInt(result.get(1));
            int htmlEnd = Integer.parseInt(result.get(2));
            int numOfPStart = Integer.parseInt(result.get(3));
            int numOfPEnd = Integer.parseInt(result.get(4));

            if (htmlStart != -1 && htmlEnd != -1 && infoQuote.trim() != ""
                    && !startBiggerThanEnd(htmlStart, htmlEnd, numOfPStart, numOfPEnd)) {

                String infoText = createInfoText(citation);

//                new InfoRange(citation, inter, "/div[1]/div[1]/p[" + numOfPStart + "]", htmlStart,
//                        "/div[1]/div[1]/p[" + numOfPEnd + "]", htmlEnd, infoQuote, infoText);

                VirtualRequiresInterface.getInstance().createInfoRange(citation.getId(), inter.getXmlId(), "/div[1]/div[1]/p[" + numOfPStart + "]", htmlStart,
                        "/div[1]/div[1]/p[" + numOfPEnd + "]", htmlEnd, infoQuote, infoText);
            }
        }
    }

    private String createInfoText(CitationDto citation) {

        TwitterCitation twitterCitation = new TwitterCitation(citation);

        // concatenation of meta information
        String sourceLink = citation.getSourceLink();
        String date = citation.getDate();
        String tweetID = Long.toString(citation.getId());
        String location = twitterCitation.getLocation();
        String country = twitterCitation.getCountry();
        String username = twitterCitation.getUsername();
        String userProfileURL = twitterCitation.getUserProfileURL();

        String infoText;

        // complete info text
        // infoText = "SOURCE LINK: " + sourceLink + "\n" + "DATE: " + date + "\n" +
        // "TWEET ID: " + tweetID + "\n"
        // + "COUNTRY: " + country + "\n" + "LOCATION: " + location + "\n" + "USERNAME:"
        // + username + "\n"
        // + "USER PROFILE: " + userProfileURL;

        // short info text
        infoText = "LINK: " + sourceLink + "\n" + "DATA: " + date + "\n";
        if (!country.equals("unknown")) {
            infoText += "PAÍS: " + country;
        }

        return infoText;
    }

    private String getHtmlTransc(ScholarInterDto inter) {
       return VirtualRequiresInterface.getInstance().getWriteFromPlainHtmlWriter4OneInter(inter.getXmlId(), false);
    }

    public boolean startBiggerThanEnd(int htmlStart, int htmlEnd, int numOfPStart, int numOfPEnd) {
        return htmlStart > htmlEnd && numOfPStart == numOfPEnd;
    }

    public String convertFirstCharToUpperCaseInSentence(String str) {
        // Create a char array of given String
        char[] ch = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z') {
                // Convert into Lower-Case
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }

    public String cleanTweetText(String originalTweetText) {
        String result = originalTweetText.toLowerCase().replaceAll("[\"*«»“”()';]", "");
        result = result.replace("\\n", " ");
        result = result.replace("...", " ");

        // não posso por em variáveis pq o tamanho do texto
        // vai sendo encurtado no ciclo for
        // int resultLen = result.length();
        // int lastCharPos = resultLen - 1;

        // apagar apenas os hífenes e pontos que não fizerem parte de palavras
        String charSet = "-.,;?!q"; // 'q' porque muitas pessoas escrevem 'q' em vez de "que"
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (charSet.indexOf(c) != -1) {
                result = cleanCharFromString(c, result, i, result.length() - 1);
            }
        }
        return result;
    }

    private String capitalizeFirstWord(String sentence) {
        return sentence.substring(0, 1).toUpperCase() + sentence.substring(1);
    }

    public List<String> patternFinding(String text, String tweet) {
//		logger.debug("------------------------------ PATTERN FINDING ALGORITHM-------------------------");
//		logger.debug("ORIGINAL TWEET TEXT: " + tweet);

        // é chato pôr o text é lowercase pq estamos a adulterar a informação original,
        // experimentar outra distance em vez do Jaro
        text = text.toLowerCase();
        // o "clean" já mete o tweet em lowerCase
        tweet = cleanTweetText(tweet);

//		this.logger.debug("CLEANED TWEET TEXT: " + tweet);

        // variables updated over iteration
        int start = -1; // -1 means that the pattern was not found, either for start and end
        int end = -1;
        int offset = 0;
        String patternFound = "";

        // parameters that can be adjusted
        int window = this.WINDOW_THRESHOLD;
        double jaroThreshold = 0.9;
        int startCorrectParam = 3; // parâmetro utilizado na correção da start position

        // algorithm
        int count = 0; // aux counter to check if we reach the minimum value set by "window" variable
        outerloop:
        for (String initialWord : tweet.split("\\s+")) {
            for (String word : initialWord.split(",")) {
                offset = Math.max(start, end);
                if (offset == -1) {
                    offset = 0;
                }

                List<String> info = maxJaroValue(text.substring(offset), word);
                String wordFound = info.get(0);
                double jaroValue = Double.parseDouble(info.get(1));

                // logger.debug("tweet word: " + word);
                // logger.debug("text word: " + wordFound);

                // a palavra tem de existir no texto e estar à frente do offset!
                // primeira palavra encontrada
                if (jaroValue > jaroThreshold && text.indexOf(wordFound, offset) != -1) {
                    // logger.debug(" text contains this word");
                    // logger.debug(Double.toString(jaroValue));

                    // é só updated uma vez e é quando o início começa bem
                    if (count == 0) {
                        // é só updated uma vez e é quando o início começa bem
                        start = text.indexOf(wordFound, offset);
                        patternFound += word + " ";
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
                            // logger.debug(" padrão até agora: " + patternFound);

                            // este update ao start dá bug quando as palavras iniciais do padrão aparecem
                            // antes do padrão
                            String[] splits = patternFound.split(" ");
                            String firstWordOfPatternFound = splits[0];
                            String lastWordOfPatternFound = splits[splits.length - 1];

                            start = text.lastIndexOf(firstWordOfPatternFound, offset - lastWordOfPatternFound.length());
                        }
                        end = text.indexOf(wordFound, offset) + wordFound.length();
                        // logger.debug(" a palavra encontrada no Texto foi: " + wordFound);
                        patternFound += word + " ";
                        count++;
                    }
                }
                // caso em q a palavra não existe no texto
                else {
                    // logger.debug(" text DOES NOT contains this word");
                    // logger.debug(Double.toString(jaroValue));
                    if (count < window) { // significa que não fizémos o número mínimo de palavras seguidas, logo é dar
                        // reset!!
                        count = 0;
                        start = -1;
                        end = -1;
                        patternFound = "";
                        // logger.debug(" dei reset ao count, next word!");
                    } else {
                        // logger.debug(" vou dar break pq já garanti a window");
                        break outerloop;
                    }
                }
                // logger.debug(" count: " + count);
            }
        }

        // this is the case where the pattern exists (start !=-1) but the window was not
        // fulfilled
        if (count < window && start != -1) {
            start = -1;
            end = -1;
            patternFound = "";
        }

        int numOfPStart = -1;
        int numOfPEnd = -1;
        int htmlStart = -1;
        int htmlEnd = -1;

        int earlyStart = -1;
        int laterEnd = -1;

        String prefix = "";
        String suffix = "";

        if (start != -1 && end != -1) {
            // HTML treatment
            numOfPStart = 1 + countOccurencesOfSubstring(text, "<p", start); // +1 porque o getTranscription não traz o
            // primeiro <p
            numOfPEnd = 1 + countOccurencesOfSubstring(text, "<p", end); // +1 porque o getTranscription não traz o
            // primeiro <p

            htmlStart = start - text.lastIndexOf("\">", start) - 2; // -2, para compensar
            htmlEnd = end - text.lastIndexOf("\">", end) - 2; // -2, para compensar

            // logger.debug("htmlStart: " + htmlStart);
            // logger.debug("htmlEnd: " + htmlEnd);

            // logger.debug("index of >: " + text.lastIndexOf("\">", start));
            // logger.debug("index of <: " + text.indexOf("<", end));
            //
            // logger.debug("\n");
            //
            // logger.debug("start: " + start);
            // logger.debug("end: " + end);
            //
            // logger.debug("\n");
            //
            // logger.debug("last dot: " + text.lastIndexOf(".", start));
            // logger.debug("next dot: " + text.indexOf(".", end));
            //
            // logger.debug("\n");

            // dots solution
            earlyStart = htmlStart;
            laterEnd = htmlEnd;

            if (text.lastIndexOf(".", start) > text.lastIndexOf("\">", start)) {
                // para cobrir a frase até ao ponto final anterior é fazer
                earlyStart = text.lastIndexOf(".", start) - text.lastIndexOf("\">", start) - 2;
                // logger.debug("earlyStart: " + earlyStart);
                // prefix = text.substring(text.lastIndexOf(".", start) + 1, start);
                // logger.debug("prefix: " + prefix);
            }

            if (text.indexOf(".", end) < text.indexOf("<", end)) {
                // para cobrir a frase até ao ponto final seguinte é fazer
                laterEnd = text.indexOf(".", end) - text.lastIndexOf("\">", start) - 2;
                // logger.debug("laterEnd: " + laterEnd);
                // suffix = text.substring(end, text.indexOf(".", end));
                // logger.debug("suffix: " + suffix);
            }

        }

        // logger.debug("earlyStart: " + earlyStart);
        // logger.debug("laterEnd: " + laterEnd);
        //
        // logger.debug("original pattern found: " + patternFound);

        // patternFound = prefix + patternFound + suffix;
        //
        // logger.debug("modified pattern found: " + patternFound);

        patternFound = patternFound.trim();

        // converts the first letter of each sentence to upper case
        String upperPattern = "";
        if (patternFound != "") {
            String[] patternSplit = patternFound.split("\\.\\s+");
            // logger.debug("length do split: " + patternSplit.length);
            for (String s : patternSplit) {
                // logger.debug("string s: " + s);
                upperPattern += this.capitalizeFirstWord(s) + ". ";
            }

        }

        if (upperPattern != "") {
            upperPattern = upperPattern.substring(0, upperPattern.length() - 2);
        }
        // logger.debug("UPPER PATTERN: " + upperPattern);

        List<String> result = new ArrayList<>();
        result.add(upperPattern);
        result.add(String.valueOf(earlyStart));
        result.add(String.valueOf(laterEnd));
        result.add(String.valueOf(numOfPStart));
        result.add(String.valueOf(numOfPEnd));

        return result;
    }

    public int lastIndexOfCapitalLetter(String str, int auxPos) {
        for (int i = auxPos; i >= 0; i--) {
            if (Character.isUpperCase(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Atomic
    private long getLastTwitterId(File fileEntry) {
        long tempMaxID = VirtualModule.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName());
        return tempMaxID;
    }

    // returns max jaro value between a word in the pattern and every word in the
    // text
    public List<String> maxJaroValue(String text, String wordToFind) {
        JaroWinklerDistance jaro = new JaroWinklerDistance();
        double maxJaroValue = 0.0;
        String wordFound = "";
        for (String textWord : text.split("\\s+")) {
            if (textWord.contains("</p>")) {
                textWord = textWord.substring(0, textWord.indexOf("</p>"));
            }
            if (jaro.apply(textWord, wordToFind) > maxJaroValue) {
                maxJaroValue = jaro.apply(textWord, wordToFind);
                wordFound = textWord;
            }
        }

        List<String> info = new ArrayList<>();
        info.add(wordFound);
        info.add(String.valueOf(maxJaroValue));
        return info;
    }

    private String cleanCharFromString(char charToClean, String s, int position, int lastCharPos) {
        // limpar hífenes que tenham espaços em branco à esquerda ou à direita
        if (charToClean == '-') {
            s = replaceChar(s, position, lastCharPos);
        }
        // limpar pontos que tenham espaços em branco à esquerda e à direita
        else if (charToClean == '.') {
            s = replaceDotChar(s, position, lastCharPos);
        }
        // limpar pontos que tenham ponto é vírgula em branco à esquerda e à direita
        else if (charToClean == ';') {
            s = replaceChar(s, position, lastCharPos);
        }
        // limpar vírgulas que tenham espaços em branco à esquerda e à direita
        else if (charToClean == ',') {
            s = replaceChar(s, position, lastCharPos);
        }
        // limpar pontos de interrogação que tenham espaços em branco à esquerda e à
        // direita
        else if (charToClean == '?') {
            s = replaceChar(s, position, lastCharPos);
        }
        // limpar pontos de exclamação que tenham espaços em branco à esquerda e à
        // direita
        else if (charToClean == '!') {
            s = replaceChar(s, position, lastCharPos);
        }
        // substituir as ocorrências da letra 'q' com espaços à esquerda e à direita por
        // "que"
        else if (charToClean == 'q') {
            if (position != 0) {
                if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
                    s = s.substring(0, position) + "que" + s.substring(position + 1);
                }
            }
        }
        return s;
    }

    private String replaceChar(String s, int position, int lastCharPos) {
        if (position != 0) {
            if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
                s = s.substring(0, position) + ' ' + s.substring(position + 1);
            }
        }
        return s;
    }

    // caso específico do ponto final
    private String replaceDotChar(String s, int position, int lastCharPos) {
        if (position != 0) {
            if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
                s = s.substring(0, position) + ' ' + s.substring(position + 1);
            }
        }
        // caso em q o . vem mesmo no início da frase
        else if (position == 0) {
//			this.logger.debug("ENTREI NO IF EM QUE O . VEM NA POSITION 0");
            if (s.charAt(position + 1) == ' ') {
                s = s.substring(position + 1);
            }
        }
        return s;
    }

    private void searchQueryParser(String query)
            throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
        Query parsedQuery = this.queryParser.parse(QueryParser.escape(query)); // escape foi a solução porque ele
        // stressava
        // com o EOF
        searchIndexAndDisplayResults(parsedQuery);
    }

    private void searchIndexAndDisplayResults(Query query) {
        try {
            int hitsPerPage = 5;
            Directory directory = new NIOFSDirectory(this.docDir);
            IndexReader idxReader = DirectoryReader.open(directory);
            IndexSearcher idxSearcher = new IndexSearcher(idxReader);

            ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
            if (hits.length > 0) {
                int docId = hits[0].doc;
                float score = hits[0].score;
                if (score > 30) {
                    Document d = idxSearcher.doc(docId);

                }
            }
            directory.close();
            idxReader.close();
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
            clauses[i] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term(this.TEXT, split[i])));
        }
        SpanNearQuery query = new SpanNearQuery(clauses, 0, true);

        searchIndexAndDisplayResults(query);
    }

    // Fuzzy SearchProcessor - Pesquisa tem de ser exata
    // SearchProcessor for fragments with a set of words similar to input
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

    // SearchProcessor for fragments with a set of equal to inputs
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

    public int countOccurencesOfSubstring(String string, String substring, int subsStartPos) {
        int count = 0;
        int idx = 0;

        while ((idx = string.indexOf(substring, idx)) != -1 && idx < subsStartPos) {
            idx++;
            count++;
        }

        return count;
    }
}
