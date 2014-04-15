/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

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
public class TEIWriter implements TextTreeVisitor {

	private String result = "";

	public String getResult() {
		return result;
	}

	@Override
	public void visit(AppText appText) {
		result = result + "<app>";

		TextPortion firstChild = appText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</app>";

		if (appText.getParentOfLastText() == null) {
			if (appText.getNextText() != null) {
				appText.getNextText().accept(this);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		result = result + "<rdgGrp>";

		TextPortion firstChild = rdgGrpText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</rdgGrp>";

		if (rdgGrpText.getParentOfLastText() == null) {
			rdgGrpText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RdgText rdgText) {
		String wit = "";
		for (FragInter inter : rdgText.getInterps()) {
			wit = wit + "#" + inter.getXmlId() + " ";
		}

		if (!wit.equals("")) {
			result = result + "<rdg wit=\"" + wit.trim() + "\">";
		} else {
			result = result + "<rdg>";
		}

		TextPortion firstChild = rdgText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</rdg>";

		if (rdgText.getParentOfLastText() == null) {
			rdgText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		result = result + "<p>";

		TextPortion firstChild = paragraphText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</p>";

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

		if (!rendition.equals("")) {
			result = result + "<seg rendition=\"" + rendition.trim() + "\">";
		} else {
			result = result + "<seg>";
		}

		TextPortion firstChild = segText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</seg>";

		if (segText.getParentOfLastText() == null) {
			segText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SimpleText simpleText) {
		result = result + simpleText.getValue();

		if (simpleText.getParentOfLastText() == null) {
			simpleText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		String hyphenated = lbText.getHyphenated() ? "type=\"hyphenated\"" : "";
		String breakWord = lbText.getBreakWord() ? "break=\"no\"" : "";
		String ed = "";
		for (FragInter inter : lbText.getInterps()) {
			ed = ed + "#" + inter.getXmlId() + " ";
		}

		ed = ed.equals("") ? "" : "ed=\"" + ed.trim() + "\"";

		result = result + "<lb " + ed + " " + hyphenated + " " + breakWord
				+ "/>";

		if (lbText.getParentOfLastText() == null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {
		result = result + "<pb/>";

		if (pbText.getParentOfLastText() == null) {
			pbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText spaceText) {
		String dim = "dim=\"" + spaceText.getDim().getDesc() + "\"";
		String quantity = "quantity=\"" + spaceText.getQuantity() + "\"";
		String unit = "unit=\"" + spaceText.getUnit().getDesc() + "\"";

		result = result + "<space " + dim + " " + quantity + " " + unit + "/>";

		if (spaceText.getParentOfLastText() == null) {
			spaceText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		String place = addText.getPlace() != Place.UNSPECIFIED ? "place=\""
				+ addText.getPlace().getDesc() + "\"" : "";

		result = result + "<add " + place + ">";

		TextPortion firstChild = addText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</add>";

		if (addText.getParentOfLastText() == null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		String rend = delText.getHow() != HowDel.UNSPECIFIED ? "rend=\""
				+ delText.getHow().getDesc() + "\"" : "";

		result = result + "<del " + rend + ">";

		TextPortion firstChild = delText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</del>";

		if (delText.getParentOfLastText() == null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {
		result = result + "<subst>";

		TextPortion firstChild = substText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</subst>";

		if (substText.getParentOfLastText() == null) {
			substText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(GapText gapText) {
		result = result + "<gap reason=\">" + gapText.getReason().getDesc()
				+ "\" extent=\"" + gapText.getExtent() + "\" unit=\""
				+ gapText.getUnit().getDesc() + "\"/>";

		if (gapText.getParentOfLastText() == null) {
			gapText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(UnclearText unclearText) {
		result = result + "<unclear reason=\">"
				+ unclearText.getReason().getDesc() + "\">";

		TextPortion firstChild = unclearText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</unclear>";

		if (unclearText.getParentOfLastText() == null) {
			unclearText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AltText altText) {
		String valueTarget = "";
		String valueWeight = "";
		for (AltTextWeight weight : altText.getAltTextWeightSet()) {
			valueTarget = valueTarget + "#" + weight.getSegText().getXmlId();
			valueWeight = valueWeight + " " + weight.getWeight();
		}

		result = result + "<alt target=\"" + valueTarget + "\"" + " mode=\""
				+ altText.getMode().getDesc() + "\" weights=\"" + valueWeight
				+ "\"/>";

		if (altText.getParentOfLastText() == null) {
			altText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(NoteText noteText) {
		result = result + "<note type=\"" + noteText.getType().getDesc()
				+ "\">";

		TextPortion firstChild = noteText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</note>";

		if (noteText.getParentOfLastText() == null) {
			noteText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RefText refText) {
		result = result + "<note type=\"" + refText.getType().getDesc() + "\" "
				+ "target=\"#" + refText.getTarget() + "\">";

		TextPortion firstChild = refText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		result = result + "</ref>";

		if (refText.getParentOfLastText() == null) {
			refText.getNextText().accept(this);
		}
	}

}
