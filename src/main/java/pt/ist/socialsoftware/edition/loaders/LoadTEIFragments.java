package pt.ist.socialsoftware.edition.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AddText.Place;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.DelText.HowDel;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.EditionInter;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FormatText.Rendition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDText.OpenClose;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceUnit;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VariationPoint;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.visitors.CanonicalCleaner;
import pt.ist.socialsoftware.edition.visitors.EmptyTextCleaner;
import pt.ist.socialsoftware.edition.visitors.GraphConsistencyChecker;
import pt.ist.socialsoftware.edition.visitors.GraphWriter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

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
				throw new LdoDException("identificador não declarado " + xmlId);
			}
			objects.addAll(objects2);
		}
		return objects;
	}

	private void getCorpusXmlIds() {
		for (Edition edition : ldoD.getEditions()) {
			putObjectByXmlID(edition.getXmlId(), edition);
		}

		for (Taxonomy taxonomy : ldoD.getTaxonomies()) {
			putObjectByXmlID(taxonomy.getXmlId(), taxonomy);
			for (Category category : taxonomy.getCategories()) {
				putObjectByXmlID(category.getXmlId(), category);
			}
		}

		for (Heteronym heteronym : ldoD.getHeteronyms()) {
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
			throw new LdoDException("Ficheiro não encontrado");
		} catch (JDOMException e) {
			throw new LdoDException("Ficheiro com problemas de codificação TEI");
		} catch (IOException e) {
			throw new LdoDException("Problemas com o ficheiro, tipo ou formato");
		}

		if (doc == null) {
			LdoDException ex = new LdoDException(
					"Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		ldoDTEI = doc.getRootElement();
		namespace = ldoDTEI.getNamespace();
	}

	public void loadFragments(InputStream file) throws LdoDException {
		parseTEIFile(file);

		ldoD = LdoD.getInstance();

		getCorpusXmlIds();

		XPathExpression<Element> xp = xpfac.compile(
				"/def:teiCorpus/def:TEI/def:teiHeader", Filters.element(),
				null, Namespace.getNamespace("def", namespace.getURI()));

		for (Element fragmentTEI : xp.evaluate(doc)) {
			String fragmentTEIID = fragmentTEI.getParentElement()
					.getAttributeValue("id", fragmentTEI.getNamespace("xml"));

			if (fragmentTEIID == null) {
				throw new LdoDException("falta xml:id de um fragmento");
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
				throw new LdoDException("xml:id:" + fragmentTEIID
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
			endPoint = addReading4Paragraph(startPoint, fragInters,
					OpenClose.OPEN);

			startPoint = endPoint;

			endPoint = loadContent(paragraph, startPoint, fragInters);

			startPoint = endPoint;

			endPoint = addReading4Paragraph(startPoint, fragInters,
					OpenClose.CLOSE);

			startPoint = endPoint;
		}

		postParsing(fragment);
	}

	private VariationPoint addReading4Paragraph(VariationPoint startPoint,
			List<FragInter> pendingFragInterps, OpenClose openClose) {

		VariationPoint endPoint = new VariationPoint();

		ParagraphText text = new ParagraphText();
		text.setOpenClose(openClose);

		Reading.addReading(startPoint, endPoint, pendingFragInterps, text);

		return endPoint;
	}

	private void postParsing(Fragment fragment) {
		EmptyTextCleaner eCleaner = new EmptyTextCleaner();
		fragment.getVariationPoint().accept(eCleaner);

		CanonicalCleaner cCleaner = new CanonicalCleaner();
		fragment.getVariationPoint().accept(cCleaner);

		GraphConsistencyChecker checkConsistency = new GraphConsistencyChecker();
		fragment.getVariationPoint().accept(checkConsistency);

		GraphWriter graphWriter = new GraphWriter();
		fragment.getVariationPoint().accept(graphWriter);
		System.out.println(graphWriter.getResult());

		for (FragInter fragInter : fragment.getFragmentInter()) {
			HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
			fragment.getVariationPoint().accept(writer);
			System.out.println(writer.getTranscription());
		}
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
					endPoint = loadSeg(element2, startPoint, fragInters);
				} else if (element2.getName().equals("subst")) {
					endPoint = loadSubst(element2, startPoint, fragInters);
				} else if (element2.getName().equals("add")) {
					endPoint = loadAdd(element2, startPoint, fragInters);
				} else if (element2.getName().equals("del")) {
					endPoint = loadDel(element2, startPoint, fragInters);
				} else {
					assert false : "DOES NOT HANDLE LOAD OF:" + element2
							+ " OF TYPE:" + element2.getCType().toString();
				}
			}
			startPoint = endPoint;

		}
		return endPoint;
	}

	private VariationPoint loadSubst(Element element,
			VariationPoint startPoint, List<FragInter> fragInters) {

		VariationPoint initialPoint = startPoint;
		Reading initialRdg = null;
		VariationPoint endPoint = null;

		for (Content content : element.getContent()) {
			if (content.getCType() == CType.Text) {
				if (content.getValue().trim() != "") {
					endPoint = startPoint;
				} else {
					throw new LdoDException(
							"elementos inesperado dentro de subst"
									+ content.getValue());

					// assert false : "UNEXPECTED TEXT ELEMENT WITHIN <subst>"
					// + content.getValue();
				}
			} else if (content.getCType() == CType.Comment) {
				// ignore comments
				endPoint = startPoint;
			} else if (content.getCType() == CType.Element) {
				Element element2 = (Element) content;
				if (element2.getName().equals("add")) {
					endPoint = loadAdd(element2, startPoint, fragInters);
				} else if (element2.getName().equals("del")) {
					endPoint = loadDel(element2, startPoint, fragInters);
				} else {
					throw new LdoDException("não carrega elementos: "
							+ element2 + " do tipo:"
							+ element2.getCType().toString()
							+ "dentro de <subst>");

					// assert false : "DOES NOT HANDLE LOAD OF:" + element2
					// + " OF TYPE:" + element2.getCType().toString()
					// + "WITHIN <subst>";
				}
			}

			if (endPoint.getInReadings().get(0).getPreviousVariationPoint() == initialPoint) {
				initialRdg = endPoint.getInReadings().get(0);
			}
			startPoint = endPoint;

		}

		SubstText openSubstText = new SubstText(OpenClose.OPEN);
		initialRdg.addBeginText(openSubstText);

		SubstText closeSubstText = new SubstText(OpenClose.CLOSE);
		Reading endRdg = endPoint.getInReadings().get(0);
		endRdg.addEndText(closeSubstText);

		return endPoint;
	}

	private VariationPoint loadDel(Element element, VariationPoint startPoint,
			List<FragInter> fragInters) {
		List<Content> contentList = element.getContent();

		if (contentList.size() != 1)
			throw new LdoDException(
					"del contém outros elementos para além de texto"
							+ element.getValue());

		assert contentList.size() == 1 : "<del> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		if (contentList.get(0).getCType() != CType.Text)
			throw new LdoDException(
					"del contém outros elementos para além de texto"
							+ element.getValue());

		assert contentList.get(0).getCType() == CType.Text : "<del> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		VariationPoint endPoint = loadSimpleText((Text) contentList.get(0),
				startPoint, fragInters);
		Reading rdg = endPoint.getInReadings().get(0);

		Attribute howDelAttribute = element.getAttribute("rend");

		HowDel how = getHowDelAttribute(howDelAttribute);

		DelText openDelText = new DelText(OpenClose.OPEN, how);

		DelText closeDelText = new DelText(OpenClose.CLOSE, how);

		rdg.addBeginText(openDelText);
		rdg.addEndText(closeDelText);

		return endPoint;

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
				throw new LdoDException(
						"valor desconhecido para atributo rend=" + howDelValue
								+ " dentro de del");
				// assert false : "UNKOWN rend ATTRIBUTE VALUE=" + howDelValue
				// + " FOR <del>";
			}
		}
		return howDel;
	}

	/**
	 * In this project a <add> element can only contain simple text
	 * 
	 * @param element
	 * @param startPoint
	 * @param fragInters
	 */
	private VariationPoint loadAdd(Element element, VariationPoint startPoint,
			List<FragInter> fragInters) {
		List<Content> contentList = element.getContent();

		if (contentList.size() != 1)
			throw new LdoDException(
					"add contém outros elementos para além de texto"
							+ element.getValue());

		assert contentList.size() == 1 : "<add> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		if (contentList.get(0).getCType() != CType.Text)
			throw new LdoDException(
					"add contém outros elementos para além de texto"
							+ element.getValue());

		assert contentList.get(0).getCType() == CType.Text : "<add> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		VariationPoint endPoint = loadSimpleText((Text) contentList.get(0),
				startPoint, fragInters);
		Reading rdg = endPoint.getInReadings().get(0);

		Attribute placeAttribute = element.getAttribute("place");

		Place place = getPlaceAttribute(placeAttribute);

		AddText openAddText = new AddText(OpenClose.OPEN, place);

		AddText closeAddText = new AddText(OpenClose.CLOSE, place);

		rdg.addBeginText(openAddText);
		rdg.addEndText(closeAddText);

		return endPoint;
	}

	private Place getPlaceAttribute(Attribute placeAttribute) {
		Place place = Place.UNSPECIFIED;
		if (placeAttribute != null) {
			String placeValue = placeAttribute.getValue();

			if (placeValue.equals("above")) {
				place = Place.ABOVE;
			} else if (placeValue.equals("superimposed")) {
				place = Place.SUPERIMPOSED;
			} else if (placeValue.equals("margin")) {
				place = Place.MARGIN;
			} else if (placeValue.equals("top")) {
				place = Place.TOP;
			} else if (placeValue.equals("inline")) {
				place = Place.INLINE;
			} else {
				throw new LdoDException(
						"valor desconhecido para atributo place=" + place);
				// assert false : "UNKOWN place ATTRIBUTE VALUE=" + place;
			}
		}

		return place;
	}

	/**
	 * In the this project a <seg> element can only contain simple text, it is
	 * used for formating
	 * 
	 * @param element
	 * @param startPoint
	 * @param fragInters
	 * @return
	 */
	private VariationPoint loadSeg(Element element, VariationPoint startPoint,
			List<FragInter> fragInters) {
		List<Content> contentList = element.getContent();

		if (contentList.size() != 1)
			throw new LdoDException("seg não contém apenas texto" + element);

		assert contentList.size() == 1 : "<seg> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		if (contentList.get(0).getCType() != CType.Text)
			throw new LdoDException("seg não contém apenas texto" + element);

		assert contentList.get(0).getCType() == CType.Text : "<seg> DOES NOT CONTAIN SIMPLE TEXT"
				+ element;

		VariationPoint endPoint = loadSimpleText((Text) contentList.get(0),
				startPoint, fragInters);
		Reading rdg = endPoint.getInReadings().get(0);

		String[] listRendXmlId = null;

		Attribute rendAttribute = element.getAttribute("rendition");

		if (rendAttribute != null) {

			listRendXmlId = element.getAttribute("rendition").getValue()
					.split("\\s+");

			for (int i = listRendXmlId.length - 1; i >= 0; i--) {

				FormatText openFormatText = new FormatText(OpenClose.OPEN);
				FormatText closeFormatText = new FormatText(OpenClose.CLOSE);

				String rendXmlId = listRendXmlId[i].substring(1);

				if (rendXmlId.equals("right")) {
					openFormatText.setRend(Rendition.RIGHT);
					closeFormatText.setRend(Rendition.RIGHT);
				} else if (rendXmlId.equals("left")) {
					openFormatText.setRend(Rendition.LEFT);
					closeFormatText.setRend(Rendition.LEFT);
				} else if (rendXmlId.equals("center")) {
					openFormatText.setRend(Rendition.CENTER);
					closeFormatText.setRend(Rendition.CENTER);
				} else if (rendXmlId.equals("bold")) {
					openFormatText.setRend(Rendition.BOLD);
					closeFormatText.setRend(Rendition.BOLD);
				} else if (rendXmlId.equals("red")) {
					openFormatText.setRend(Rendition.RED);
					closeFormatText.setRend(Rendition.RED);
				} else if (rendXmlId.equals("u")) {
					openFormatText.setRend(Rendition.UNDERLINED);
					closeFormatText.setRend(Rendition.UNDERLINED);
				} else {
					throw new LdoDException("valor desconhecido para rend="
							+ listRendXmlId[i]);
					// assert false : "UNKNOWN rend VALUE" + listRendXmlId[i];
				}

				rdg.addBeginText(openFormatText);
				rdg.addEndText(closeFormatText);
			}
		}

		return endPoint;
	}

	private VariationPoint loadSpace(Element element,
			VariationPoint startPoint, List<FragInter> fragInters) {
		VariationPoint endPoint = new VariationPoint();

		SpaceText.SpaceDim dim = getDimAttribute(element);
		SpaceText.SpaceUnit unit = getUnitAttribute(element);
		int quantity = getQuantityAttribute(element);

		Reading reading = new Reading();
		SpaceText text = new SpaceText(dim, quantity, unit);
		reading.addBeginText(text);

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
				throw new LdoDException(
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
			throw new LdoDException("valor desconhecido para atributo unit="
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
			throw new LdoDException("valor desconhecido para atributo dim="
					+ dimAttribute.getValue());

			// assert false : "UNKNOWN VALUE FOR ATTRIBUTE dim=\""
			// + dimAttribute.getValue() + "\"";
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
				throw new LdoDException("valor inesperado dentro de app"
						+ rdgElement.getName());
				// assert false : "UNEXPECTED ELEMENT NESTED WITHIN APP" +
				// rdgElement.getName();
			}
		}

		List<FragInter> pendingFragInters = new ArrayList<FragInter>(fragInters);
		for (Reading rdg : endPoint.getInReadings()) {
			pendingFragInters.removeAll(rdg.getFragInters());
		}

		if (!pendingFragInters.isEmpty()) {
			addReading4Empty(startPoint, endPoint, pendingFragInters, true);
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

		if (witAttribute == null)
			throw new LdoDException("elemento rdg necessita de atributo wit "
					+ rdgElement);

		assert witAttribute != null : "rdg ELEMENT REQUIRES VALUE FOR ATTRIBUTE wit"
				+ rdgElement;

		String[] listInterXmlId = rdgElement.getAttribute("wit").getValue()
				.split("\\s+");
		List<FragInter> fragInters = getFragItersByListXmlID(listInterXmlId);
		VariationPoint endPoint = new VariationPoint();

		if (rdgElement.getContent().isEmpty()) {
			addReading4Empty(startPoint, endPoint, fragInters, true);
		} else {
			endPoint = loadContent(rdgElement, startPoint, fragInters);
		}
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
		LbText text = new LbText(isBreak(element), isHiphenated(element));
		reading.addBeginText(text);

		for (FragInter fragIter : toFragInters) {
			reading.addFragInters(fragIter);
			pendingFragInterps.remove(fragIter);
		}

		startPoint.addOutReadings(reading);
		endPoint.addInReadings(reading);

		if (!pendingFragInterps.isEmpty()) {
			addReading4Empty(startPoint, endPoint, pendingFragInterps,
					isBreak(element));
		}

		return endPoint;
	}

	private void addReading4Empty(VariationPoint startPoint,
			VariationPoint endPoint, List<FragInter> pendingFragInterps,
			Boolean isBreak) {

		EmptyText text = new EmptyText(isBreak);

		Reading.addReading(startPoint, endPoint, pendingFragInterps, text);
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
			throw new LdoDException(
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
			breakWord = element.getAttributeValue("break");
		}

		Boolean toBreak = false;
		if (breakWord == null || breakWord.equals("yes")) {
			toBreak = true;
		} else if (breakWord.equals("no")) {
			toBreak = false;
		} else {
			throw new LdoDException("valor inesperado para atribute break="
					+ breakWord);
			// assert false : "INVALID PARAMETER FOR break ATTRIBUTE" +
			// breakWord;
		}
		return toBreak;
	}

	private VariationPoint loadSimpleText(Text text, VariationPoint startPoint,
			List<FragInter> fragInters) {
		String value = text.getTextTrim();

		VariationPoint endPoint = new VariationPoint();
		if (value.equals("")) {
			// ignore empty space
			endPoint = startPoint;
		} else {
			Reading reading = new Reading();
			SimpleText simpleText = new SimpleText(value);
			reading.addBeginText(simpleText);

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

			if ((objects == null) || (objects.isEmpty()))
				throw new LdoDException(
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
				throw new LdoDException("elemento wit sem atributo xml:id="
						+ witnessXmlID);
			assert witnessXmlID != null : "MISSING xml:id FOR WITNESS";

			if (getObjectsByXmlID(witnessXmlID) != null)
				throw new LdoDException("já está declarado o atributo xml:id="
						+ witnessXmlID);
			assert getObjectsByXmlID(witnessXmlID) == null : "xml:id:"
					+ witnessXmlID + " IS ALREADY DECLARED";

			if (witnessListXmlID == null)
				throw new LdoDException("falta atributo xml:id para listWit");
			assert witnessListXmlID != null : "MISSING xml:id FOR WITNESS LIST";

			if (witnessListXmlID2 == null)
				throw new LdoDException("falta atributo xml:id para listWit");
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

				Element bibl = witness.getChild("bibl", namespace);

				Heteronym heteronym = getHeteronym(bibl);
				if (heteronym != null)
					fragInter.setHeteronym(heteronym);

				((EditionInter) fragInter).setTitle(bibl.getChildTextTrim(
						"title", namespace));
				fragInter.setDate(bibl.getChildTextTrim("date", namespace));
				((EditionInter) fragInter).setNumber(getBiblScope(bibl,
						"number"));
				((EditionInter) fragInter).setPage(getBiblScope(bibl, "pp"));
				((EditionInter) fragInter).setNotes(getBiblNotes(bibl));
			}
			putObjectByXmlID(witnessXmlID, fragInter);
			putObjectByXmlID(witnessListXmlID, fragInter);
			putObjectByXmlID(witnessListXmlID2, fragInter);
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

	private int getBiblScope(Element bibl, String type) {
		int scope = 0;
		for (Element biblScope : bibl.getChildren("biblScope", namespace)) {
			Attribute typeAtt = biblScope.getAttribute("type");
			if (typeAtt.getValue().equals(type)) {
				String scopeText = biblScope.getTextTrim();
				if (scopeText != "") {
					scope = Integer.parseInt(scopeText);
				}
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
						throw new LdoDException("atributo corresp=" + hetXmlId
								+ " para persName em witness "
								+ bibl.getValue() + " não está declarado");
					}
				} else {
					throw new LdoDException(
							"falta atributo corresp para persName em witness"
									+ bibl.getValue());
				}
			} else {
				throw new LdoDException(
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

			if (getObjectsByXmlID(biblID) != null)
				throw new LdoDException("já está declarado o atributo xml:id="
						+ biblID);
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

			if (getObjectsByXmlID(manuscriptID) != null)
				throw new LdoDException("já está declarado o atributo xml:id="
						+ manuscriptID);

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
		}

	}

}
