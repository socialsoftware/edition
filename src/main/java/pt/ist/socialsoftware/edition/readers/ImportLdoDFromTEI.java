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
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.DateTime;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.DatabaseBootstrap;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.EditionInterpretation;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.SourceInterpretation;
import pt.ist.socialsoftware.edition.domain.Taxonomy;

public class ImportLdoDFromTEI {

	Element ldoDTEI = null;
	Namespace namespace = null;
	LdoD ldoD = null;

	Document doc = null;

	HashMap<String, Object> xmlIDMap = new HashMap<String, Object>();
	HashMap<String, Set<Object>> xmlIDGroupMap = new HashMap<String, Set<Object>>();

	XPathFactory xpfac = XPathFactory.instance();

	public void loadLdoDTEI() {
		SAXBuilder builder = new SAXBuilder();
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

		loadCorpusHeader();

		loadFragments();

	}

	private void loadFragments() {
		XPathExpression<Element> xp = xpfac.compile(
				"/def:teiCorpus/def:TEI/def:teiHeader", Filters.element(),
				null, Namespace.getNamespace("def", namespace.getURI()));

		for (Element fragmentTEI : xp.evaluate(doc)) {
			String fragmentTEIID = fragmentTEI.getAttributeValue("id",
					fragmentTEI.getNamespace("xml"));

			String fragmentTEITitle = fragmentTEI
					.getChild("fileDesc", namespace)
					.getChild("titleStmt", namespace)
					.getChildText("title", namespace);

			Fragment fragment = new Fragment();
			fragment.setLdoD(ldoD);
			fragment.setTitle(fragmentTEITitle);

			xmlIDMap.put(fragmentTEIID, fragment);

			loadSourceManuscripts(fragment);
			loadPrintedSources(fragment);
			loadWitnesses(fragment);

		}
	}

	private void loadWitnesses(Fragment fragment) {
		XPathExpression<Element> xp = xpfac
				.compile(
						"/def:teiCorpus/def:TEI/def:teiHeader/def:fileDesc/def:sourceDesc/def:listWit/.//def:witness",
						Filters.element(), null,
						Namespace.getNamespace("def", namespace.getURI()));

		for (Element witness : xp.evaluate(doc)) {
			Object object = xmlIDMap.get(witness.getChild("ref", namespace)
					.getAttributeValue("target").substring(1));

			String groupId = witness.getParentElement().getAttributeValue("id",
					witness.getNamespace("xml"));

			Set<Object> witnessesSet = xmlIDGroupMap.get(groupId);
			if (witnessesSet == null) {
				witnessesSet = new HashSet<Object>();
			}

			if (object instanceof ManuscriptSource) {
				SourceInterpretation sourceInter = new SourceInterpretation();
				sourceInter.setFragment(fragment);
				sourceInter.setSource((ManuscriptSource) object);

				xmlIDMap.put(
						witness.getAttributeValue("id",
								witness.getNamespace("xml")), sourceInter);

				witnessesSet.add(sourceInter);
				xmlIDGroupMap.put(groupId, witnessesSet);

			} else if (object instanceof PrintedSource) {
				SourceInterpretation sourceInter = new SourceInterpretation();
				sourceInter.setFragment(fragment);
				sourceInter.setSource((PrintedSource) object);

				xmlIDMap.put(
						witness.getAttributeValue("id",
								witness.getNamespace("xml")), sourceInter);

				witnessesSet.add(sourceInter);
				xmlIDGroupMap.put(groupId, witnessesSet);

			} else if (object instanceof Edition) {
				EditionInterpretation editionInter = new EditionInterpretation();
				editionInter.setFragment(fragment);
				editionInter.setEdition((Edition) object);

				xmlIDMap.put(
						witness.getAttributeValue("id",
								witness.getNamespace("xml")), editionInter);

				witnessesSet.add(editionInter);
				xmlIDGroupMap.put(groupId, witnessesSet);
			}

			System.out.println(object);
			System.out.println(witness.getParentElement().getAttributeValue(
					"id", witness.getNamespace("xml")));
			System.out.println(witness.getChild("ref", namespace)
					.getAttributeValue("target").substring(1));
			System.out.println(witness);
		}

	}

	private void loadPrintedSources(Fragment fragment) {
		XPathExpression<Element> xp = xpfac
				.compile(
						"/def:teiCorpus/def:TEI/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:bibl",
						Filters.element(), null,
						Namespace.getNamespace("def", namespace.getURI()));

		for (Element bibl : xp.evaluate(doc)) {
			PrintedSource printedSource = new PrintedSource();
			printedSource.setFragment(fragment);

			String biblID = bibl.getAttributeValue("id",
					bibl.getNamespace("xml"));

			xmlIDMap.put(biblID, printedSource);

			printedSource.setTitle(bibl.getChildText("title", namespace));
			printedSource.setPubPlace(bibl.getChildText("pubPlace", namespace));
			printedSource.setIssue(bibl.getChildText("biblScope", namespace));
			printedSource.setDate(new DateTime(bibl.getChildText("date",
					namespace)));
		}

	}

	private void loadSourceManuscripts(Fragment fragment) {
		XPathExpression<Element> xp = xpfac
				.compile(
						"/def:teiCorpus/def:TEI/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:msDesc",
						Filters.element(), null,
						Namespace.getNamespace("def", namespace.getURI()));

		for (Element msDesc : xp.evaluate(doc)) {
			ManuscriptSource manuscript = new ManuscriptSource();
			manuscript.setFragment(fragment);

			String manuscriptID = msDesc.getAttributeValue("id",
					msDesc.getNamespace("xml"));

			xmlIDMap.put(manuscriptID, manuscript);

			Element msId = msDesc.getChild("msIdentifier", namespace);
			manuscript
					.setSettlement(msId.getChildText("settlement", namespace));
			manuscript
					.setRepository(msId.getChildText("repository", namespace));
			manuscript.setIdno(msId.getChildText("idno", namespace));
		}

	}

	private void loadCorpusHeader() {
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

			xmlIDMap.put(
					heteronymTEI.getAttributeValue("id",
							heteronymTEI.getNamespace("xml")), heteronym);

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

			xmlIDMap.put(tononomyID, taxonomy);

			taxonomy.setName(taxonomyTEI.getChild("bibl", namespace).getText());

			for (Element categoryTEI : taxonomyTEI.getChildren("category",
					namespace)) {
				Category category = new Category();
				category.setTaxonomy(taxonomy);

				String categoryID = categoryTEI.getAttributeValue("id",
						categoryTEI.getNamespace("xml"));

				xmlIDMap.put(categoryID, category);

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

			Set<Object> editionsSet = xmlIDGroupMap.get(groupId);
			if (editionsSet == null) {
				editionsSet = new HashSet<Object>();
			}

			editionsSet.add(edition);

			xmlIDGroupMap.put(groupId, editionsSet);
			xmlIDMap.put(id, edition);

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
