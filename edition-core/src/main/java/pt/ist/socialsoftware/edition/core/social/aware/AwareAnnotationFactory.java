package pt.ist.socialsoftware.edition.core.social.aware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.core.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.core.domain.Citation;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.domain.Range;
import pt.ist.socialsoftware.edition.core.domain.SimpleText;
import pt.ist.socialsoftware.edition.core.domain.SocialMediaCriteria;
import pt.ist.socialsoftware.edition.core.domain.TimeWindow;
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.core.utils.RangeJson;

public class AwareAnnotationFactory {
	private static Logger logger = LoggerFactory.getLogger(FetchCitationsFromTwitter.class);
	
	public static void logger(Object toPrint) {
    	System.out.println(toPrint);
    }

	//primeiro criar anotação
	//depois criar range e depois passar anotação ao construtor
	//o catch é q o nosso range não vem da interação do user com a app, 
	//mas sim dos valores retornados de um algoritmo de string matching
	
	/*
	@Atomic(mode = TxMode.WRITE)
	public HumanAnnotation createAnnotation(String quote, String text, LdoDUser user, List<RangeJson> rangeList,
			List<String> tagList) {
		logger.debug("createAnnotation start:{}, startOffset:{}, end:{}, endOffset:{}", rangeList.get(0).getStart(),
				rangeList.get(0).getStartOffset(), rangeList.get(0).getEnd(), rangeList.get(0).getEndOffset());

		SimpleText startText = null;
		// startText =
		// getFragment().getTextPortion().getSimpleText(getLastUsed(), 0,
		// rangeList.get(0).getStartOffset());
		SimpleText endText = null;
		// endText = getFragment().getTextPortion().getSimpleText(getLastUsed(),
		// 0, rangeList.get(0).getEndOffset());

		HumanAnnotation annotation = new HumanAnnotation(this, startText, endText, quote, text, user);

		for (RangeJson rangeJson : rangeList) {
			new Range(annotation, rangeJson.getStart(), rangeJson.getStartOffset(), rangeJson.getEnd(),
					rangeJson.getEndOffset());
		}

		for (String tag : tagList) {
			createTag(annotation.getUser(), tag, annotation);
		}

		return annotation;
	}
	*/
	
	//local inner class inside the method
    public class JaroInfo {
        String wordFound;
        double jaroValue;
        
        public JaroInfo(String wordFound, double jaroValue) {
        	this.wordFound = wordFound;
        	this.jaroValue = jaroValue;
        }
    }
	
	//returns max jaro value between a word in the pattern and every word in the text - not so efficient
    public static JaroInfo maxJaroValue(String text, String wordToFind) {
		JaroWinklerDistance jaro = new JaroWinklerDistance();
    	double maxJaroValue = 0.0;
    	String wordFound = "";
    	for(String textWord: text.split("\\s+")) {
    		if(jaro.apply(textWord, wordToFind) > maxJaroValue) {
    			maxJaroValue = jaro.apply(textWord, wordToFind);
    			wordFound = textWord;
    		}
    	}
    	
    	JaroInfo info = new AwareAnnotationFactory().new JaroInfo(wordFound, maxJaroValue);
    	return info;
    }
    
    public static String cleanTweetText(String originalTweetText) {
    	//regex
    	String result = originalTweetText.toLowerCase().replaceAll("[\"*\\n;«»“”()]", "");
    	
    	//apagar apenas os hífenes e pontos que não fizerem parte de palavras
    	int resultLen = result.length();
    	int lastCharPos = resultLen-1;
    	String charSet= "-.,?";
    	for(int i = 0; i < resultLen; i++) {
    		char c = result.charAt(i);
    		//logger(result.charAt(i));
    		if(charSet.indexOf(c)!=-1){
    			//logger("entrei no primeiro if do CLEAN");
    			result = cleanCharFromString(c, result, i, lastCharPos);
    		}
    	}
    	return result;
    }
	
