package pt.ist.socialsoftware.edition.core.export;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.core.domain.Annotation;
import pt.ist.socialsoftware.edition.core.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.core.domain.Category;
import pt.ist.socialsoftware.edition.core.domain.Citation;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.Range;
import pt.ist.socialsoftware.edition.core.domain.Tag;
import pt.ist.socialsoftware.edition.core.domain.Tweet;
import pt.ist.socialsoftware.edition.core.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.core.domain.VirtualEditionInter;

public class VirtualEditionFragmentsTEIExport {
	Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

	Document jdomDoc = null;

	@Atomic
	public void export() {
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			exportFragment(fragment);
		}
	}

	@Atomic
	public String exportFragment(Fragment fragment) {
		this.jdomDoc = new Document();
		Element rootElement = new Element("teiCorpus");
		rootElement.setNamespace(this.xmlns);
		this.jdomDoc.setRootElement(rootElement);
		Element tei = new Element("TEI", this.xmlns);
		Attribute id = new Attribute("id", fragment.getXmlId(), Namespace.XML_NAMESPACE);
		tei.setAttribute(id);
		rootElement.addContent(tei);
		Element teiHeader = new Element("teiHeader", this.xmlns);
		teiHeader.setAttribute("type", "text");
		tei.addContent(teiHeader);
		Element fileDesc = new Element("fileDesc", this.xmlns);
		teiHeader.addContent(fileDesc);
		Element sourceDesc = new Element("sourceDesc", this.xmlns);
		fileDesc.addContent(sourceDesc);

		Element witnesses = new Element("listWit", this.xmlns);
		id = new Attribute("id", fragment.getXmlId() + ".WIT.ED.VIRT", Namespace.XML_NAMESPACE);
		witnesses.setAttribute(id);
		sourceDesc.addContent(witnesses);
		for (VirtualEditionInter virtualEditionInter : fragment.getVirtualEditionInters()) {
			exportVirtualEditionInterWitness(witnesses, virtualEditionInter);
		}

		Element profileDesc = new Element("profileDesc", this.xmlns);
		teiHeader.addContent(profileDesc);
		for (VirtualEditionInter virtualEditionInter : fragment.getVirtualEditionInters()) {
			Element textClass = new Element("textClass", this.xmlns);
			textClass.setAttribute("source", "#" + virtualEditionInter.getXmlId());
			profileDesc.addContent(textClass);

			exportVirtualEditionInterTags(textClass, virtualEditionInter);
			exportVirtualEditionInterAnnotations(textClass, virtualEditionInter);
		}

		// TODO: export fragment citations
		exportFragmentCitations(teiHeader, fragment);

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		// System.out.println(xml.outputString(rootElement));

		return xml.outputString(rootElement);
	}

	private void exportVirtualEditionInterWitness(Element witnesses, VirtualEditionInter virtualEditionInter) {
		Element witness = new Element("witness", this.xmlns);
		Attribute id = new Attribute("id", virtualEditionInter.getXmlId(), Namespace.XML_NAMESPACE);
		witness.setAttribute(id);
		witness.setAttribute("source", "#" + virtualEditionInter.getUses().getXmlId());
		witnesses.addContent(witness);

		Element number = new Element("num", this.xmlns);
		number.setAttribute("value", Integer.toString(virtualEditionInter.getNumber()));
		witness.addContent(number);
	}

	private void exportVirtualEditionInterTags(Element textClass, VirtualEditionInter virtualEditionInter) {
		for (Tag tag : virtualEditionInter.getTagSet()) {
			if (tag.getAnnotation() == null) {
				Element catRef = new Element("catRef", this.xmlns);
				catRef.setAttribute("scheme", "#" + virtualEditionInter.getEdition().getXmlId());
				catRef.setAttribute("target", "#" + tag.getCategory().getName());
				catRef.setAttribute("resp", "#" + tag.getContributor().getUsername());
				textClass.addContent(catRef);
			}
		}
	}

	// TODO suggestion: faz sentido ???
	private void exportAwareAnnotationCitation(Element note, AwareAnnotation annotation) {
		exportCitation(note, annotation.getCitation());
		if (annotation.getCitation() instanceof TwitterCitation) {
			exportTwitterCitation(note, (TwitterCitation) annotation.getCitation());
		}
	}

	private void exportFragmentCitations(Element teiHeader, Fragment fragment) {
		Element citationList = new Element("citationList", this.xmlns);
		teiHeader.addContent(citationList);
		for (Citation citation : fragment.getCitationSet()) {
			exportCitation(citationList, citation);
			if (citation instanceof TwitterCitation) {
				exportTwitterCitation(citationList, (TwitterCitation) citation);
			}
		}
	}

	protected void exportCitation(Element citationList, Citation citation) {
		Element citationElement = new Element("citation", this.xmlns);
		citationList.addContent(citationElement);
		Element sourceLink = new Element("sourceLink", this.xmlns);
		sourceLink.addContent(citation.getSourceLink());
		citationElement.addContent(sourceLink);
		Element date = new Element("date", this.xmlns);
		date.addContent(citation.getDate());
		citationElement.addContent(date);
		Element fragText = new Element("fragText", this.xmlns);
		fragText.addContent(citation.getFragText());
		citationElement.addContent(fragText);
	}

	protected void exportTwitterCitation(Element citationList, TwitterCitation citation) {
		Element citationElement = new Element("citation", this.xmlns);
		citationElement.setAttribute("type", "twitter");
		citationList.addContent(citationElement);

		Element tweetText = new Element("tweetText", this.xmlns);
		tweetText.addContent(citation.getSourceLink());
		citationElement.addContent(tweetText);
		Element tweetID = new Element("tweetID", this.xmlns);
		tweetID.addContent(Long.toString(citation.getTweetID()));
		citationElement.addContent(tweetID);
		Element location = new Element("location", this.xmlns);
		location.addContent(citation.getLocation());
		citationElement.addContent(location);
		Element country = new Element("country", this.xmlns);
		country.addContent(citation.getCountry());
		citationElement.addContent(country);
		Element username = new Element("username", this.xmlns);
		username.addContent(citation.getUsername());
		citationElement.addContent(username);
		Element userProfileURL = new Element("userProfileURL", this.xmlns);
		userProfileURL.addContent(citation.getUserProfileURL());
		citationElement.addContent(userProfileURL);
		Element userImageURL = new Element("userImageURL", this.xmlns);
		userImageURL.addContent(citation.getUserImageURL());
		citationElement.addContent(userImageURL);

		// TODO suggestion: export Tweets here ??
		Element tweetList = new Element("tweetList", this.xmlns);
		citationElement.addContent(tweetList);
		exportTweets(tweetList, citation);
	}

	private void exportTweets(Element tweetList, TwitterCitation citation) {
		Element tweetElement = new Element("tweet", this.xmlns);
		tweetList.addContent(tweetElement);

		for (Tweet tweet : citation.getTweetSet()) {
			Element sourceLink = new Element("sourceLink", this.xmlns);
			sourceLink.addContent(tweet.getSourceLink());
			tweetElement.addContent(sourceLink);
			Element date = new Element("date", this.xmlns);
			date.addContent(tweet.getDate());
			tweetElement.addContent(date);

			Element tweetText = new Element("tweetText", this.xmlns);
			tweetText.addContent(tweet.getTweetText());
			tweetElement.addContent(tweetText);
			Element tweetID = new Element("tweetID", this.xmlns);
			tweetID.addContent(Long.toString(tweet.getTweetID()));
			tweetElement.addContent(tweetID);
			Element location = new Element("location", this.xmlns);
			location.addContent(tweet.getLocation());
			tweetElement.addContent(location);
			Element country = new Element("country", this.xmlns);
			country.addContent(tweet.getCountry());
			tweetElement.addContent(country);
			Element username = new Element("username", this.xmlns);
			username.addContent(tweet.getUsername());
			tweetElement.addContent(username);
			Element userProfileURL = new Element("userProfileURL", this.xmlns);
			userProfileURL.addContent(tweet.getUserProfileURL());
			tweetElement.addContent(userProfileURL);
			Element userImageURL = new Element("userImageURL", this.xmlns);
			userImageURL.addContent(tweet.getUserImageURL());
			tweetElement.addContent(userImageURL);

			Element originalTweetID = new Element("originalTweetID", this.xmlns);
			originalTweetID.addContent(Long.toString(tweet.getOriginalTweetID()));
			tweetElement.addContent(originalTweetID);
			Element isRetweet = new Element("isRetweet", this.xmlns);
			isRetweet.addContent(String.valueOf(tweet.getIsRetweet()));
			tweetElement.addContent(isRetweet);
		}

	}

	private void exportVirtualEditionInterAnnotations(Element textClass, VirtualEditionInter virtualEditionInter) {
		for (Annotation annotation : virtualEditionInter.getAnnotationSet()) {
			Element note = new Element("note", this.xmlns);
			note.setText(StringEscapeUtils.unescapeHtml(annotation.getText()));
			textClass.addContent(note);

			exportAnnotationRanges(annotation, note);

			if (annotation instanceof HumanAnnotation) {
				// TODO: set type - done
				note.setAttribute("resp", "#" + ((HumanAnnotation) annotation).getUser().getUsername());
				note.setAttribute("type", "human");
				exportAnnotationCategories(virtualEditionInter, (HumanAnnotation) annotation, note);
			} else if (annotation instanceof AwareAnnotation) {
				// TODO: set type - done
				// TODO ??? export aware annotation citations???
				// note.setAttribute("type", "aware");
				// exportAwareAnnotationCitation(note, ((AwareAnnotation) annotation));
			}
		}
	}

	private void exportAnnotationCategories(VirtualEditionInter virtualEditionInter, HumanAnnotation annotation,
			Element note) {
		for (Category category : annotation.getCategories()) {
			Element catRef = new Element("catRef", this.xmlns);
			catRef.setAttribute("scheme", "#" + virtualEditionInter.getEdition().getXmlId());
			catRef.setAttribute("target", "#" + category.getName());
			note.addContent(catRef);
		}
	}

	private void exportAnnotationRanges(Annotation annotation, Element note) {
		for (Range range : annotation.getRangeSet()) {
			Element quote = new Element("quote", this.xmlns);
			quote.setAttribute("from", range.getStart());
			quote.setAttribute("to", range.getEnd());
			quote.setAttribute("fromOffset", Integer.toString(range.getStartOffset()));
			quote.setAttribute("toOffset", Integer.toString(range.getEndOffset()));
			quote.setText(StringEscapeUtils.unescapeHtml(annotation.getQuote()));
			note.addContent(quote);
		}
	}

}
