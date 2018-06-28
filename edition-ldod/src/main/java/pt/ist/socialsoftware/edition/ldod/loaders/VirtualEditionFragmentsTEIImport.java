package pt.ist.socialsoftware.edition.ldod.loaders;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.utils.RangeJson;

public class VirtualEditionFragmentsTEIImport {
	private static Logger logger = LoggerFactory.getLogger(VirtualEditionFragmentsTEIImport.class);

	LdoD ldoD = null;
	Namespace namespace = null;

	public String importFragmentFromTEI(InputStream inputStream) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		Document doc;
		try {
			doc = builder.build(inputStream);
		} catch (FileNotFoundException e) {
			throw new LdoDLoadException("Ficheiro não encontrado");
		} catch (JDOMException e) {
			throw new LdoDLoadException("Ficheiro com problemas de codificação TEI");
		} catch (IOException e) {
			throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato");
		}

		if (doc == null) {
			LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		return processImport(doc);
	}

	public void importFragmentFromTEI(String fragmentTEI) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		InputStream stream = new ByteArrayInputStream(fragmentTEI.getBytes());

		importFragmentFromTEI(stream);
	}

	@Atomic(mode = TxMode.WRITE)
	public String processImport(Document doc) {
		this.ldoD = LdoD.getInstance();
		this.namespace = doc.getRootElement().getNamespace();

		Fragment fragment = getFragment(doc);

		// TODO: importFragmentCitations - done
		importFragmentCitations(doc, fragment);

		importWitnesses(doc, fragment);

		importTextClasses(doc, fragment);

		return fragment.getXmlId();
	}

	private void importWitnesses(Document doc, Fragment fragment) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:witness", Filters.element(), null,
				Namespace.getNamespace("def", this.namespace.getURI()));
		List<Element> wits = new ArrayList<>(xp.evaluate(doc));

		while (!wits.isEmpty()) {
			Element wit = wits.remove(0);
			if (fragment.getFragInterByXmlId(wit.getAttributeValue("source").substring(1)) == null) {
				wits.add(wit);
			} else {
				String interXmlId = wit.getAttributeValue("id", Namespace.XML_NAMESPACE);
				String editionAcronym = interXmlId.substring(interXmlId.lastIndexOf("VIRT.") + "VIRT.".length(),
						interXmlId.lastIndexOf('.'));
				VirtualEdition virtualEdition = this.ldoD.getVirtualEdition(editionAcronym);

				logger.debug("importWitnesses id: {}, source: {}", interXmlId, wit.getAttributeValue("source"));
				virtualEdition.createVirtualEditionInter(
						fragment.getFragInterByXmlId(wit.getAttributeValue("source").substring(1)),
						Integer.parseInt(wit.getChild("num", this.namespace).getAttributeValue("value")));
			}
		}
	}

	private void importTextClasses(Document doc, Fragment fragment) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:textClass", Filters.element(), null,
				Namespace.getNamespace("def", this.namespace.getURI()));

		for (Element textClass : xp.evaluate(doc)) {
			VirtualEditionInter inter = (VirtualEditionInter) fragment
					.getFragInterByXmlId(textClass.getAttributeValue("source").substring(1));

			for (Element catRef : textClass.getChildren("catRef", this.namespace)) {
				importTag(catRef, inter);
			}

			for (Element note : textClass.getChildren("note", this.namespace)) {
				importAnnotation(note, inter);
			}
		}
	}

	// TODO - done
	private void importFragmentCitations(Document doc, Fragment fragment) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:fragmentCitations", Filters.element(), null,
				Namespace.getNamespace("def", this.namespace.getURI()));

		for (Element citationElement : xp.evaluate(doc)) {
			if (citationElement.getAttributeValue("type") == "twitter") {
				String sourceLink = citationElement.getAttributeValue("sourceLink");
				String date = citationElement.getAttributeValue("date");

				Element fragTextElement = citationElement.getChild("fragText", this.namespace);
				String fragText = fragTextElement.getAttributeValue("fragText");

				Element tweetTextElement = citationElement.getChild("tweetText", this.namespace);
				String tweetText = tweetTextElement.getAttributeValue("tweetText");

				long tweetID = Long.parseLong(citationElement.getAttributeValue("tweetID"));
				String location = citationElement.getAttributeValue("location");
				String country = citationElement.getAttributeValue("country");
				String username = citationElement.getAttributeValue("username");
				String userProfileURL = citationElement.getAttributeValue("userProfileURL");
				String userImageURL = citationElement.getAttributeValue("userImageURL");

				new TwitterCitation(fragment, sourceLink, date, fragText, tweetText, tweetID, location, country,
						username, userProfileURL, userImageURL);
			}
		}

	}

	private void importTag(Element catRef, VirtualEditionInter inter) {
		String username = catRef.getAttributeValue("resp").substring(1);
		String tag = catRef.getAttributeValue("target").substring(1);

		inter.getVirtualEdition().getTaxonomy().createTag(inter, tag, null, this.ldoD.getUser(username));
	}

	// TODO: else if aware - done
	// novo import annotation
	private void importAnnotation(Element note, VirtualEditionInter inter) {
		String text = StringEscapeUtils.escapeHtml(note.getText().trim());
		Element quoteElement = note.getChild("quote", this.namespace);
		String from = quoteElement.getAttributeValue("from");
		String to = quoteElement.getAttributeValue("to");
		String fromOffset = quoteElement.getAttributeValue("fromOffset");
		String toOffset = quoteElement.getAttributeValue("toOffset");
		String quote = quoteElement.getText().trim();

		RangeJson range = new RangeJson();
		range.setStart(from);
		range.setStartOffset(Integer.parseInt(fromOffset));
		range.setEnd(to);
		range.setEndOffset(Integer.parseInt(toOffset));
		List<RangeJson> rangeList = new ArrayList<>();
		rangeList.add(range);

		if (note.getAttributeValue("type") == "human") {
			String username = note.getAttributeValue("resp").substring(1);
			List<String> tagList = new ArrayList<>();
			for (Element catRef : note.getChildren("catRef", this.namespace)) {
				String tag = catRef.getAttributeValue("target").substring(1);
				tagList.add(tag);
			}
			inter.createHumanAnnotation(quote, text, this.ldoD.getUser(username), rangeList, tagList);
		}

		else if (note.getAttributeValue("type") == "aware") {
			long tweetID = Long.parseLong(note.getAttributeValue("citationId"));
			Citation citation = inter.getFragment().getCitationById(tweetID);
			// inter.createAwareAnnotation(quote, text, citation, rangeList);
		}
	}

	// original code
	// private void importAnnotation(Element note, VirtualEditionInter inter) {
	// String username = note.getAttributeValue("resp").substring(1);
	// String text = StringEscapeUtils.escapeHtml(note.getText().trim());
	// Element quoteElement = note.getChild("quote", this.namespace);
	// String from = quoteElement.getAttributeValue("from");
	// String to = quoteElement.getAttributeValue("to");
	// String fromOffset = quoteElement.getAttributeValue("fromOffset");
	// String toOffset = quoteElement.getAttributeValue("toOffset");
	// String quote = quoteElement.getText().trim();
	//
	// RangeJson range = new RangeJson();
	// range.setStart(from);
	// range.setStartOffset(Integer.parseInt(fromOffset));
	// range.setEnd(to);
	// range.setEndOffset(Integer.parseInt(toOffset));
	//
	// List<String> tagList = new ArrayList<>();
	// for (Element catRef : note.getChildren("catRef", this.namespace)) {
	// String tag = catRef.getAttributeValue("target").substring(1);
	// tagList.add(tag);
	// }
	// List<RangeJson> rangeList = new ArrayList<>();
	// rangeList.add(range);
	// inter.createAnnotation(quote, text, this.ldoD.getUser(username), rangeList,
	// tagList);
	// }

	private Fragment getFragment(Document doc) {
		LdoD ldoD = LdoD.getInstance();

		Namespace namespace = doc.getRootElement().getNamespace();
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:TEI", Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));
		String fragXmlId = xp.evaluate(doc).get(0).getAttributeValue("id", Namespace.XML_NAMESPACE);

		return ldoD.getFragmentByXmlId(fragXmlId);
	}
}
