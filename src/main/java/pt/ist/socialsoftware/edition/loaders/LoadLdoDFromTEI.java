package pt.ist.socialsoftware.edition.loaders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Content.CType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.DateTime;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.EditionInter;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceUnit;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VariationPoint;
import pt.ist.socialsoftware.edition.visitors.CanonicalCleaner;
import pt.ist.socialsoftware.edition.visitors.GraphConsistencyChecker;
import pt.ist.socialsoftware.edition.visitors.GraphWriter;

public class LoadLdoDFromTEI {

	public static void main(String[] args) {
		new LoadLdoDFromTEI().loadLdoDTEI();
	}

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

	private List<Object> getObjectsByListXmlID(String[] listXmlId) {
		List<Object> objects = new ArrayList<Object>();
		for (String xmlId : listXmlId) {
			objects.addAll(getObjectsByXmlID(xmlId.substring(1)));
		}
		return objects;
	}

	XPathFactory xpfac = XPathFactory.instance();

	public void loadLdoDTEI() {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			// TODO: create a config variable for the xml file
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

	private void loadLdoDTEIElements() {
		ldoD = LdoD.getInstance();

		loadCorpusHeader();

		loadFragments();

	}

	private void loadFragments() {
		XPathExpression<Element> xp = xpfac.compile(
				"/def:teiCorpus/def:TEI/def:teiHeader", Filters.element(),
				null, Namespace.getNamespace("def", namespace.getURI()));

		for (Element fragmentTEI : xp.evaluate(doc)) {
			String fragmentTEIID = fragmentTEI.getParentElement()
					.getAttributeValue("id", fragmentTEI.getNamespace("xml"));

			assert fragmentTEIID != null : "MISSING xml:id ATTRIBUTE IN FRAGMENT <TEI > ELEMENT";

			String fragmentTEITitle = fragmentTEI
					.getChild("fileDesc", namespace)
					.getChild("titleStmt", namespace)
					.getChildText("title", namespace);

			Fragment fragment = new Fragment();
			fragment.setLdoD(ldoD);
			fragment.setTitle(fragmentTEITitle);

			assert getObjectsByXmlID(fragmentTEIID) == null : "xml:id:"
					+ fragmentTEIID + " IS ALREADY DECLARED";

			putObjectByXmlID(fragmentTEIID, fragment);

			loadSourceManuscripts(fragment, fragmentTEIID);
			loadPrintedSources(fragment, fragmentTEIID);
			loadWitnesses(fragment, fragmentTEIID);

			loadFragmentText(fragment, fragmentTEIID);

		}
	}

	private void loadFragmentText(Fragment fragment, String fragmentXmlID) {
		String selectThisFragment = "[@xml:id='" + fragmentXmlID + "']";
		String queryExpression = "/def:teiCorpus/def:TEI" + selectThisFragment
				+ "/def:text/.//def:p";

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		VariationPoint startPoint = new VariationPoint();
		startPoint.setFragment(fragment);
		VariationPoint endPoint = null;

		List<FragInter> fragInters = new ArrayList<FragInter>(
				fragment.getFragmentInter());
		for (Element paragraph : xp.evaluate(doc)) {
			endPoint = loadContent(paragraph, startPoint, fragInters);
			startPoint = endPoint;
		}

		CanonicalCleaner cleaner = new CanonicalCleaner();
		fragment.getVariationPoint().accept(cleaner);

		GraphConsistencyChecker checkConsistency = new GraphConsistencyChecker();
		fragment.getVariationPoint().accept(checkConsistency);

		GraphWriter graphWriter = new GraphWriter();
		fragment.getVariationPoint().accept(graphWriter);
		System.out.println(graphWriter.getResult());
		//
		// for (FragInter fragInter : fragment.getFragmentInter()) {
		// PlainText4InterWriter interWriter = new PlainText4InterWriter(
		// fragInter);
		// fragment.getVariationPoint().accept(interWriter);
		// System.out.println(interWriter.getResult());
		// }
	}

	private VariationPoint loadContent(Element element,
			VariationPoint startPoint, List<FragInter> fragInters) {
		VariationPoint endPoint = null;
		for (Content content : element.getContent()) {
			if (content.getCType() == CType.Text) {
				endPoint = loadSimpleText((Text) content, startPoint,
						fragInters);
			} else if (content.getCType() == CType.Comment) {
				// ignore comments
				endPoint = startPoint;
			} else if (content.getCType() == CType.Element) {
				Element element2 = (Element) content;
				if (element2.getName().equals("lb")) {
					endPoint = loadLb(element2, startPoint, fragInters);
				} else if (element2.getName().equals("app")) {
					endPoint = loadApp(element2, startPoint, fragInters);
				} else if (element2.getName().equals("space")) {
					endPoint = loadSpace(element2, startPoint, fragInters);
				} else if (element2.getName().equals("seg")) {
					endPoint = startPoint;
				} else if (element2.getName().equals("subst")) {
					endPoint = startPoint;
				} else if (element2.getName().equals("add")) {
					endPoint = startPoint;
				} else {
					assert false : "DOES NOT HANDLE LOAD OF:" + element2
							+ " OF TYPE:" + element2.getCType().toString();
				}
			}
			startPoint = endPoint;

		}
		return endPoint;
	}

	// private VariationPoint loadSeg(Element element, VariationPoint
	// startPoint) {
	// for (Content content : element.getChildren()) {
	// loadContent(content, startPoint);
	// }
	// }
	//
	// private void loadDel(Element element, VariationPoint startPoint,
	// VariationPoint endPoint) {
	// for (Content content : element.getChildren()) {
	// loadContent(content, startPoint, endPoint);
	// }
	// }
	//
	// private void loadAdd(Element element, VariationPoint startPoint,
	// VariationPoint endPoint) {
	// for (Content content : element.getChildren()) {
	// loadContent(content, startPoint, endPoint);
	// }
	// }
	//
	// private void loadSubst(Element element, VariationPoint startPoint,
	// VariationPoint endPoint) {
	// for (Content content : element.getChildren()) {
	// loadContent(content, startPoint, endPoint);
	// }
	//
	// }

	private VariationPoint loadSpace(Element element,
			VariationPoint startPoint, List<FragInter> fragInters) {
		VariationPoint endPoint = new VariationPoint();

		SpaceText.SpaceDim dim = getDimAttribute(element);
		SpaceText.SpaceUnit unit = getUnitAttribute(element);
		int quantity = getQuantityAttribute(element);

		Reading reading = new Reading();
		SpaceText text = new SpaceText();
		text.setReading(reading);
		text.setDim(dim);
		text.setQuantity(quantity);
		text.setUnit(unit);
		text.setNextText(null);

		for (FragInter fragIter : fragInters) {
			reading.addFragInters(fragIter);
		}

		startPoint.addOutReadings(reading);
		endPoint.addInReadings(reading);

		return endPoint;
	}

	private int getQuantityAttribute(Element element) {
		int quantity = 0;
		Attribute quantityAttribute = element.getAttribute("quantity");
		if (quantityAttribute == null) {
			quantity = 0;
		} else {
			try {
				quantity = Integer.parseInt(quantityAttribute.getValue());
			} catch (NumberFormatException e) {
				assert false : "VALUE FOR ATTRIBUTE quantity IN NOT INTEGER=\""
						+ quantityAttribute.getValue() + "\"";
			}
		}
		return quantity;
	}

	private SpaceText.SpaceUnit getUnitAttribute(Element element) {
		SpaceText.SpaceUnit unit = SpaceUnit.UNKNOWN;
		Attribute unitAttribute = element.getAttribute("unit");
		if (unitAttribute == null) {
			unit = SpaceUnit.UNKNOWN;
		} else if (unitAttribute.getValue().equals("minims")) {
			unit = SpaceUnit.MINIMS;
		} else {
			assert false : "UNKNOWN VALUE FOR ATTRIBUTE unit=\""
					+ unitAttribute.getValue() + "\"";
		}

		return unit;
	}

	private SpaceText.SpaceDim getDimAttribute(Element element) {
		SpaceText.SpaceDim dim = null;

		Attribute dimAttribute = element.getAttribute("dim");
		if (dimAttribute == null) {
			dim = SpaceDim.UNKNOWN;
		} else if (dimAttribute.getValue().equals("vertical")) {
			dim = SpaceDim.VERTICAL;
		} else if (dimAttribute.getValue().equals("horizontal")) {
			dim = SpaceDim.HORIZONTAL;
		} else {
			assert false : "UNKNOWN VALUE FOR ATTRIBUTE dim=\""
					+ dimAttribute.getValue() + "\"";
		}
		return dim;
	}

	private VariationPoint loadApp(Element appElement,
			VariationPoint startPoint, List<FragInter> fragInters) {
		VariationPoint endPoint = new VariationPoint();
		for (Element rdgElement : appElement.getChildren()) {
			if (rdgElement.getName().equals("rdg")) {
				VariationPoint tmpPoint = loadRdg(rdgElement, startPoint);

				for (Reading reading : tmpPoint.getInReadings()) {
					reading.setNextVariationPoint(endPoint);
				}
			} else if (rdgElement.getName().equals("rdgGrp")) {
				List<VariationPoint> endPoints = loadRdgGrp(rdgElement,
						startPoint);
				for (VariationPoint tmpPoint : endPoints) {
					for (Reading reading : tmpPoint.getInReadings()) {
						reading.setNextVariationPoint(endPoint);
					}
				}
			} else {
				assert false : "UNEXPECTED ELEMENT NESTED WITHIN APP";
			}
		}

		List<FragInter> pendingFragInters = new ArrayList<FragInter>(fragInters);
		for (Reading rdg : endPoint.getInReadings()) {
			pendingFragInters.removeAll(rdg.getFragInters());
		}

		if (!pendingFragInters.isEmpty()) {
			addEmptyReading(startPoint, endPoint, pendingFragInters);
		}

		return endPoint;
	}

	private List<VariationPoint> loadRdgGrp(Element rdgGrpElement,
			VariationPoint startPoint) {
		List<VariationPoint> endPoints = new ArrayList<VariationPoint>();
		for (Element rdgElement : rdgGrpElement.getChildren()) {
			if (rdgElement.getName().equals("rdg")) {
				endPoints.add(loadRdg(rdgElement, startPoint));
			} else if (rdgElement.getName().equals("rdgGrp")) {
				endPoints.addAll(loadRdgGrp(rdgElement, startPoint));
			}
		}
		return endPoints;
	}

	private VariationPoint loadRdg(Element rdgElement, VariationPoint startPoint) {
		Attribute witAttribute = rdgElement.getAttribute("wit");
		assert witAttribute != null : "rdg ELEMENT REQUIRES VALUE FOR ATTRIBUTE wit"
				+ rdgElement;

		String[] listInterXmlId = rdgElement.getAttribute("wit").getValue()
				.split("\\s+");
		List<FragInter> fragInters = getFragItersByListXmlID(listInterXmlId);
		VariationPoint endPoint = new VariationPoint();

		endPoint = loadContent(rdgElement, startPoint, fragInters);

		return endPoint;
	}

	private List<FragInter> getFragItersByListXmlID(String[] listInterXmlId) {
		List<Object> objects = getObjectsByListXmlID(listInterXmlId);
		List<FragInter> fragIters = new ArrayList<FragInter>();
		for (Object object : objects) {
			fragIters.add((FragInter) object);
		}
		return fragIters;
	}

	private VariationPoint loadLb(Element element, VariationPoint startPoint,
			List<FragInter> fragInters) {

		List<FragInter> pendingFragInterps = new ArrayList<FragInter>(
				fragInters);
		List<FragInter> toFragInters = null;

		Attribute edAttribute = element.getAttribute("ed");
		if (edAttribute == null) {
			toFragInters = fragInters;
		} else {

			String[] listInterXmlId = element.getAttribute("ed").getValue()
					.split("\\s+");
			toFragInters = getFragItersByListXmlID(listInterXmlId);
		}

		VariationPoint endPoint = new VariationPoint();

		Reading reading = new Reading();
		LbText text = new LbText();
		text.setReading(reading);
		text.setBreakWord(isBreak(element));
		text.setHyphenated(isHiphenated(element));
		text.setNextText(null);

		for (FragInter fragIter : toFragInters) {
			reading.addFragInters(fragIter);
			pendingFragInterps.remove(fragIter);
		}

		startPoint.addOutReadings(reading);
		endPoint.addInReadings(reading);

		if (!pendingFragInterps.isEmpty()) {
			addEmptyReading(startPoint, endPoint, pendingFragInterps);
		}

		return endPoint;
	}

	private void addEmptyReading(VariationPoint startPoint,
			VariationPoint endPoint, List<FragInter> pendingFragInterps) {
		Reading emptyRdg = new Reading();
		EmptyText emptyText = new EmptyText();
		emptyText.setReading(emptyRdg);
		emptyText.setNextText(null);

		for (FragInter fragInter : pendingFragInterps) {
			emptyRdg.addFragInters(fragInter);
		}

		startPoint.addOutReadings(emptyRdg);
		endPoint.addInReadings(emptyRdg);
	}

	private Boolean isHiphenated(Element element) {
		String hyphenated = null;
		Attribute hyphenatedAttribute = element.getAttribute("type");
		if (hyphenatedAttribute != null) {
			hyphenated = element.getAttributeValue("type");
		}

		Boolean toHyphenate = false;
		if (hyphenated == null) {
			toHyphenate = false;
		} else if (hyphenated.equals("hyphenated")) {
			toHyphenate = true;
		} else {
			assert false : "UNEXPECTED PARAMETER FOR hyphenated ATTRIBUTE"
					+ element;
		}
		return toHyphenate;
	}

	private Boolean isBreak(Element element) {
		String breakWord = "yes";
		Attribute breakAttribute = element.getAttribute("break");
		if (breakAttribute != null) {
			breakWord = element.getAttributeValue("break");
		}

		Boolean toBreak = false;
		if (breakWord == null || breakWord.equals("yes")) {
			toBreak = true;
		} else if (breakWord.equals("no")) {
			toBreak = false;
		} else {
			assert false : "INVALID PARAMETER FOR break ATTRIBUTE" + element;
		}
		return toBreak;
	}

	private VariationPoint loadSimpleText(Text text, VariationPoint startPoint,
			List<FragInter> fragInters) {
		String value = text.getTextTrim();

		VariationPoint endPoint = new VariationPoint();
		if (value.equals("")) {
			// ignore empty text
			endPoint = startPoint;
		} else {
			Reading reading = new Reading();
			SimpleText simpleText = new SimpleText();
			simpleText.setReading(reading);
			simpleText.setValue(value);
			simpleText.setNextText(null);

			for (FragInter fragInter : fragInters) {
				reading.addFragInters(fragInter);
			}

			startPoint.addOutReadings(reading);
			endPoint.addInReadings(reading);
		}
		return endPoint;
	}

	// TODO: a cleaner way to read parent's xmlID
	private void loadWitnesses(Fragment fragment, String fragmentTEIID) {
		String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
		String queryExpression = "/def:teiCorpus/def:TEI"
				+ selectThisFragment
				+ "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listWit/.//def:witness";

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		for (Element witness : xp.evaluate(doc)) {
			String sourceOrEditionXmlID = witness.getChild("ref", namespace)
					.getAttributeValue("target").substring(1);

			List<Object> objects = getObjectsByXmlID(sourceOrEditionXmlID);

			assert (objects != null) && (!objects.isEmpty()) : "MISSING SOURCE OBJECT FOR xml:id:"
					+ sourceOrEditionXmlID;

			String witnessXmlID = witness.getAttributeValue("id",
					witness.getNamespace("xml"));
			String witnessListXmlID = witness.getParentElement()
					.getAttributeValue("id", witness.getNamespace("xml"));
			String witnessListXmlID2 = witness.getParentElement()
					.getParentElement()
					.getAttributeValue("id", witness.getNamespace("xml"));

			assert witnessXmlID != null : "MISSING xml:id FOR WITNESS";
			assert getObjectsByXmlID(witnessXmlID) == null : "xml:id:"
					+ witnessXmlID + " IS ALREADY DECLARED";
			assert witnessListXmlID != null : "MISSING xml:id FOR WITNESS LIST";
			assert witnessListXmlID2 != null : "MISSING xml:id FOR WITNESS LIST";

			Object object = objects.get(0);

			FragInter fragInter = null;

			if (object instanceof ManuscriptSource) {
				fragInter = new SourceInter();
				fragInter.setFragment(fragment);
				((SourceInter) fragInter).setSource((ManuscriptSource) object);
			} else if (object instanceof PrintedSource) {
				fragInter = new SourceInter();
				fragInter.setFragment(fragment);
				((SourceInter) fragInter).setSource((PrintedSource) object);
			} else if (object instanceof Edition) {
				fragInter = new EditionInter();
				fragInter.setFragment(fragment);
				((EditionInter) fragInter).setEdition((Edition) object);
			}
			putObjectByXmlID(witnessXmlID, fragInter);
			putObjectByXmlID(witnessListXmlID, fragInter);
			putObjectByXmlID(witnessListXmlID2, fragInter);
		}

	}

	private void loadPrintedSources(Fragment fragment, String fragmentTEIID) {
		String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
		String queryExpression = "/def:teiCorpus/def:TEI"
				+ selectThisFragment
				+ "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:bibl";

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		for (Element bibl : xp.evaluate(doc)) {
			PrintedSource printedSource = new PrintedSource();
			printedSource.setFragment(fragment);

			String biblID = bibl.getAttributeValue("id",
					bibl.getNamespace("xml"));

			assert getObjectsByXmlID(biblID) == null : "xml:id:" + biblID
					+ " IS ALREADY DECLARED";

			putObjectByXmlID(biblID, printedSource);

			printedSource.setTitle(bibl.getChildText("title", namespace));
			printedSource.setPubPlace(bibl.getChildText("pubPlace", namespace));
			printedSource.setIssue(bibl.getChildText("biblScope", namespace));
			printedSource.setDate(new DateTime(bibl.getChildText("date",
					namespace)));
		}

	}

	private void loadSourceManuscripts(Fragment fragment, String fragmentTEIID) {

		String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
		String queryExpression = "/def:teiCorpus/def:TEI"
				+ selectThisFragment
				+ "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:msDesc";

		// Map<String, Object> vars = new HashMap<String, Object>();
		// vars.put("xml:id", null);

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				// "/def:teiCorpus/def:TEI[@xml:id='Fr1']/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:msDesc",
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		// xp.setVariable("fragmentTEIID", fragmentTEIID);

		for (Element msDesc : xp.evaluate(doc)) {
			ManuscriptSource manuscript = new ManuscriptSource();
			manuscript.setFragment(fragment);

			String manuscriptID = msDesc.getAttributeValue("id",
					msDesc.getNamespace("xml"));

			assert getObjectsByXmlID(manuscriptID) == null : "xml:id:"
					+ manuscriptID + " IS ALREADY DECLARED";

			putObjectByXmlID(manuscriptID, manuscript);

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

			String heteronymXmlID = heteronymTEI.getAttributeValue("id",
					heteronymTEI.getNamespace("xml"));

			assert getObjectsByXmlID(heteronymXmlID) == null : "xml:id:"
					+ heteronymXmlID + " IS ALREADY DECLARED";

			putObjectByXmlID(heteronymXmlID, heteronym);

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

			assert getObjectsByXmlID(tononomyID) == null : "xml:id:"
					+ tononomyID + " IS ALREADY DECLARED";

			putObjectByXmlID(tononomyID, taxonomy);

			taxonomy.setName(taxonomyTEI.getChild("bibl", namespace).getText());

			for (Element categoryTEI : taxonomyTEI.getChildren("category",
					namespace)) {
				Category category = new Category();
				category.setTaxonomy(taxonomy);

				String categoryID = categoryTEI.getAttributeValue("id",
						categoryTEI.getNamespace("xml"));

				assert getObjectsByXmlID(categoryID) == null : "xml:id:"
						+ categoryID + " IS ALREADY DECLARED";

				putObjectByXmlID(categoryID, category);

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

		assert getObjectsByXmlID(listEditionsXmlID) == null : "xml:id:"
				+ listEditionsXmlID + " IS ALREADY DECLARED";

		for (Element bibl : corpusHeaderListBibl.getChildren("bibl", namespace)) {
			Edition edition = new Edition();
			edition.setLdoD(ldoD);

			String editionXmlID = bibl.getAttributeValue("id",
					bibl.getNamespace("xml"));

			assert getObjectsByXmlID(editionXmlID) == null : "xml:id:"
					+ editionXmlID + " IS ALREADY DECLARED";

			putObjectByXmlID(editionXmlID, edition);
			putObjectByXmlID(listEditionsXmlID, edition);

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