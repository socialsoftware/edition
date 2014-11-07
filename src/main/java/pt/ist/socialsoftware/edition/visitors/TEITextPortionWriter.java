/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.controller.LdoDExceptionHandler;
import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AddText.Place;
import pt.ist.socialsoftware.edition.domain.AltText;
import pt.ist.socialsoftware.edition.domain.AltTextWeight;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.DelText.HowDel;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.GapText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.NoteText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.RefText;
import pt.ist.socialsoftware.edition.domain.Rend;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.TextPortion;
import pt.ist.socialsoftware.edition.domain.UnclearText;

/**
 * Produces a TEI representation of the tree representing a fragment text
 * 
 * @author ars
 * 
 */
public class TEITextPortionWriter implements TextTreeVisitor {

	private final static Logger logger = LoggerFactory
			.getLogger(LdoDExceptionHandler.class);

	private String result = "";

	// create the jdom
	// Document jdomDoc = new Document();
	// create root element

	Namespace xmlns;

	// List<String> fragInterSelectedSet = new ArrayList<String>();

	Element rootElement = null;
	Set<FragInter> fragInterSelectedList = null;

	// jdomDoc.setRootElement(rootElement);

	public TEITextPortionWriter(Element rootElement) {
		this.rootElement = rootElement;
		this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
	}

	public TEITextPortionWriter(Element rootElement,
			Set<FragInter> fragInterSelectedList) {
		// TODO Auto-generated constructor stub
		this.rootElement = rootElement;
		this.fragInterSelectedList = fragInterSelectedList;
		this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
	}

	private Element preGenerate(String name) {
		Element newElement = new Element(name, xmlns);
		rootElement.addContent(newElement);
		rootElement = newElement;
		return newElement;
	}

	public String getResult() {
		return result;
	}