    public static String cleanCharFromString(char charToClean, String s, int position, int lastCharPos) {
    	//i!=lastCharPos serve para prevenior um IndexOutOfBound
    	logger("string s : " + s);
		logger("position : " + position);
		logger("lastCharPos : " + lastCharPos);
    	
    	//limpar hífenes que tenham espaços em branco à esquerda ou à direita
    	if(charToClean == '-') {
			logger("entrei no if do hífen");
			if(position!=0) {
				if(s.charAt(position-1) == ' ' || (position!=lastCharPos && s.charAt(position+1) == ' ')) {
					s = s.substring(0,position)+' '+s.substring(position+1);
				}
			}	
		}
		//limpar pontos que tenham espaços em branco à esquerda e à direita
		else if(charToClean == '.') {
			logger("entrei no if do ponto");
			if(position!=0) {
				if(s.charAt(position-1) == ' ' && (position!=lastCharPos && s.charAt(position+1) == ' ')) {
					s = s.substring(0,position)+' '+s.substring(position+1);
				}
			}	
		}
    	//limpar vírgulas que tenham espaços em branco à esquerda e à direita
		else if(charToClean == ',') {
			logger("entrei no if da vírgula");
			if(s.charAt(position-1) == ' ' && (position!=lastCharPos && s.charAt(position+1) == ' ')) {
				s = s.substring(0,position)+' '+s.substring(position+1);
			}
		}
    	//limpar pontos de interrogação que tenham espaços em branco à esquerda e à direita
		else if(charToClean == '?') {
			logger("entrei no if do ponto de interrogação");
			if(position!=0) {
				if(s.charAt(position-1) == ' ' && (position!=lastCharPos && s.charAt(position+1) == ' ')) {
					s = s.substring(0,position)+' '+s.substring(position+1);
				}
			}	
		}
		return s;
    }
    
	
	@Atomic
	public void create() throws IOException {
		logger.debug("BEGINNIG OF FACTORY"); 
		
		//algoritmo do Annotation Factory
		//o removeAll modifica o conjunto
		//experimentar biblioteca Guava do Google, não modifica os sets
		/*
		LdoD ldoD = LdoD.getInstance();
		//allTwitterCitations - all twitter citations in the archive
		Set<TwitterCitation> allTwitterCitations = ldoD.getCitationsSet()
				.stream().filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
				.collect(Collectors.toSet());
		for(VirtualEdition ve: ldoD.getVirtualEditionsSet()) {
			if(ve.isSAVE()) {
				//perguntar ao stor se é este o método correto de obter os Inters a partir de uma VE
				for(VirtualEditionInter inter: ve.getAllDepthVirtualEditionInters()) {
					//currentTwitterCitations - current twitter citations from a certain frag/fragInter
					//totalTwitterCitations - total twitter citations from a certain frag/fragInter
					Set<TwitterCitation> currentTwitterCitations = getCurrentTwitterCitationsByInter(inter);
					Set<TwitterCitation> totalTwitterCitations = getTotalTwitterCitationsByInter(allTwitterCitations, inter);
					totalTwitterCitations.removeAll(currentTwitterCitations);
					for(TwitterCitation newCitation: totalTwitterCitations) {
						//String quote = ""; //string matching algorithm
						//String text = ""; //meta information inside citation object
						//new AwareAnnotation(inter, quote, text);
					}
					
				}
			}
		}
		
		VirtualEdition duarteEdit = LdoD.getInstance().getVirtualEdition("LdoD-EditDuarte");
		duarteEdit.addCriteria(new TimeWindow());
		System.out.println(duarteEdit.isSAVE());
		*/
		
		LdoD ldoD = LdoD.getInstance();
		logger("------------------------------------");
		int window = 10;
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		File file;
		file = new File("C:/Users/dnf_o/projetoTese/ldod/social/citationsPositions/positions.txt");
		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);
		
		Set<TwitterCitation> allTwitterCitations = ldoD.getAllTwitterCitation();
		
