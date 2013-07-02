package pt.ist.socialsoftware.edition.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AddText.Place;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.DelText.HowDel;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.Rend;
import pt.ist.socialsoftware.edition.domain.Rend.Rendition;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceUnit;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.TextPortion;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

public class LoadTEIFragments {

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
			List<Object> objects2 = getObjectsByXmlID(xmlId.substring(1));
			if (objects2 == null) {
				throw new LdoDLoadException("identificador não declarado "
						+ xmlId);
			}
			objects.addAll(objects2);
		}
		return objects;
	}

	private Set<FragInter> getFragItersByListXmlID(String[] listInterXmlId) {
		List<Object> objects = getObjectsByListXmlID(listInterXmlId);
		Set<FragInter> fragIters = new HashSet<FragInter>();
		for (Object object : objects) {
			fragIters.add((FragInter) object);
		}
		return fragIters;
	}

	private void getCorpusXmlIds() {
		for (ExpertEdition edition : ldoD.getExpertEditionsSet()) {
			putObjectByXmlID(edition.getXmlId(), edition);
		}

		for (Taxonomy taxonomy : ldoD.getTaxonomiesSet()) {
			putObjectByXmlID(taxonomy.getXmlId(), taxonomy);
			for (Category category : taxonomy.getCategoriesSet()) {
				putObjectByXmlID(category.getXmlId(), category);
			}
		}

		for (Heteronym heteronym : ldoD.getHeteronymsSet()) {
			putObjectByXmlID(heteronym.getXmlId(), heteronym);
		}
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
			throw new LdoDLoadException(
					"Ficheiro com problemas de codificação TEI");
		} catch (IOException e) {
			throw new LdoDLoadException(
					"Problemas com o ficheiro, tipo ou formato");
		}

		if (doc == null) {
			LdoDLoadException ex = new LdoDLoadException(
					"Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		ldoDTEI = doc.getRootElement();
		namespace = ldoDTEI.getNamespace();
	}

	public void loadFragments(InputStream file) throws LdoDLoadException {
		parseTEIFile(file);

		ldoD = LdoD.getInstance();

		getCorpusXmlIds();

		XPathExpression<Element> xp = xpfac.compile("//def:TEI/def:teiHeader",
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		for (Element fragmentTEI : xp.evaluate(doc)) {
			String fragmentTEIID = fragmentTEI.getParentElement()
					.getAttributeValue("id", fragmentTEI.getNamespace("xml"));

			if (fragmentTEIID == null) {
				throw new LdoDLoadException("falta xml:id de um fragmento");
			}

			assert fragmentTEIID != null : "MISSING xml:id ATTRIBUTE IN FRAGMENT <TEI > ELEMENT";

			String fragmentTEITitle = fragmentTEI
					.getChild("fileDesc", namespace)
					.getChild("titleStmt", namespace)
					.getChildText("title", namespace);

			Fragment fragment = new Fragment();
			fragment.setLdoD(ldoD);
			fragment.setTitle(fragmentTEITitle);

			if (getObjectsByXmlID(fragmentTEIID) != null) {
				throw new LdoDLoadException("xml:id:" + fragmentTEIID
						+ " já está declarado");
			}

			assert getObjectsByXmlID(fragmentTEIID) == null : "xml:id:"
					+ fragmentTEIID + " IS ALREADY DECLARED";

			putObjectByXmlID(fragmentTEIID, fragment);
			fragment.setXmlId(fragmentTEIID);

			loadSourceManuscripts(fragment, fragmentTEIID);
			loadPrintedSources(fragment, fragmentTEIID);
			loadWitnesses(fragment, fragmentTEIID);

			loadFragmentText(fragment, fragmentTEIID);

		}
	}

	private void loadFragmentText(Fragment fragment, String fragmentXmlID) {
		String selectThisFragment = "[@xml:id='" + fragmentXmlID + "']";
		String queryExpression = "//def:TEI" + selectThisFragment
				+ "/def:text/def:body";

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		Set<FragInter> fragInters = fragment.getFragmentInterSet();

		AppText app = new AppText();
		app.setFragment(fragment);
		RdgText rdg = new RdgText(app, fragInters);

		Element element = xp.evaluate(doc).get(0);

		loadElement(element, rdg);
	}

	private void loadElement(Element element, TextPortion parent) {
		for (Content content : element.getContent()) {
			if (content.getCType() == CType.Text) {
				loadSimpleText((Text) content, parent);
			} else if (content.getCType() == CType.Comment) {
				// ignore comments
			} else if (content.getCType() == CType.Element) {
				Element element2 = (Element) content;
				if (element2.getName().equals("div")) {
					loadDiv(element2, parent);
				} else if (element2.getName().equals("p")) {
					loadParagraph(element2, parent);
				} else if (element2.getName().equals("lb")) {
					loadLb(element2, parent);
				} else if (element2.getName().equals("pb")) {
					loadPb(element2, parent);
				} else if (element2.getName().equals("app")) {
					loadApp(element2, parent);
				} else if (element2.getName().equals("space")) {
					loadSpace(element2, parent);
				} else if (element2.getName().equals("seg")) {
					loadSeg(element2, parent);
				} else if (element2.getName().equals("add")) {
					loadAdd(element2, parent);
				} else if (element2.getName().equals("del")) {
					loadDel(element2, parent);
				} else if (element2.getName().equals("subst")) {
					loadSubst(element2, parent);
				} else {
					assert false : "DOES NOT HANDLE LOAD OF:" + element2
							+ " OF TYPE:" + element2.getCType().toString();
				}
			}
		}
	}

	private void loadSubst(Element element, TextPortion parent) {
		SubstText substText = new SubstText(parent);

		for (Content content : element.getContent()) {
			if (content.getCType() == CType.Text) {
				if (content.getValue().trim() != "") {
					// empty text
				} else {
					throw new LdoDLoadException(
							"elementos inesperado dentro de subst"
									+ content.getValue());
				}
			} else if (content.getCType() == CType.Comment) {
				// ignore comments
			} else if (content.getCType() == CType.Element) {
				Element element2 = (Element) content;
				if (element2.getName().equals("add")) {
					loadAdd(element2, substText);
				} else if (element2.getName().equals("del")) {
					loadDel(element2, substText);
				} else {
					throw new LdoDLoadException("não carrega elementos: "
							+ element2 + " do tipo:"
							+ element2.getCType().toString()
							+ "dentro de <subst>");
				}
			}
		}
	}

	private void loadDel(Element element, TextPortion parent) {
		List<Content> contentList = element.getContent();

		if (contentList.size() != 1)
			throw new LdoDLoadException(
					"del contém outros elementos para além de texto"
							+ element.getValue());

		if (contentList.get(0).getCType() != CType.Text)
			throw new LdoDLoadException(
					"del contém outros elementos para além de texto"
							+ element.getValue());

		Attribute howDelAttribute = element.getAttribute("rend");
		HowDel how = getHowDelAttribute(howDelAttribute);

		DelText delText = new DelText(parent, how);

		loadSimpleText((Text) contentList.get(0), delText);
	}

	private void loadAdd(Element element, TextPortion parent) {
		List<Content> contentList = element.getContent();

		if (contentList.size() != 1)
			throw new LdoDLoadException(
					"add contém outros elementos para além de texto"
							+ element.getValue());

		if (contentList.get(0).getCType() != CType.Text)
			throw new LdoDLoadException(
					"add contém outros elementos para além de texto"
							+ element.getValue());

		Attribute placeAttribute = element.getAttribute("place");
		Place place = getPlaceAttribute(placeAttribute);

		AddText addText = new AddText(parent, place);

		loadSimpleText((Text) contentList.get(0), addText);
	}

	/**
	 * In the this project a <seg> element can only contain simple text, it is
	 * used for formating and cross referencing
	 * 
	 */
	private void loadSeg(Element element, TextPortion parent) {
		List<Content> contentList = element.getContent();

		if (contentList.size() != 1)
			throw new LdoDLoadException("seg não contém apenas texto" + element);

		assert contentList.size() == 1 : "<seg> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		if (contentList.get(0).getCType() != CType.Text)
			throw new LdoDLoadException("seg não contém apenas texto" + element);

		assert contentList.get(0).getCType() == CType.Text : "<seg> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		SegText segText = new SegText(parent);

		setRenditions(element, segText);

		loadSimpleText((Text) contentList.get(0), segText);
	}

	private void loadSpace(Element element, TextPortion parent) {
		SpaceText.SpaceDim dim = getDimAttribute(element);
		SpaceText.SpaceUnit unit = getUnitAttribute(element);
		int quantity = getQuantityAttribute(element);

		new SpaceText(parent, dim, quantity, unit);
	}

	private void loadRdgGrp(Element rdgGrpElement, TextPortion parent) {
		RdgGrpText rdgGrpText = new RdgGrpText(parent);
		for (Element rdgElement : rdgGrpElement.getChildren()) {
			if (rdgElement.getName().equals("rdg")) {
				loadRdg(rdgElement, rdgGrpText);
			} else if (rdgElement.getName().equals("rdgGrp")) {
				loadRdgGrp(rdgElement, rdgGrpText);
			}
		}
	}

	private void loadRdg(Element rdgElement, TextPortion parent) {
		Attribute witAttribute = rdgElement.getAttribute("wit");

		if (witAttribute == null)
			throw new LdoDLoadException(
					"elemento rdg necessita de atributo wit " + rdgElement);

		String witValue = rdgElement.getAttribute("wit").getValue().trim();

		String[] listInterXmlId = witValue.split("\\s+");
		Set<FragInter> fragInters = getFragItersByListXmlID(listInterXmlId);

		RdgText rdgText = new RdgText(parent, fragInters);

		if (!rdgElement.getContent().isEmpty()) {
			loadElement(rdgElement, rdgText);
		}
	}

	private void loadApp(Element appElement, TextPortion parent) {
		AppText app = new AppText(parent);

		for (Element rdgElement : appElement.getChildren()) {
			if (rdgElement.getName().equals("rdg")) {
				loadRdg(rdgElement, app);
			} else if (rdgElement.getName().equals("rdgGrp")) {
				loadRdgGrp(rdgElement, app);
			} else {
				throw new LdoDLoadException("elemento inesperado dentro de app"
						+ rdgElement.getName());
				// assert false : "UNEXPECTED ELEMENT NESTED WITHIN APP" +
				// rdgElement.getName();
			}
		}
	}

	private void loadPb(Element element, TextPortion parent) {
		Set<FragInter> toFragInters = null;

		Attribute edAttribute = element.getAttribute("ed");
		if (edAttribute == null) {
			toFragInters = parent.getInterps();
		} else {
			String[] listInterXmlId = element.getAttribute("ed").getValue()
					.split("\\s+");
			toFragInters = getFragItersByListXmlID(listInterXmlId);

			for (FragInter inter : toFragInters) {
				if (!parent.getInterps().contains(inter)) {
					throw new LdoDLoadException(
							"testemunho com identificador:"
									+ inter.getXmlId()
									+ " é associado a pb mas não está declarado no contexto do seu rdg");
				}
			}
		}

		new PbText(parent, toFragInters);
	}

	private void loadLb(Element element, TextPortion parent) {
		Set<FragInter> toFragInters = null;

		Attribute edAttribute = element.getAttribute("ed");
		if (edAttribute == null) {
			toFragInters = parent.getInterps();
		} else {
			String[] listInterXmlId = element.getAttribute("ed").getValue()
					.split("\\s+");
			toFragInters = getFragItersByListXmlID(listInterXmlId);

			for (FragInter inter : toFragInters) {
				if (!parent.getInterps().contains(inter)) {
					throw new LdoDLoadException(
							"testemunho com identificador:"
									+ inter.getXmlId()
									+ " é associado a lb mas não está declarado no contexto do seu rdg");
				}
			}
		}

		new LbText(parent, isBreak(element), isHiphenated(element),
				toFragInters);
	}

	private void loadSimpleText(Text text, TextPortion parent) {
		String value = text.getTextTrim();

		if (value.equals("")) {
			// ignore empty space
		} else {
			new SimpleText(parent, value);
		}
	}

	private void loadParagraph(Element paragraph, TextPortion parent) {
		ParagraphText paragraphText = new ParagraphText(parent);

		loadElement(paragraph, paragraphText);
	}

	private void loadDiv(Element div, TextPortion parent) {
		for (Element element : div.getChildren()) {
			loadElement(element, parent);
		}
	}

	// TODO: a cleaner way to read parent's xmlID
	private void loadWitnesses(Fragment fragment, String fragmentTEIID) {
		String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
		String queryExpression = "//def:TEI"
				+ selectThisFragment
				+ "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listWit/.//def:witness";

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		for (Element witness : xp.evaluate(doc)) {
			String sourceOrEditionXmlID = witness.getChild("ref", namespace)
					.getAttributeValue("target").substring(1);

			List<Object> objects = getObjectsByXmlID(sourceOrEditionXmlID);

			if ((objects == null) || (objects.isEmpty()))
				throw new LdoDLoadException(
						"não existe uma fonte declarada para o atributo xml:id="
								+ sourceOrEditionXmlID);

			assert (objects != null) && (!objects.isEmpty()) : "MISSING SOURCE OBJECT FOR xml:id:"
					+ sourceOrEditionXmlID;

			String witnessXmlID = witness.getAttributeValue("id",
					witness.getNamespace("xml"));
			String witnessListXmlID = witness.getParentElement()
					.getAttributeValue("id", witness.getNamespace("xml"));
			String witnessListXmlID2 = witness.getParentElement()
					.getParentElement()
					.getAttributeValue("id", witness.getNamespace("xml"));

			if (witnessXmlID == null)
				throw new LdoDLoadException("elemento wit sem atributo xml:id="
						+ witnessXmlID);
			assert witnessXmlID != null : "MISSING xml:id FOR WITNESS";

			if (getObjectsByXmlID(witnessXmlID) != null)
				throw new LdoDLoadException(
						"já está declarado o atributo xml:id=" + witnessXmlID);
			assert getObjectsByXmlID(witnessXmlID) == null : "xml:id:"
					+ witnessXmlID + " IS ALREADY DECLARED";

			if (witnessListXmlID == null)
				throw new LdoDLoadException(
						"falta atributo xml:id para listWit");
			assert witnessListXmlID != null : "MISSING xml:id FOR WITNESS LIST";

			if (witnessListXmlID2 == null)
				throw new LdoDLoadException(
						"falta atributo xml:id para listWit");
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
			} else if (object instanceof ExpertEdition) {
				fragInter = new ExpertEditionInter();
				fragInter.setFragment(fragment);
				((ExpertEditionInter) fragInter)
						.setExpertEdition((ExpertEdition) object);

				Element bibl = witness.getChild("bibl", namespace);

				Heteronym heteronym = getHeteronym(bibl);
				if (heteronym != null)
					fragInter.setHeteronym(heteronym);

				((ExpertEditionInter) fragInter).setTitle(bibl
						.getChildTextTrim("title", namespace));
				fragInter.setDate(bibl.getChildTextTrim("date", namespace));

				loadFragmentNumberInWitness(fragInter, bibl);

				((ExpertEditionInter) fragInter).setVolume(getBiblScope(bibl,
						"vol"));

				loadFragmentsPagesInWitness(fragInter, bibl);

				((ExpertEditionInter) fragInter).setNotes(getBiblNotes(bibl));
			}
			fragInter.setXmlId(witnessXmlID);

			putObjectByXmlID(witnessXmlID, fragInter);
			putObjectByXmlID(witnessListXmlID, fragInter);
			putObjectByXmlID(witnessListXmlID2, fragInter);
		}

	}

	private void loadFragmentsPagesInWitness(FragInter fragInter, Element bibl) {
		String pp = getBiblScope(bibl, "pp");
		if (pp != "") {
			((ExpertEditionInter) fragInter).setStartPage(Integer.parseInt(pp));
			((ExpertEditionInter) fragInter).setEndPage(Integer.parseInt(pp));
		} else {
			for (Element biblScope : bibl.getChildren("biblScope", namespace)) {
				Attribute unitAtt = biblScope.getAttribute("unit");
				if (unitAtt.getValue().equals("pp")) {
					String from = biblScope.getAttributeValue("from");
					String to = biblScope.getAttributeValue("to");

					((ExpertEditionInter) fragInter).setStartPage(Integer
							.parseInt(from));
					((ExpertEditionInter) fragInter).setEndPage(Integer
							.parseInt(to));
				}
			}
		}
	}

	private void loadFragmentNumberInWitness(FragInter fragInter, Element bibl) {
		String ppString = getBiblScope(bibl, "number");
		if (ppString != "") {
			((ExpertEditionInter) fragInter).setNumber(Integer
					.parseInt(ppString));
		} else {
			((ExpertEditionInter) fragInter).setNumber(0);
		}
	}

	private String getBiblNotes(Element bibl) {
		String notes = "";
		List<Element> notesList = bibl.getChildren("note", namespace);
		for (Element noteElement : notesList) {
			notes = notes + noteElement.getTextTrim() + ";";
		}
		return notes;
	}

	private String getBiblScope(Element bibl, String unit) {
		String scope = "";
		for (Element biblScope : bibl.getChildren("biblScope", namespace)) {
			Attribute unitAtt = biblScope.getAttribute("unit");
			if (unitAtt == null) {
				throw new LdoDLoadException(
						"elemento biblScope sem atributo unit");
			}
			if (unitAtt.getValue().equals(unit)) {
				scope = biblScope.getTextTrim();
				return scope;
			}
		}
		return scope;
	}

	private Heteronym getHeteronym(Element bibl) {
		Heteronym heteronym;
		String hetXmlId = null;
		Element respStmt = bibl.getChild("respStmt", namespace);
		if (respStmt != null) {
			Element persName = respStmt.getChild("persName", namespace);
			if (persName != null) {
				Attribute correspAtt = persName.getAttribute("corresp");
				if (correspAtt != null) {
					hetXmlId = persName.getAttributeValue("corresp").substring(
							1);

					List<Object> heteronymList = getObjectsByXmlID(hetXmlId);
					if (heteronymList != null) {
						heteronym = (Heteronym) heteronymList.get(0);
					} else {
						throw new LdoDLoadException("atributo corresp="
								+ hetXmlId + " para persName em witness "
								+ bibl.getValue() + " não está declarado");
					}
				} else {
					throw new LdoDLoadException(
							"falta atributo corresp para persName em witness"
									+ bibl.getValue());
				}
			} else {
				throw new LdoDLoadException(
						"falta atributo element persName em witness respStmt"
								+ bibl.getValue());
			}
		} else {
			heteronym = null;
		}
		return heteronym;
	}

	private void loadPrintedSources(Fragment fragment, String fragmentTEIID) {
		String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
		String queryExpression = "//def:TEI"
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

			if (getObjectsByXmlID(biblID) != null)
				throw new LdoDLoadException(
						"já está declarado o atributo xml:id=" + biblID);
			assert getObjectsByXmlID(biblID) == null : "xml:id:" + biblID
					+ " IS ALREADY DECLARED";

			putObjectByXmlID(biblID, printedSource);

			printedSource.setXmlId(biblID);

			printedSource.setTitle(bibl.getChildText("title", namespace));
			printedSource.setPubPlace(bibl.getChildText("pubPlace", namespace));
			printedSource.setIssue(bibl.getChildText("biblScope", namespace));
			printedSource.setDate(bibl.getChildText("date", namespace));
		}

	}

	private void loadSourceManuscripts(Fragment fragment, String fragmentTEIID) {

		String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
		String queryExpression = "//def:TEI"
				+ selectThisFragment
				+ "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:msDesc";

		XPathExpression<Element> xp = xpfac.compile(queryExpression,
				Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));

		for (Element msDesc : xp.evaluate(doc)) {
			ManuscriptSource manuscript = new ManuscriptSource();
			manuscript.setFragment(fragment);

			String manuscriptID = msDesc.getAttributeValue("id",
					msDesc.getNamespace("xml"));

			if (getObjectsByXmlID(manuscriptID) != null)
				throw new LdoDLoadException(
						"já está declarado o atributo xml:id=" + manuscriptID);

			assert getObjectsByXmlID(manuscriptID) == null : "xml:id:"
					+ manuscriptID + " IS ALREADY DECLARED";

			putObjectByXmlID(manuscriptID, manuscript);

			manuscript.setXmlId(manuscriptID);

			Element msId = msDesc.getChild("msIdentifier", namespace);
			manuscript
					.setSettlement(msId.getChildText("settlement", namespace));
			manuscript
					.setRepository(msId.getChildText("repository", namespace));
			manuscript.setIdno(msId.getChildText("idno", namespace));

			Element altElement = msId.getChild("altIdentifier", namespace);

			if (altElement == null) {
				throw new LdoDLoadException(
						"falta declarar altIdentifier de um msIdentifier");
			}

			manuscript.setAltIdentifier(altElement.getChildText("idno",
					namespace));

			Element physDesc = msDesc.getChild("physDesc", namespace);

			Element objectDesc = physDesc.getChild("objectDesc", namespace);
			if (objectDesc.getAttributeValue("form").equals("leaf")) {
				manuscript.setForm(ManuscriptSource.Form.LEAF);
			} else {
				throw new LdoDLoadException(
						"não está definido o valor do atributo form="
								+ objectDesc.getAttributeValue("form"));
			}

			Element supportDesc = objectDesc.getChild("supportDesc", namespace);
			if (supportDesc.getAttributeValue("material").equals("paper")) {
				manuscript.setMaterial(ManuscriptSource.Material.PAPER);
			} else {
				throw new LdoDLoadException(
						"não está definido o valor do atributo material="
								+ objectDesc.getAttributeValue("material"));
			}

			Element layoutElement = objectDesc
					.getChild("layoutDesc", namespace).getChild("layout",
							namespace);
			manuscript.setColumns(Integer.parseInt(layoutElement
					.getAttributeValue("columns")));

			Element handDesc = physDesc.getChild("handDesc", namespace)
					.getChild("p", namespace);
			Element additions = physDesc.getChild("additions", namespace);
			Element binding = physDesc.getChild("bindingDesc", namespace)
					.getChild("binding", namespace).getChild("p", namespace);

			manuscript.setNotes(handDesc.getTextTrim() + ", "
					+ additions.getTextTrim() + ", " + binding.getTextTrim());
		}
	}

	private Boolean isHiphenated(Element element) {
		String hyphenated = null;
		Attribute hyphenatedAttribute = element.getAttribute("type");
		if (hyphenatedAttribute != null) {
			hyphenated = hyphenatedAttribute.getValue();
		}

		Boolean toHyphenate = false;
		if (hyphenated == null) {
			toHyphenate = false;
		} else if (hyphenated.equals("hyphenated")) {
			toHyphenate = true;
		} else {
			throw new LdoDLoadException(
					"valor inesperado para atribute hyphenated=" + hyphenated);

			// assert false : "UNEXPECTED PARAMETER FOR hyphenated ATTRIBUTE"
			// + hyphenated;
		}
		return toHyphenate;
	}

	private Boolean isBreak(Element element) {
		String breakWord = "yes";
		Attribute breakAttribute = element.getAttribute("break");
		if (breakAttribute != null) {
			breakWord = breakAttribute.getValue();
		}

		Boolean toBreak = false;
		if (breakWord == null || breakWord.equals("yes")) {
			toBreak = true;
		} else if (breakWord.equals("no")) {
			toBreak = false;
		} else {
			throw new LdoDLoadException("valor inesperado para atribute break="
					+ breakWord);
			// assert false : "INVALID PARAMETER FOR break ATTRIBUTE" +
			// breakWord;
		}
		return toBreak;
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
				throw new LdoDLoadException(
						"valor para atributo quantity não é um número="
								+ quantityAttribute.getValue());
				// assert false :
				// "VALUE FOR ATTRIBUTE quantity IS NOT INTEGER=\""
				// + quantityAttribute.getValue() + "\"";
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
			throw new LdoDLoadException(
					"valor desconhecido para atributo unit="
							+ unitAttribute.getValue());
			// assert false : "UNKNOWN VALUE FOR ATTRIBUTE unit=\""
			// + unitAttribute.getValue() + "\"";
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
			throw new LdoDLoadException("valor desconhecido para atributo dim="
					+ dimAttribute.getValue());

			// assert false : "UNKNOWN VALUE FOR ATTRIBUTE dim=\""
			// + dimAttribute.getValue() + "\"";
		}
		return dim;
	}

	private void setRenditions(Element element, SegText segText) {
		String[] listRendXmlId = null;

		Attribute rendAttribute = element.getAttribute("rendition");

		if (rendAttribute != null) {
			listRendXmlId = element.getAttribute("rendition").getValue()
					.split("\\s+");

			for (int i = listRendXmlId.length - 1; i >= 0; i--) {
				String rendXmlId = listRendXmlId[i].substring(1);

				if (rendXmlId.equals("right")) {
					segText.addRend(new Rend(Rendition.RIGHT));
				} else if (rendXmlId.equals("left")) {
					segText.addRend(new Rend(Rendition.LEFT));
				} else if (rendXmlId.equals("center")) {
					segText.addRend(new Rend(Rendition.CENTER));
				} else if (rendXmlId.equals("bold")) {
					segText.addRend(new Rend(Rendition.BOLD));
				} else if (rendXmlId.equals("i")) {
					segText.addRend(new Rend(Rendition.ITALIC));
				} else if (rendXmlId.equals("red")) {
					segText.addRend(new Rend(Rendition.RED));
				} else if (rendXmlId.equals("u")) {
					segText.addRend(new Rend(Rendition.UNDERLINED));
				} else {
					throw new LdoDLoadException("valor desconhecido para rend="
							+ listRendXmlId[i]);
					// assert false : "UNKNOWN rend VALUE" + listRendXmlId[i];
				}
			}
		}
	}

	private Place getPlaceAttribute(Attribute placeAttribute) {
		Place place = Place.UNSPECIFIED;
		if (placeAttribute != null) {
			String placeValue = placeAttribute.getValue();

			if (placeValue.equals("above")) {
				place = Place.ABOVE;
			} else if (placeValue.equals("below")) {
				place = Place.BELOW;
			} else if (placeValue.equals("superimposed")) {
				place = Place.SUPERIMPOSED;
			} else if (placeValue.equals("margin")) {
				place = Place.MARGIN;
			} else if (placeValue.equals("top")) {
				place = Place.TOP;
			} else if (placeValue.equals("bottom")) {
				place = Place.BOTTOM;
			} else if (placeValue.equals("inline")) {
				place = Place.INLINE;
			} else if (placeValue.equals("opposite")) {
				place = Place.OPPOSITE;
			} else if (placeValue.equals("end")) {
				place = Place.END;
			} else {
				throw new LdoDLoadException(
						"valor desconhecido para atributo place=" + placeValue);
			}
		}
		return place;
	}

	private HowDel getHowDelAttribute(Attribute howDelAttribute) {
		HowDel howDel = HowDel.UNSPECIFIED;
		if (howDelAttribute != null) {
			String howDelValue = howDelAttribute.getValue();

			if (howDelValue.equals("overtyped")) {
				howDel = HowDel.OVERTYPED;
			} else if (howDelValue.equals("overstrike")) {
				howDel = HowDel.OVERSTRIKE;
			} else if (howDelValue.equals("overwritten")) {
				howDel = HowDel.OVERWRITTEN;
			} else {
				throw new LdoDLoadException(
						"valor desconhecido para atributo rend=" + howDelValue
								+ " dentro de del");
			}
		}
		return howDel;
	}

}
