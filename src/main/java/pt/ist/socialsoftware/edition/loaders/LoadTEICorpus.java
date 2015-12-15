package pt.ist.socialsoftware.edition.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.utils.DateUtils;

public class LoadTEICorpus {

	private Element ldoDTEI = null;
	private Namespace namespace = null;
	private LdoD ldoD = null;

	private Document doc = null;

	private final Map<String, List<Object>> xmlIDMap = new HashMap<String, List<Object>>();

	private void putObjectByXmlID(String xmlID, Object object) {

		List<Object> list = xmlIDMap.get(xmlID);
		if (list == null) {
			list = new ArrayList<Object>();
		}
		list.add(object);

		xmlIDMap.put(xmlID, list);
	}

	private List<Object> getObjectsByXmlID(String xmlID) {
		List<Object> objects = xmlIDMap.get(xmlID);
		return objects;
	}

	XPathFactory xpfac = XPathFactory.instance();

	private void parseTEIFile(InputStream file) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			// TODO: create a config variable for the xml file
			doc = builder.build(file);
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

		ldoDTEI = doc.getRootElement();
		namespace = ldoDTEI.getNamespace();
	}

	@Atomic(mode = TxMode.WRITE)
	public void loadTEICorpus(InputStream file) {
		parseTEIFile(file);

		ldoD = LdoD.getInstance();

		loadTitleStmt();

		loadListBibl();

		loadHeteronyms();
	}

	private void loadHeteronyms() {
		Element corpusHeteronyms = ldoDTEI.getChild("teiHeader", namespace).getChild("profileDesc", namespace)
				.getChild("particDesc", namespace).getChild("listPerson", namespace);

		for (Element heteronymTEI : corpusHeteronyms.getChildren("person", namespace)) {
			String heteronymXmlID = heteronymTEI.getAttributeValue("id", heteronymTEI.getNamespace("xml"));

			if (getObjectsByXmlID(heteronymXmlID) != null) {
				throw new LdoDLoadException("xml:id:" + heteronymXmlID + " já foi declarado");
			}

			assert getObjectsByXmlID(heteronymXmlID) == null : "xml:id:" + heteronymXmlID + " IS ALREADY DECLARED";

			String name = heteronymTEI.getChildText("persName", namespace);

			Heteronym heteronym = new Heteronym(ldoD, name);

			putObjectByXmlID(heteronymXmlID, heteronym);

			heteronym.setXmlId(heteronymXmlID);

		}
	}

	private void loadListBibl() {
		Element corpusHeaderListBibl = ldoDTEI.getChild("teiHeader", namespace).getChild("fileDesc", namespace)
				.getChild("sourceDesc", namespace).getChild("listBibl", namespace);

		String listEditionsXmlID = corpusHeaderListBibl.getAttributeValue("id",
				corpusHeaderListBibl.getNamespace("xml"));

		if (getObjectsByXmlID(listEditionsXmlID) != null) {
			throw new LdoDLoadException("xml:id:" + listEditionsXmlID + " já foi declarado");
		}

		assert getObjectsByXmlID(listEditionsXmlID) == null : "xml:id:" + listEditionsXmlID + " IS ALREADY DECLARED";

		for (Element bibl : corpusHeaderListBibl.getChildren("bibl", namespace)) {
			String editionXmlID = bibl.getAttributeValue("id", bibl.getNamespace("xml"));

			if (getObjectsByXmlID(editionXmlID) != null) {
				throw new LdoDLoadException("xml:id:" + editionXmlID + " já foi declarado");
			}

			assert getObjectsByXmlID(editionXmlID) == null : "xml:id:" + editionXmlID + " IS ALREADY DECLARED";

			String author = bibl.getChild("author", namespace).getChild("persName", namespace).getText();
			String title = bibl.getChild("title", namespace).getText();
			String editor = bibl.getChild("editor", namespace).getChild("persName", namespace).getText();
			LocalDate date = DateUtils.convertDate(bibl.getChild("date", namespace).getAttributeValue("when"));

			ExpertEdition edition = new ExpertEdition(ldoD, title, author, editor, date);

			edition.setXmlId(editionXmlID);

			putObjectByXmlID(editionXmlID, edition);
			putObjectByXmlID(listEditionsXmlID, edition);

		}
	}

	private void loadTitleStmt() {
		Element corpusHeaderTitleStmt = ldoDTEI.getChild("teiHeader", namespace).getChild("fileDesc", namespace)
				.getChild("titleStmt", namespace);

		ldoD.setTitle(corpusHeaderTitleStmt.getChild("title", namespace).getText());
		ldoD.setAuthor(corpusHeaderTitleStmt.getChild("author", namespace).getText());
		ldoD.setEditor(corpusHeaderTitleStmt.getChild("editor", namespace).getText());
		ldoD.setSponsor(corpusHeaderTitleStmt.getChild("sponsor", namespace).getText());
		ldoD.setFunder(corpusHeaderTitleStmt.getChild("funder", namespace).getText());
		ldoD.setPrincipal(corpusHeaderTitleStmt.getChild("principal", namespace).getText());
	}

}