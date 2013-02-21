package pt.ist.socialsoftware.edition.readers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jvstm.Atomic;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.joda.time.DateTime;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.DatabaseBootstrap;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Taxonomy;

public class ImportLdoDFromTEI {

	Element ldoDTEI = null;
	Namespace namespace = null;
	LdoD ldoD = null;

	HashMap<String, Object> xmlIdMap = new HashMap<String, Object>();
	HashMap<String, Set<Object>> xmlIdGroupMap = new HashMap<String, Set<Object>>();

	public void loadLdoDTEI() {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			// TODO: to use a configuration variable for the xml file
			doc = builder.build(new FileInputStream(
					"/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
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

		ldoDTEI = doc.getRootElement();
		namespace = ldoDTEI.getNamespace();

		loadLdoDTEIElements();
	}

	@Atomic
	private void loadLdoDTEIElements() {
		DatabaseBootstrap.initDatabase();
		ldoD = LdoD.getInstance();

		loadTitleStmt();

		loadListBibl();

		loadTaxonomies();

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

			xmlIdMap.put(tononomyID, taxonomy);

			taxonomy.setName(taxonomyTEI.getChild("bibl", namespace).getText());

			for (Element categoryTEI : taxonomyTEI.getChildren("category",
					namespace)) {
				Category category = new Category();
				category.setTaxonomy(taxonomy);

				String categoryID = categoryTEI.getAttributeValue("id",
						categoryTEI.getNamespace("xml"));

				xmlIdMap.put(categoryID, category);

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

		String groupId = corpusHeaderListBibl.getAttributeValue("id",
				corpusHeaderListBibl.getNamespace("xml"));

		for (Element bibl : corpusHeaderListBibl.getChildren("bibl", namespace)) {
			Edition edition = new Edition();
			edition.setLdoD(ldoD);

			String id = bibl.getAttributeValue("id", bibl.getNamespace("xml"));

			Set<Object> editionsSet = xmlIdGroupMap.get(groupId);
			if (editionsSet == null) {
				editionsSet = new HashSet<Object>();
			}

			editionsSet.add(edition);

			xmlIdGroupMap.put(groupId, editionsSet);
			xmlIdMap.put(id, edition);

			edition.setAuthor(bibl.getChild("author", namespace)
					.getChild("persName", namespace).getText());
			edition.setTitle(bibl.getChild("title", namespace).getText());
			edition.setEditor(bibl.getChild("editor", namespace)
					.getChild("persName", namespace).getText());
			edition.setDate(new DateTime(bibl.getChild("date", namespace)
					.getAttributeValue("when")));
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