	@Override
	public void visit(AppText appText) {
		result = result + "<app>";

		preGenerate("app");

		TextPortion firstChild = appText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (!appText.getType().getDesc().equalsIgnoreCase("UNSPECIFIED")) {
			Attribute typeAtt = new Attribute("type", appText.getType()
					.getDesc());
			rootElement.setAttribute(typeAtt);
		}

		result = result + "</app>";
		rootElement = rootElement.getParentElement();

		if (appText.getParentOfLastText() == null) {
			if (appText.getNextText() != null) {
				appText.getNextText().accept(this);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		result = result + "<rdgGrp>";

		preGenerate("rdgGrp");

		if (rdgGrpText.getType() != null) {
			if (rdgGrpText.getType().getDesc().compareTo("unspecified") != 0) {
				Attribute typeAtt = new Attribute("type", rdgGrpText.getType()
						.getDesc());
				rootElement.setAttribute(typeAtt);
			}
		}

		TextPortion firstChild = rdgGrpText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</rdgGrp>";
		rootElement = rootElement.getParentElement();

		if (rdgGrpText.getParentOfLastText() == null) {
			rdgGrpText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RdgText rdgText) {

		String wit = "";
		boolean selected = false;

		for (FragInter inter : rdgText.getInterps()) {

			if (fragInterSelectedList.contains(inter)) {
				selected = true;
				wit = wit + "#" + inter.getXmlId() + " ";
			}

		}

		if (selected) {

			preGenerate("rdg");

			if (!wit.equals("")) {
				result = result + "<rdg wit=\"" + wit.trim() + "\">";

				Attribute witAtt = new Attribute("wit", wit.trim());
				rootElement.setAttribute(witAtt);

			} else {
				result = result + "<rdg>";
			}

			TextPortion firstChild = rdgText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}

			result = result + "</rdg>";
			rootElement = rootElement.getParentElement();

		}

		if (rdgText.getParentOfLastText() == null) {
			rdgText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		result = result + "<p>";

		preGenerate("p");

		TextPortion firstChild = paragraphText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</p>";
		rootElement = rootElement.getParentElement();

		if (paragraphText.getParentOfLastText() == null) {
			paragraphText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SegText segText) {
		String rendition = "";
		for (Rend rend : segText.getRendSet()) {
			rendition = rendition + "#" + rend.getRend().getDesc() + " ";
		}

		preGenerate("seg");

		if (!rendition.equals("")) {
			result = result + "<seg rendition=\"" + rendition.trim() + "\">";

			Attribute witAtt = new Attribute("rendition", rendition.trim());
			rootElement.setAttribute(witAtt);

		} else {
			result = result + "<seg>";
		}

		if (segText.getXmlId() != null) {
			Attribute ids = new Attribute("id", segText.getXmlId(),
					Namespace.XML_NAMESPACE);
			rootElement.setAttribute(ids);
		}

		TextPortion firstChild = segText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</seg>";
		rootElement = rootElement.getParentElement();

		if (segText.getParentOfLastText() == null) {
			segText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SimpleText simpleText) {
		result = result + simpleText.getValue();

		rootElement.addContent(simpleText.getValue());

		if (simpleText.getParentOfLastText() == null) {
			simpleText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {

		String ed = "";
		boolean selected = false;

		for (FragInter inter : lbText.getInterps()) {
			if (fragInterSelectedList.contains(inter)) {
				ed = ed + "#" + inter.getXmlId() + " ";
				selected = true;
			}
		}

		if (selected) {
			String hyphenated = lbText.getHyphenated() ? "type=\"hyphenated\""
					: "";
			String breakWord = lbText.getBreakWord() ? "break=\"no\"" : "";

			preGenerate("lb");

			Attribute edAtt = new Attribute("ed", ed.trim());
			rootElement.setAttribute(edAtt);

			if (lbText.getHyphenated()) {
				Attribute hyphAtt = new Attribute("type", "hyphenated");
				rootElement.setAttribute(hyphAtt);
			}

			if (!lbText.getBreakWord()) {
				Attribute breakAtt = new Attribute("break", "no");
				rootElement.setAttribute(breakAtt);
			}

			rootElement = rootElement.getParentElement();

			ed = ed.equals("") ? "" : "ed=\"" + ed.trim() + "\"";

			result = result + "<lb " + ed + " " + hyphenated + " " + breakWord
					+ "/>";

		}
		if (lbText.getParentOfLastText() == null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {

		String ed = "";
		String facs = "";
		boolean selected = false;

		for (FragInter inter : pbText.getInterps()) {
			if (fragInterSelectedList.contains(inter)) {
				ed = ed + "#" + inter.getXmlId() + " ";
				selected = true;
			}
		}

		if (selected) {
			preGenerate("pb");

			Attribute edAtt = new Attribute("ed", ed.trim());
			rootElement.setAttribute(edAtt);

			if (pbText.getSurface() != null) {
				facs = pbText.getSurface().getGraphic();
				Attribute facsAtt = new Attribute("facs", facs);
				rootElement.setAttribute(facsAtt);
			}

			result = result + "<pb/>";
			rootElement = rootElement.getParentElement();

		}
		if (pbText.getParentOfLastText() == null) {
			pbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText spaceText) {

		String dim = "dim=\"" + spaceText.getDim().getDesc() + "\"";
		String quantity = "quantity=\"" + spaceText.getQuantity() + "\"";
		String unit = "unit=\"" + spaceText.getUnit().getDesc() + "\"";

		preGenerate("space");

		Attribute dimAtt = new Attribute("dim", spaceText.getDim().getDesc());
		rootElement.setAttribute(dimAtt);

		Attribute quantityAtt = new Attribute("quantity",
				Integer.toString(spaceText.getQuantity()));
		rootElement.setAttribute(quantityAtt);

		Attribute unitAtt = new Attribute("unit", spaceText.getUnit().getDesc());
		rootElement.setAttribute(unitAtt);

		result = result + "<space " + dim + " " + quantity + " " + unit + "/>";
		rootElement = rootElement.getParentElement();

		if (spaceText.getParentOfLastText() == null) {
			spaceText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		String place = addText.getPlace() != Place.UNSPECIFIED ? "place=\""
				+ addText.getPlace().getDesc() + "\"" : "";

		preGenerate("add");
		result = result + "<add " + place + ">";

		if (addText.getPlace() != Place.UNSPECIFIED) {
			Attribute placeAtt = new Attribute("place", addText.getPlace()
					.getDesc());
			rootElement.setAttribute(placeAtt);
		}

		TextPortion firstChild = addText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</add>";
		rootElement = rootElement.getParentElement();

		if (addText.getParentOfLastText() == null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		String rend = delText.getHow() != HowDel.UNSPECIFIED ? "rend=\""
				+ delText.getHow().getDesc() + "\"" : "";

		preGenerate("del");
		result = result + "<del " + rend + ">";

		if (delText.getHow() != HowDel.UNSPECIFIED) {
			Attribute delAtt = new Attribute("rend", delText.getHow().getDesc());
			rootElement.setAttribute(delAtt);
		}

		TextPortion firstChild = delText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</del>";
		rootElement = rootElement.getParentElement();

		if (delText.getParentOfLastText() == null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {

		preGenerate("subst");
		result = result + "<subst>";

		TextPortion firstChild = substText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</subst>";
		rootElement = rootElement.getParentElement();

		if (substText.getParentOfLastText() == null) {
			substText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(GapText gapText) {

		preGenerate("gap");

		result = result + "<gap reason=\"" + gapText.getReason().getDesc()
				+ "\" extent=\"" + gapText.getExtent() + "\" unit=\""
				+ gapText.getUnit().getDesc() + "\"/>";

		Attribute reasonAtt = new Attribute("reason", gapText.getReason()
				.getDesc());
		Attribute extentAtt = new Attribute("extent", String.valueOf(gapText
				.getExtent()));
		Attribute unitAtt = new Attribute("unit", gapText.getUnit().getDesc());

		rootElement.setAttribute(reasonAtt);
		rootElement.setAttribute(extentAtt);
		rootElement.setAttribute(unitAtt);

		rootElement = rootElement.getParentElement();

		if (gapText.getParentOfLastText() == null) {
			gapText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(UnclearText unclearText) {

		preGenerate("unclear");
		result = result + "<unclear reason=\">"
				+ unclearText.getReason().getDesc() + "\">";

		Attribute reasonAtt = new Attribute("reason", unclearText.getReason()
				.getDesc());
		rootElement.setAttribute(reasonAtt);

		TextPortion firstChild = unclearText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</unclear>";
		rootElement = rootElement.getParentElement();

		if (unclearText.getParentOfLastText() == null) {
			unclearText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AltText altText) {
		String valueTarget = "";
		String valueWeight = "";

		for (AltTextWeight weight : altText.getAltTextWeightSet()) {
			valueTarget = valueTarget + "#" + weight.getSegText().getXmlId()
					+ " ";
			valueWeight = valueWeight + " " + weight.getWeight();
		}

		valueTarget = valueTarget.trim();
		valueWeight = valueWeight.trim();

		preGenerate("alt");
		result = result + "<alt target=\"" + valueTarget + "\"" + " mode=\""
				+ altText.getMode().getDesc() + "\" weights=\"" + valueWeight
				+ "\"/>";

		Attribute targetAtt = new Attribute("target", valueTarget);
		Attribute modeAtt = new Attribute("mode", altText.getMode().getDesc());
		Attribute weightsAtt = new Attribute("weights", valueWeight);

		rootElement.setAttribute(targetAtt);
		rootElement.setAttribute(modeAtt);
		rootElement.setAttribute(weightsAtt);

		rootElement = rootElement.getParentElement();

		if (altText.getParentOfLastText() == null) {
			altText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(NoteText noteText) {

		preGenerate("note");

		result = result + "<note type=\"" + noteText.getType().getDesc()
				+ "\">";

		Attribute typeAtt = new Attribute("type", noteText.getType().getDesc());
		rootElement.setAttribute(typeAtt);

		TextPortion firstChild = noteText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</note>";
		rootElement = rootElement.getParentElement();

		if (noteText.getParentOfLastText() == null) {
			noteText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RefText refText) {

		preGenerate("ref");

		result = result + "<note type=\"" + refText.getType().getDesc() + "\" "
				+ "target=\"#" + refText.getTarget() + "\">";

		Attribute typeAtt = new Attribute("type", refText.getType().getDesc());
		Attribute targetAtt = new Attribute("target", "#" + refText.getTarget());

		rootElement.setAttribute(typeAtt);
		rootElement.setAttribute(targetAtt);

		TextPortion firstChild = refText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</ref>";
		rootElement = rootElement.getParentElement();

		if (refText.getParentOfLastText() == null) {
			refText.getNextText().accept(this);
		}
	}

}