		for (TwitterCitation c : allTwitterCitations) {
			/*
			System.out.println("+++++++++++ CITATIONS ++++++++++++++++");
			System.out.println(c.getSourceLink());
			System.out.println(c.getDate());
			System.out.println(c.getFragText());
			System.out.println(c.getExternalId());
			*/
			
			//será preciso por estes textos em lowerCase??
			String text = c.getFragText();
			String tweet = c.getTweetText();
			
			//é chato pôr o text é lowercase pq estamos a adulterar a informação original, experimentar outra distance em vez do Jaro
			text = text.toLowerCase();
			//o "clean" já mete o tweet em lowerCase
			tweet = cleanTweetText(tweet);
			
			
			//variables updated over iteration
			int start = -1;
			int end = -1;
			int offset = 0;
			String patternFound = "";
			double jaroThreshold = 0.799;

			
			//algorithm
			int count = 0; //aux counter to check if we reach the minimum value set by "window" variable
			outerloop:
			for (String initialWord : tweet.split("\\s+")) {
				for(String word: initialWord.split(",")) {
					logger("--------------------------------------------------");
					offset = Math.max(start, end);
					if(offset==-1) {
						offset=0;
					}
					logger("offset: " + offset);
					
					JaroInfo info = maxJaroValue(text.substring(offset), word);
					logger("tweet word: " + word);
					logger("text word: " + info.wordFound);
	
					//a palavra tem de existir no texto e estar à frente do offset!
					//primeira palavra encontrada
					if(info.jaroValue > jaroThreshold && text.indexOf(info.wordFound, offset)!=-1) { 
						logger("	text contains this word");
						logger(text.indexOf(info.wordFound, offset));
						logger(info.jaroValue);
						//é só updated uma vez e é quando o início começa bem
						if(count==0) {
							//é só updated uma vez e é quando o início começa bem
							start = text.indexOf(info.wordFound, offset);
							logger("	dei update do start para: " + start);
							logger("	a palavra encontrada no Texto foi: " + info.wordFound);
							patternFound+= info.wordFound + " ";
							count=1;
						}
						//restantes palavras encontradas
						//vai sendo constantemente updated enquanto corre bem
						else {
							//entra neste if para dar o update exato do start
							//pq a primeira palavra do padrão pode ocorrer várias vezes no texto antes de ocorrer no padrão
							//o mais correto seria fazer quando count==1 (pq é quando já recolhemos pelo menos uma palavra)
							//mas como o offset só é updated no início de cada ciclo temos de esperar uma iteração
							if(count==2) {
								//este update ao start dá bug quando as palavras iniciais do padrão aparecem antes do padrão
								logger("	padrão até agora: " + patternFound);
								
								String[] splits = patternFound.split(" ");
								String firstWordOfPatternFound = splits[0];
								logger("	primeira palavra: " + firstWordOfPatternFound);
								String lastWordOfPatternFound = splits[splits.length-1];
								logger("	última palavra: " + lastWordOfPatternFound);
								
								start = text.lastIndexOf(firstWordOfPatternFound, offset-lastWordOfPatternFound.length());
								logger("	dei update do start para: " + start);
							}
							end = text.indexOf(info.wordFound, offset) + info.wordFound.length();
							logger("	dei update do end para: " + end);
							logger("	a palavra encontrada no Texto foi: " + info.wordFound);
							patternFound+= info.wordFound + " ";
							count++;
						}
					}
					//caso em q a palavra não existe no texto
					else {
						logger("	text DOES NOT contains this word");
						logger(info.jaroValue);
						if(count<window) { //significa que não fizémos o número mínimo de palavras seguidas, logo é dar reset!!
							count=0;
							start=-1;
							end=-1;
							patternFound = "";
							logger("	dei reset ao count, next word!");
						}
						else {
							logger("	vou dar break pq já garanti a window");
							break outerloop;
						}
					}
					logger("	count: " + count);
				}//for interno
			}//for externo
			
			bw.write("***************** CITATION **************************");
			bw.write("\n");
			bw.write("Date: " + c.getDate());
			bw.write("\n");
			bw.write("External ID: " + c.getExternalId());
			bw.write("\n");
			bw.write("Tweet ID: " + c.getTweetID());
			bw.write("\n");
			
			bw.write("Frag text: " + c.getFragText());
			bw.write("\n");
			
			bw.write("Tweet text: " + c.getTweetText());
			bw.write("\n");
			
			bw.write("Start index: " + start);
			bw.write("\n");
			bw.write("End index: " + end);
			bw.write("\n");
			bw.write("Pattern found: " + patternFound);
			bw.write("\n");
			bw.write("Pattern clean: " + tweet);
			bw.write("\n");
			
			if(patternFound.equals("")) {
				bw.write("	Pattern does not exist!");
				bw.write("\n");			
			}
			
		}
	
