package pt.ist.socialsoftware.edition.ldod.export;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.domain.Annotation;
import pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Range;
import pt.ist.socialsoftware.edition.ldod.domain.Tag;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

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

	private void exportFragmentCitations(Element teiHeader, Fragment fragment) {
		Element citationList = new Element("citationList", this.xmlns);
		teiHeader.addContent(citationList);
		for (Citation citation : fragment.getCitationSet()) {
			// acho q afinal vai ser preciso a declaração de citationElement ficar aqui
			// para passar depois ao método exportTwitterCitation
			Element citationElement = new Element("citation", this.xmlns);
			citationList.addContent(citationElement);

			exportCitation(citationElement, citation);
			if (citation instanceof TwitterCitation) {
				exportTwitterCitation(citationElement, (TwitterCitation) citation);
			}
		}
	}

	protected void exportCitation(Element citationElement, Citation citation) {
		citationElement.setAttribute("sourceLink", citation.getSourceLink());
		citationElement.setAttribute("date", citation.getDate());
		// citationElement.setAttribute("fragText", citation.getFragText());

		Element fragTex = new Element("fragTex", this.xmlns);
		fragTex.addContent(citation.getFragText());
		citationElement.addContent(fragTex);

		// old code
		// Element citationElement = new Element("citation", this.xmlns);
		// citationList.addContent(citationElement);
	}

	protected void exportTwitterCitation(Element citationElement, TwitterCitation citation) {
		citationElement.setAttribute("type", "twitter");

		Element tweetText = new Element("tweetText", this.xmlns);
		tweetText.addContent(citation.getTweetText());
		citationElement.addContent(tweetText);

		citationElement.setAttribute("tweetId", Long.toString(citation.getTweetID()));
		citationElement.setAttribute("location", citation.getLocation());
		citationElement.setAttribute("country", citation.getCountry());
		citationElement.setAttribute("username", citation.getUsername());
		citationElement.setAttribute("userProfileURL", citation.getUserProfileURL());
		citationElement.setAttribute("userImageURL", citation.getUserProfileURL());
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
				// TODO - done
				note.setAttribute("type", "aware");
				note.setAttribute("citationId", Long.toString(((AwareAnnotation) annotation).getCitation().getId()));
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
