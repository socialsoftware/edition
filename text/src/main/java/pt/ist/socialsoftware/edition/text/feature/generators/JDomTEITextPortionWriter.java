package pt.ist.socialsoftware.edition.text.feature.generators;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.text.domain.*;

import java.util.Set;

public class JDomTEITextPortionWriter implements TextPortionVisitor {
    private final static Logger logger = LoggerFactory.getLogger(JDomTEITextPortionWriter.class);

    // create the jdom
    // Document jdomDoc = new Document();
    // create root element

    private final Namespace xmlns;

    // List<String> fragInterSelectedSet = new ArrayList<String>();

    private Element rootElement = null;
    private Set<ScholarInter> fragInterSelectedList = null;

    // jdomDoc.setRootElement(rootElement);

    public JDomTEITextPortionWriter(Element rootElement) {
        this.rootElement = rootElement;
        this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
    }

    public JDomTEITextPortionWriter(Element rootElement, Set<ScholarInter> expertEditionInterSelectedList) {
        // TODO Auto-generated constructor stub
        this.rootElement = rootElement;
        this.fragInterSelectedList = expertEditionInterSelectedList;
        this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
    }

    private Element generateElement(Element rootElement, String name) {
        Element newElement = new Element(name, this.xmlns);
        rootElement.addContent(newElement);
        return newElement;
    }

    @Override
    public void visit(AppText appText) {
        this.rootElement = generateElement(this.rootElement, "app");

        propagate2FirstChild(appText);

        if (!appText.getType().getDesc().equalsIgnoreCase("UNSPECIFIED")) {
            Attribute typeAtt = new Attribute("type", appText.getType().getDesc());
            this.rootElement.setAttribute(typeAtt);
        }

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(appText);
    }

