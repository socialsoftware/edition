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

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc == null) {
			LdoDException ex = new LdoDException(
					"Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		ldoDTEI = doc.getRootElement();
		namespace = ldoDTEI.getNamespace();
	}

	public void loadTEICorpus(InputStream file) {
		parseTEIFile(file);

		ldoD = LdoD.getInstance();

		loadTitleStmt();

		loadListBibl();

		loadTaxonomies();

		loadHeteronyms();
	}

	private void loadHeteronyms() {
		Element corpusHeteronyms = ldoDTEI.getChild("teiHeader", namespace)
				.getChild("profileDesc", namespace)
				.getChild("particDesc", namespace)
				.getChild("listPerson", namespace);

		for (Element heteronymTEI : corpusHeteronyms.getChildren("person",
				namespace)) {
			Heteronym heteronym = new Heteronym();
			heteronym.setLdoD(ldoD);

			String heteronymXmlID = heteronymTEI.getAttributeValue("id",
					heteronymTEI.getNamespace("xml"));

			if (getObjectsByXmlID(heteronymXmlID) != null) {
				throw new LdoDException("xml:id:" + heteronymXmlID
						+ " já foi declarado");
			}

			assert getObjectsByXmlID(heteronymXmlID) == null : "xml:id:"
					+ heteronymXmlID + " IS ALREADY DECLARED";

			putObjectByXmlID(heteronymXmlID, heteronym);

			heteronym.setXmlId(heteronymXmlID);

			heteronym.setName(heteronymTEI.getChildText("persName", namespace));
		}
	}

	private void loadTaxonomies() {
		List<Element> corpusTaxonomies = ldoDTEI
				.getChild("teiHeader", namespace)
				.getChild("encodingDesc", namespace)
				.getChild("classDecl", namespace)
				.getChildren("taxonomy", namespace);

		for (Element taxonomyTEI : corpusTaxonomies) {
			Taxonomy taxonomy = new Taxonomy();
			taxonomy.setLdoD(ldoD);

			String tononomyID = taxonomyTEI.getAttributeValue("id",
					taxonomyTEI.getNamespace("xml"));

			if (getObjectsByXmlID(tononomyID) != null) {
				throw new LdoDException("xml:id:" + tononomyID
						+ " já foi declarado");
			}

			assert getObjectsByXmlID(tononomyID) == null : "xml:id:"
					+ tononomyID + " IS ALREADY DECLARED";

			putObjectByXmlID(tononomyID, taxonomy);

			taxonomy.setXmlId(tononomyID);

			taxonomy.setName(taxonomyTEI.getChild("bibl", namespace).getText());

			for (Element categoryTEI : taxonomyTEI.getChildren("category",
					namespace)) {
				Category category = new Category();
				category.setTaxonomy(taxonomy);

				String categoryID = categoryTEI.getAttributeValue("id",
						categoryTEI.getNamespace("xml"));

				if (getObjectsByXmlID(categoryID) != null) {
					throw new LdoDException("xml:id:" + categoryID
							+ " já foi declarado");
				}

				assert getObjectsByXmlID(categoryID) == null : "xml:id:"
						+ categoryID + " IS ALREADY DECLARED";

				putObjectByXmlID(categoryID, category);

				category.setXmlId(categoryID);

				category.setName(categoryTEI.getChild("catDesc", namespace)
						.getText());
			}

		}
	}

	private void loadListBibl() {
		Element corpusHeaderListBibl = ldoDTEI.getChild("teiHeader", namespace)
				.getChild("fileDesc", namespace)
				.getChild("sourceDesc", namespace)
				.getChild("listBibl", namespace);

		String listEditionsXmlID = corpusHeaderListBibl.getAttributeValue("id",
				corpusHeaderListBibl.getNamespace("xml"));

		if (getObjectsByXmlID(listEditionsXmlID) != null) {
			throw new LdoDException("xml:id:" + listEditionsXmlID
					+ " já foi declarado");
		}

		assert getObjectsByXmlID(listEditionsXmlID) == null : "xml:id:"
				+ listEditionsXmlID + " IS ALREADY DECLARED";

		for (Element bibl : corpusHeaderListBibl.getChildren("bibl", namespace)) {
			Edition edition = new Edition();
			edition.setLdoD(ldoD);

			String editionXmlID = bibl.getAttributeValue("id",
					bibl.getNamespace("xml"));

			if (getObjectsByXmlID(editionXmlID) != null) {
				throw new LdoDException("xml:id:" + editionXmlID
						+ " já foi declarado");
			}

			assert getObjectsByXmlID(editionXmlID) == null : "xml:id:"
					+ editionXmlID + " IS ALREADY DECLARED";

			putObjectByXmlID(editionXmlID, edition);

			edition.setXmlId(editionXmlID);

			putObjectByXmlID(listEditionsXmlID, edition);

			edition.setAuthor(bibl.getChild("author", namespace)
					.getChild("persName", namespace).getText());
			edition.setTitle(bibl.getChild("title", namespace).getText());
			edition.setEditor(bibl.getChild("editor", namespace)
					.getChild("persName", namespace).getText());
			edition.setDate(bibl.getChild("date", namespace).getAttributeValue(
					"when"));
		}
	}

	private void loadTitleStmt() {
		Element corpusHeaderTitleStmt = ldoDTEI
				.getChild("teiHeader", namespace)
				.getChild("fileDesc", namespace)
				.getChild("titleStmt", namespace);

		ldoD.setTitle(corpusHeaderTitleStmt.getChild("title", namespace)
				.getText());
		ldoD.setAuthor(corpusHeaderTitleStmt.getChild("author", namespace)
				.getText());
		ldoD.setEditor(corpusHeaderTitleStmt.getChild("editor", namespace)
				.getText());
		ldoD.setSponsor(corpusHeaderTitleStmt.getChild("sponsor", namespace)
				.getText());
		ldoD.setFunder(corpusHeaderTitleStmt.getChild("funder", namespace)
				.getText());
		ldoD.setPrincipal(corpusHeaderTitleStmt
				.getChild("principal", namespace).getText());
	}

}