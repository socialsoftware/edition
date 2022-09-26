package pt.ist.socialsoftware.edition.ldod.generators;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.AddText.Place;

import java.util.Set;

public class JDomTEITextPortionWriter implements TextPortionVisitor {
    private final static Logger logger = LoggerFactory.getLogger(JDomTEITextPortionWriter.class);

    // create the jdom
    // Document jdomDoc = new Document();
    // create root element

    private Namespace xmlns;

    // List<String> fragInterSelectedSet = new ArrayList<String>();

    private Element rootElement = null;
    private Set<FragInter> fragInterSelectedList = null;

    // jdomDoc.setRootElement(rootElement);

    public JDomTEITextPortionWriter(Element rootElement) {
        this.rootElement = rootElement;
        this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
    }

    public JDomTEITextPortionWriter(Element rootElement, Set<FragInter> fragInterSelectedList) {
        // TODO Auto-generated constructor stub
        this.rootElement = rootElement;
        this.fragInterSelectedList = fragInterSelectedList;
        this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
    }

    private Element generateElement(Element rootElement, String name) {
        Element newElement = new Element(name, xmlns);
        rootElement.addContent(newElement);
        return newElement;
    }

    @Override
    public void visit(AppText appText) {
        rootElement = generateElement(rootElement, "app");

        propagate2FirstChild(appText);

        if (!appText.getType().getDesc().equalsIgnoreCase("UNSPECIFIED")) {
            Attribute typeAtt = new Attribute("type", appText.getType().getDesc());
            rootElement.setAttribute(typeAtt);
        }

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(appText);
    }

    @Override
    public void visit(RdgGrpText rdgGrpText) {
        rootElement = generateElement(rootElement, "rdgGrp");

        if (rdgGrpText.getType() != null) {
            if (rdgGrpText.getType().getDesc().compareTo("unspecified") != 0) {
                Attribute typeAtt = new Attribute("type", rdgGrpText.getType().getDesc());
                rootElement.setAttribute(typeAtt);
            }
        }

        propagate2FirstChild(rdgGrpText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(rdgGrpText);
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
            rootElement = generateElement(rootElement, "rdg");

            if (!wit.equals("")) {
                Attribute witAtt = new Attribute("wit", wit.trim());
                rootElement.setAttribute(witAtt);
            }

            propagate2FirstChild(rdgText);

            rootElement = rootElement.getParentElement();
        }

        propagate2NextSibling(rdgText);
    }

    @Override
    public void visit(ParagraphText paragraphText) {
        rootElement = generateElement(rootElement, "p");

        propagate2FirstChild(paragraphText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(paragraphText);
    }

    @Override
    public void visit(SegText segText) {
        String rendition = "";
        for (Rend rend : segText.getRendSet()) {
            rendition = rendition + "#" + rend.getRend().getDesc() + " ";
        }

        rootElement = generateElement(rootElement, "seg");

        if (!rendition.equals("")) {
            Attribute witAtt = new Attribute("rendition", rendition.trim());
            rootElement.setAttribute(witAtt);
        }

        if (segText.getXmlId() != null) {
            Attribute ids = new Attribute("id", segText.getXmlId(), Namespace.XML_NAMESPACE);
            rootElement.setAttribute(ids);
        }

        propagate2FirstChild(segText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(segText);
    }

    @Override
    public void visit(SimpleText simpleText) {
        rootElement.addContent(simpleText.getValue());

        propagate2NextSibling(simpleText);
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
            rootElement = generateElement(rootElement, "lb");

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
        }

        propagate2NextSibling(lbText);
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
            rootElement = generateElement(rootElement, "pb");

            Attribute edAtt = new Attribute("ed", ed.trim());
            rootElement.setAttribute(edAtt);

            if (pbText.getSurface() != null) {
                facs = pbText.getSurface().getGraphic();
                Attribute facsAtt = new Attribute("facs", facs);
                rootElement.setAttribute(facsAtt);
            }

            rootElement = rootElement.getParentElement();

        }

        propagate2NextSibling(pbText);
    }