    @Override
    public void visit(RdgGrpText rdgGrpText) {
        this.rootElement = generateElement(this.rootElement, "rdgGrp");

        if (rdgGrpText.getType() != null) {
            if (rdgGrpText.getType().getDesc().compareTo("unspecified") != 0) {
                Attribute typeAtt = new Attribute("type", rdgGrpText.getType().getDesc());
                this.rootElement.setAttribute(typeAtt);
            }
        }

        propagate2FirstChild(rdgGrpText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(rdgGrpText);
    }

    @Override
    public void visit(RdgText rdgText) {

        String wit = "";
        boolean selected = false;

        for (ScholarInter inter : rdgText.getInterps()) {
            if (this.fragInterSelectedList.contains(inter)) {
                selected = true;
                wit = wit + "#" + inter.getXmlId() + " ";
            }
        }

        if (selected) {
            this.rootElement = generateElement(this.rootElement, "rdg");

            if (!wit.equals("")) {
                Attribute witAtt = new Attribute("wit", wit.trim());
                this.rootElement.setAttribute(witAtt);
            }

            propagate2FirstChild(rdgText);

            this.rootElement = this.rootElement.getParentElement();
        }

        propagate2NextSibling(rdgText);
    }

    @Override
    public void visit(ParagraphText paragraphText) {
        this.rootElement = generateElement(this.rootElement, "p");

        propagate2FirstChild(paragraphText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(paragraphText);
    }

    @Override
    public void visit(SegText segText) {
        String rendition = "";
        for (Rend rend : segText.getRendSet()) {
            rendition = rendition + "#" + rend.getRend().getDesc() + " ";
        }

        this.rootElement = generateElement(this.rootElement, "seg");

        if (!rendition.equals("")) {
            Attribute witAtt = new Attribute("rendition", rendition.trim());
            this.rootElement.setAttribute(witAtt);
        }

        if (segText.getXmlId() != null) {
            Attribute ids = new Attribute("id", segText.getXmlId(), Namespace.XML_NAMESPACE);
            this.rootElement.setAttribute(ids);
        }

        propagate2FirstChild(segText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(segText);
    }

    @Override
    public void visit(SimpleText simpleText) {
        this.rootElement.addContent(simpleText.getValue());

        propagate2NextSibling(simpleText);
    }

    @Override
    public void visit(LbText lbText) {

        String ed = "";
        boolean selected = false;

        for (ScholarInter inter : lbText.getInterps()) {
            if (this.fragInterSelectedList.contains(inter)) {
                ed = ed + "#" + inter.getXmlId() + " ";
                selected = true;
            }
        }

        if (selected) {
            this.rootElement = generateElement(this.rootElement, "lb");

            Attribute edAtt = new Attribute("ed", ed.trim());
            this.rootElement.setAttribute(edAtt);

            if (lbText.getHyphenated()) {
                Attribute hyphAtt = new Attribute("type", "hyphenated");
                this.rootElement.setAttribute(hyphAtt);
            }

            if (!lbText.getBreakWord()) {
                Attribute breakAtt = new Attribute("break", "no");
                this.rootElement.setAttribute(breakAtt);
            }

            this.rootElement = this.rootElement.getParentElement();
        }

        propagate2NextSibling(lbText);
    }

    @Override
    public void visit(PbText pbText) {

        String ed = "";
        String facs = "";
        boolean selected = false;

        for (ScholarInter inter : pbText.getInterps()) {
            if (this.fragInterSelectedList.contains(inter)) {
                ed = ed + "#" + inter.getXmlId() + " ";
                selected = true;
            }
        }

        if (selected) {
            this.rootElement = generateElement(this.rootElement, "pb");

            Attribute edAtt = new Attribute("ed", ed.trim());
            this.rootElement.setAttribute(edAtt);

            if (pbText.getSurface() != null) {
                facs = pbText.getSurface().getGraphic();
                Attribute facsAtt = new Attribute("facs", facs);
                this.rootElement.setAttribute(facsAtt);
            }

            this.rootElement = this.rootElement.getParentElement();

        }

        propagate2NextSibling(pbText);
    }

    @Override
    public void visit(SpaceText spaceText) {
        this.rootElement = generateElement(this.rootElement, "space");

        Attribute dimAtt = new Attribute("dim", spaceText.getDim().getDesc());
        this.rootElement.setAttribute(dimAtt);

        Attribute quantityAtt = new Attribute("quantity", Integer.toString(spaceText.getQuantity()));
        this.rootElement.setAttribute(quantityAtt);

        Attribute unitAtt = new Attribute("unit", spaceText.getUnit().getDesc());
        this.rootElement.setAttribute(unitAtt);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(spaceText);
    }

    @Override
    public void visit(AddText addText) {
        this.rootElement = generateElement(this.rootElement, "add");

        if (addText.getPlace() != AddText.Place.UNSPECIFIED) {
            Attribute placeAtt = new Attribute("place", addText.getPlace().getDesc());
            this.rootElement.setAttribute(placeAtt);
        }

        propagate2FirstChild(addText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(addText);
    }

    @Override
    public void visit(DelText delText) {
        this.rootElement = generateElement(this.rootElement, "del");

        if (delText.getHow() != DelText.HowDel.UNSPECIFIED) {
            Attribute delAtt = new Attribute("rend", delText.getHow().getDesc());
            this.rootElement.setAttribute(delAtt);
        }

        propagate2FirstChild(delText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(delText);
    }

    @Override
    public void visit(SubstText substText) {
        this.rootElement = generateElement(this.rootElement, "subst");

        propagate2FirstChild(substText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(substText);
    }

    @Override
    public void visit(GapText gapText) {
        this.rootElement = generateElement(this.rootElement, "gap");

        Attribute reasonAtt = new Attribute("reason", gapText.getReason().getDesc());
        Attribute extentAtt = new Attribute("extent", String.valueOf(gapText.getExtent()));
        Attribute unitAtt = new Attribute("unit", gapText.getUnit().getDesc());

        this.rootElement.setAttribute(reasonAtt);
        this.rootElement.setAttribute(extentAtt);
        this.rootElement.setAttribute(unitAtt);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(gapText);
    }

    @Override
    public void visit(UnclearText unclearText) {
        this.rootElement = generateElement(this.rootElement, "unclear");

        Attribute reasonAtt = new Attribute("reason", unclearText.getReason().getDesc());
        this.rootElement.setAttribute(reasonAtt);

        propagate2FirstChild(unclearText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(unclearText);
    }

    @Override
    public void visit(AltText altText) {
        String valueTarget = "";
        String valueWeight = "";

        for (AltTextWeight weight : altText.getAltTextWeightSet()) {
            valueTarget = valueTarget + "#" + weight.getSegText().getXmlId() + " ";
            valueWeight = valueWeight + " " + weight.getWeight();
        }

        valueTarget = valueTarget.trim();
        valueWeight = valueWeight.trim();

        this.rootElement = generateElement(this.rootElement, "alt");

        Attribute targetAtt = new Attribute("target", valueTarget);
        Attribute modeAtt = new Attribute("mode", altText.getMode().getDesc());
        Attribute weightsAtt = new Attribute("weights", valueWeight);

        this.rootElement.setAttribute(targetAtt);
        this.rootElement.setAttribute(modeAtt);
        this.rootElement.setAttribute(weightsAtt);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(altText);
    }

    @Override
    public void visit(NoteText noteText) {
        this.rootElement = generateElement(this.rootElement, "note");

        Attribute typeAtt = new Attribute("type", noteText.getType().getDesc());
        this.rootElement.setAttribute(typeAtt);

        propagate2FirstChild(noteText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(noteText);
    }

    @Override
    public void visit(RefText refText) {
        this.rootElement = generateElement(this.rootElement, "ref");

        Attribute typeAtt = new Attribute("type", refText.getType().getDesc());
        Attribute targetAtt = new Attribute("target", "#" + refText.getTarget());

        this.rootElement.setAttribute(typeAtt);
        this.rootElement.setAttribute(targetAtt);

        propagate2FirstChild(refText);

        this.rootElement = this.rootElement.getParentElement();

        propagate2NextSibling(refText);
    }

}
