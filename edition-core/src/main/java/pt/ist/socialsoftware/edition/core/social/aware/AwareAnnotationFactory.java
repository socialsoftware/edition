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
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.core.utils.RangeJson;

public class AwareAnnotationFactory {
	private static Logger logger = LoggerFactory.getLogger(FetchCitationsFromTwitter.class);
	
	public Set<TwitterCitation> getTwitterCitationsByInter(VirtualEditionInter inter){
		Set<TwitterCitation> twitterCitations = new HashSet<TwitterCitation>();
		Set<AwareAnnotation> awareAnnotations = inter.getAnnotationSet()
				.stream().filter(AwareAnnotation.class::isInstance).map(AwareAnnotation.class::cast)
				.collect(Collectors.toSet());
		
		for(AwareAnnotation aa: awareAnnotations) {
			twitterCitations.add((TwitterCitation)aa.getCitation());
		}
		
		return twitterCitations;
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
	
	//a Copy tem de estar dentro daquele for porque ele é modificado sempre q chama o removeAll ...
	//experimentar biblioteca Guava do Google, não modifica os sets
	@Atomic
	public void create() throws IOException {
		LdoD ldoD = LdoD.getInstance();
		Set<TwitterCitation> allTwitterCitations = ldoD.getCitationsSet()
				.stream().filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
				.collect(Collectors.toSet());
		for(VirtualEdition ve: ldoD.getVirtualEditionsSet()) {
			if(ve.isSAVE()) {
				//perguntar ao stor se é este o método correto de obter os Inters a partir de uma VE
				for(VirtualEditionInter inter: ve.getAllDepthVirtualEditionInters()) {
					Set<TwitterCitation> allTwitterCitationsCopy = allTwitterCitations;
					Set<TwitterCitation> twitterCitations = getTwitterCitationsByInter(inter);
					allTwitterCitationsCopy.removeAll(twitterCitations);
					for(TwitterCitation newCitation: allTwitterCitationsCopy) {
						//String quote = ""; //string matching algorithm
						//String text = ""; //meta information inside citation object
						//new AwareAnnotation(inter, quote, text);
					}
					
				}
			}
		}
		
		logger.debug("END OF FACTORY"); 
		
		
		/************************************** DEBUG DE IDS *************************************************/
		/*
		BufferedWriter bw = null;
		FileWriter fw = null;
		File file;
		file = new File("C:/Users/dnf_o/projetoTese/ldod/social/awareIDs/ids.txt");
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
}