		bw.close();
		fw.close();
		
		logger.debug("END OF FACTORY"); 
	}
	
	public Set<TwitterCitation> getTotalTwitterCitationsByInter(Set<TwitterCitation> allTwitterCitations, VirtualEditionInter inter){
		Set<TwitterCitation> totalTwitterCitations = new HashSet<TwitterCitation>();
		
		for(TwitterCitation tc: allTwitterCitations) {
			if(tc.getFragment().getFragmentInterSet().contains(inter))
				totalTwitterCitations.add(tc);
		}
		
		return totalTwitterCitations;
	}
	
	
	public Set<TwitterCitation> getCurrentTwitterCitationsByInter(VirtualEditionInter inter){
		Set<TwitterCitation> twitterCitations = new HashSet<TwitterCitation>();
		Set<AwareAnnotation> awareAnnotations = inter.getAnnotationSet()
				.stream().filter(AwareAnnotation.class::isInstance).map(AwareAnnotation.class::cast)
				.collect(Collectors.toSet());
		
		for(AwareAnnotation aa: awareAnnotations) {
			twitterCitations.add((TwitterCitation)aa.getCitation());
		}
		
		return twitterCitations;
	}
	
	

	/************************************** DEBUG DE IDS *************************************************/
	/*
	BufferedWriter bw = null;
	FileWriter fw = null;
	File file;
	file = new File("C:/Users/dnf_o/projetoTese/ldod/social/citationsPositions/positions.txt");
	fw = new FileWriter(file);
	bw = new BufferedWriter(fw);
	
	Set<TwitterCitation> allTwitterCitations = LdoD.getInstance().getCitationsSet()
			.stream().filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
			.collect(Collectors.toSet());
	
	for (TwitterCitation c : allTwitterCitations) {
		System.out.println("+++++++++++ CITATIONS ++++++++++++++++");
		System.out.println(c.getSourceLink());
		System.out.println(c.getDate());
		System.out.println(c.getFragText());
		System.out.println(c.getExternalId());
		
		bw.write("***************** CITATIONS **************************");
		bw.write("\n");
		bw.write("Date: " + c.getDate());
		bw.write("\n");
		bw.write("External ID: " + c.getExternalId());
		bw.write("\n");
		bw.write("Tweet ID: " + c.getTweetID());
		bw.write("\n");
	}
	
	
	for (Fragment frag : LdoD.getInstance().getFragmentsSet()) {
		System.out.println("++++++++++++ FRAGMENTS +++++++++++++++");
		System.out.println("Title: " + frag.getTitle());
		System.out.println("External ID: " + frag.getExternalId());
		
		bw.write("***************** FRAGMENTS **************************");
		bw.write("\n");
		bw.write("Title: " + frag.getTitle());
		bw.write("\n");
		bw.write("External ID: " + frag.getExternalId());
		bw.write("\n");
		bw.write("XML ID: " + frag.getXmlId());
		bw.write("\n");
	}
	
	bw.close();
	fw.close();
	*/
}