    @Override
    public void visit(SpaceText spaceText) {
        rootElement = generateElement(rootElement, "space");

        Attribute dimAtt = new Attribute("dim", spaceText.getDim().getDesc());
        rootElement.setAttribute(dimAtt);

        Attribute quantityAtt = new Attribute("quantity", Integer.toString(spaceText.getQuantity()));
        rootElement.setAttribute(quantityAtt);

        Attribute unitAtt = new Attribute("unit", spaceText.getUnit().getDesc());
        rootElement.setAttribute(unitAtt);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(spaceText);
    }

    @Override
    public void visit(AddText addText) {
        rootElement = generateElement(rootElement, "add");

        if (addText.getPlace() != Place.UNSPECIFIED) {
            Attribute placeAtt = new Attribute("place", addText.getPlace().getDesc());
            rootElement.setAttribute(placeAtt);
        }

        propagate2FirstChild(addText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(addText);
    }

    @Override
    public void visit(DelText delText) {
        rootElement = generateElement(rootElement, "del");

        if (delText.getHow() != DelText.HowDel.UNSPECIFIED) {
            Attribute delAtt = new Attribute("rend", delText.getHow().getDesc());
            rootElement.setAttribute(delAtt);
        }

        propagate2FirstChild(delText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(delText);
    }

    @Override
    public void visit(SubstText substText) {
        rootElement = generateElement(rootElement, "subst");

        propagate2FirstChild(substText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(substText);
    }

    @Override
    public void visit(GapText gapText) {
        rootElement = generateElement(rootElement, "gap");

        Attribute reasonAtt = new Attribute("reason", gapText.getReason().getDesc());
        Attribute extentAtt = new Attribute("extent", String.valueOf(gapText.getExtent()));
        Attribute unitAtt = new Attribute("unit", gapText.getUnit().getDesc());

        rootElement.setAttribute(reasonAtt);
        rootElement.setAttribute(extentAtt);
        rootElement.setAttribute(unitAtt);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(gapText);
    }

    @Override
    public void visit(UnclearText unclearText) {
        rootElement = generateElement(rootElement, "unclear");

        Attribute reasonAtt = new Attribute("reason", unclearText.getReason().getDesc());
        rootElement.setAttribute(reasonAtt);

        propagate2FirstChild(unclearText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(unclearText);
    }

    @Override
    public void visit(AltText altText) {
        StringBuilder valueTarget = new StringBuilder();
        StringBuilder valueWeight = new StringBuilder();

        for (AltTextWeight weight : altText.getAltTextWeightSet()) {
            valueTarget.append("#").append(weight.getSegText() != null ? weight.getSegText().getXmlId() : "").append(" ");
            valueWeight.append(" ").append(weight.getWeight());
        }

        valueTarget = new StringBuilder(valueTarget.toString().trim());
        valueWeight = new StringBuilder(valueWeight.toString().trim());

        rootElement = generateElement(rootElement, "alt");

        Attribute targetAtt = new Attribute("target", valueTarget.toString());
        Attribute modeAtt = new Attribute("mode", altText.getMode().getDesc());
        Attribute weightsAtt = new Attribute("weights", valueWeight.toString());

        rootElement.setAttribute(targetAtt);
        rootElement.setAttribute(modeAtt);
        rootElement.setAttribute(weightsAtt);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(altText);
    }

    @Override
    public void visit(NoteText noteText) {
        rootElement = generateElement(rootElement, "note");

        Attribute typeAtt = new Attribute("type", noteText.getType().getDesc());
        rootElement.setAttribute(typeAtt);

        propagate2FirstChild(noteText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(noteText);
    }

    @Override
    public void visit(RefText refText) {
        rootElement = generateElement(rootElement, "ref");

        Attribute typeAtt = new Attribute("type", refText.getType().getDesc());
        Attribute targetAtt = new Attribute("target", "#" + refText.getTarget());

        rootElement.setAttribute(typeAtt);
        rootElement.setAttribute(targetAtt);

        propagate2FirstChild(refText);

        rootElement = rootElement.getParentElement();

        propagate2NextSibling(refText);
    }

}
