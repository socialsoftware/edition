package pt.ist.socialsoftware.edition.text.feature.inout;

import org.apache.commons.io.IOUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import pt.ist.socialsoftware.edition.text.domain.*;
import pt.ist.socialsoftware.edition.text.domain.Fragment;
import pt.ist.socialsoftware.edition.text.domain.NullHeteronym;
import pt.ist.socialsoftware.edition.text.domain.ScholarInter;
import pt.ist.socialsoftware.edition.text.feature.generators.JDomTEITextPortionWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class ExpertEditionTEIExport {

    Document jdomDoc = null;

    // Element rootElement = null;

    JDomTEITextPortionWriter writer = null;
    Set<ScholarInter> scholarInterSelectedSet;

    Namespace xmlns;

    // List<String> scholarInterSelectedSet = new ArrayList<String>();

    public ExpertEditionTEIExport() {
        this.xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
    }

    public void generate(Map<Fragment, Set<ScholarInter>> fragmentMap) {
        // TODO Auto-generated method stub

        Element rootElement = generateCorpus();
        generateCorpusHeader(rootElement);

        Fragment fragment;

        for (Map.Entry<Fragment, Set<ScholarInter>> entry : fragmentMap.entrySet()) {

            fragment = entry.getKey();
            this.scholarInterSelectedSet = entry.getValue();

            generateFragment(rootElement, fragment, this.scholarInterSelectedSet);
        }
    }

    private Element generateCorpus() {
        this.jdomDoc = new Document();
        Element rootElement = new Element("teiCorpus");

        rootElement.setNamespace(this.xmlns);

        rootElement.addNamespaceDeclaration(Namespace.getNamespace("svg", "http://www.w3.org/2000/svg"));

        rootElement.addNamespaceDeclaration(Namespace.getNamespace("xi", "http://www.w3.org/2001/XInclude"));

        this.jdomDoc.setRootElement(rootElement);
        return rootElement;
    }

    private void generateCorpusHeader(Element rootElement) {
        Element newElement = new Element("teiHeader", this.xmlns);
        Attribute type = new Attribute("type", "corpus");
        newElement.setAttribute(type);
        rootElement.addContent(newElement);
    }

    private void generateFragment(Element rootElement, Fragment fragment, Set<ScholarInter> expertEditionInterSelectedSet) {

        // Namespace xmlns = Namespace.XML_NAMESPACE;
        // .getNamespace("http://www.tei-c.org/ns/1.0");

        // Namespace xmlns =
        // Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

        Element fragElement = new Element("TEI", this.xmlns);

        // fragElement.addNamespaceDeclaration(Namespace
        // .getNamespace("http://www.tei-c.org/ns/1.0"));
        // //
        // fragElement.setNamespace(Namespace
        // .getNamespace("http://www.tei-c.org/ns/1.0"));

        // fragElement.addNamespaceDeclaration(Namespace.getNamespace("",
        // fragment.getXmlId()));

        // Attribute type = new Attribute("xmlns",
        // "http://www.tei-c.org/ns/1.0");
        // fragElement.setAttribute(type);

        Attribute id = new Attribute("id", fragment.getXmlId(), Namespace.XML_NAMESPACE);
        fragElement.setAttribute(id);

        generateTextHeader(fragment, fragElement);
        generateFacsimiles(fragment, fragElement);
        generateTranscription(fragElement, fragment, expertEditionInterSelectedSet);

        rootElement.addContent(fragElement);
    }

    private void generateTextHeader(Fragment fragment, Element rootElement) {

        Element headerElement = new Element("teiHeader", this.xmlns);
        Attribute type = new Attribute("type", "text");
        headerElement.setAttribute(type);
        rootElement.addContent(headerElement);

        Element fileDescElement = new Element("fileDesc", this.xmlns);
        headerElement.addContent(fileDescElement);

        // titleStmt codification

        Element titleStmtElement = new Element("titleStmt", this.xmlns);
        fileDescElement.addContent(titleStmtElement);

        Element titleElement = new Element("title", this.xmlns);
        titleStmtElement.addContent(titleElement);
        titleElement.addContent(fragment.getTitle());

        Element authorElement = new Element("author", this.xmlns);
        titleStmtElement.addContent(authorElement);
       // authorElement.addContent(VirtualModule.getInstance().getAuthor());

        Element respStmtElement = new Element("respStmt", this.xmlns);
        titleStmtElement.addContent(respStmtElement);

        Element respElement = new Element("resp", this.xmlns);
        respStmtElement.addContent(respElement);
        respElement.addContent("encoding");

        // TODO: coder's name
        Element nameElement = new Element("name", this.xmlns);
        respStmtElement.addContent(nameElement);

        // publicationStmt codification

        Element publicationStmtElement = new Element("publicationStmt", this.xmlns);
        fileDescElement.addContent(publicationStmtElement);

        Element publisherElement = new Element("publisher", this.xmlns);
        publicationStmtElement.addContent(publisherElement);
        publisherElement.addContent("University of Coimbra");

        Element pubPlaceElement = new Element("pubPlace", this.xmlns);
        publicationStmtElement.addContent(pubPlaceElement);
        pubPlaceElement.addContent("Coimbra");

        Element availabilityElement = new Element("availability", this.xmlns);
        publicationStmtElement.addContent(availabilityElement);
        availabilityElement.setAttribute("status", "restricted");

        Element licenceElement = new Element("licence", this.xmlns);
        availabilityElement.addContent(licenceElement);
        licenceElement.setAttribute("target", "http://creativecommons.org/licenses/by-sa/3.0/");
        // TODO: <p>xpto</p> ?

        Element dateElement = new Element("date", this.xmlns);
        publicationStmtElement.addContent(dateElement);
        dateElement.setAttribute("when", "2014");

        Element sourceDescElement = new Element("sourceDesc", this.xmlns);
        fileDescElement.addContent(sourceDescElement);

        generateSources(fragment, sourceDescElement);
        generateWitnesses(fragment, sourceDescElement);
    }

    private void generateSources(Fragment fragment, Element rootElement) {

        Element listBibl = new Element("listBibl", this.xmlns);
        // listBibl.addNamespaceDeclaration(Namespace.getNamespace("id",
        // fragment.getXmlId() + ".SRC"));

        Attribute id = new Attribute("id", fragment.getXmlId() + ".SRC", Namespace.XML_NAMESPACE);
        listBibl.setAttribute(id);

        Element listBibl2 = new Element("listBibl", this.xmlns);
        // listBibl2.addNamespaceDeclaration(Namespace.getNamespace("id",
        // fragment.getXmlId() + ".SRC.MS"));

        Attribute id2 = new Attribute("id", fragment.getXmlId() + ".SRC.MS", Namespace.XML_NAMESPACE);
        listBibl2.setAttribute(id2);

        rootElement.addContent(listBibl);
        listBibl.addContent(listBibl2);

        // generate HeaderSources

        ManuscriptSource manuscript = null;

        Element msDescElement = null;
        Element msIdentifierElement = null;

        Element settlementElement = null;
        Element repositoryElement = null;
        Element idnoElement = null;
        Element altIdentifierElement = null;
        Element idnoAltElement = null;

        Element physDescElement = null;
        Element objectDescElement = null;
        Element supportDescElement = null;
        Element layoutDescElement = null;
        Element layoutElement = null;

        Element handDescElement = null;
        Element pElement = null;
        Element additionsElement = null;
        Element bindingDescElement = null;
        Element bindingElement = null;

        Element historyElement = null;
        Element originElement = null;
        Element origdateElement = null;

        for (Source source : fragment.getSourcesSet()) {

            // TODO: outros tipos de fontes
            manuscript = (ManuscriptSource) source;

            msDescElement = new Element("msDesc", this.xmlns);

            Attribute idms = new Attribute("id", manuscript.getXmlId(), Namespace.XML_NAMESPACE);
            msDescElement.setAttribute(idms);

            msIdentifierElement = new Element("msIdentifier", this.xmlns);
            physDescElement = new Element("physDesc", this.xmlns);

            msDescElement.addContent(msIdentifierElement);
            msDescElement.addContent(physDescElement);

            settlementElement = new Element("settlement", this.xmlns);
            settlementElement.addContent(manuscript.getSettlement());
            msIdentifierElement.addContent(settlementElement);

            repositoryElement = new Element("repository", this.xmlns);
            repositoryElement.addContent(manuscript.getRepository());
            msIdentifierElement.addContent(repositoryElement);

            idnoElement = new Element("idno", this.xmlns);
            idnoElement.addContent(manuscript.getIdno());
            msIdentifierElement.addContent(idnoElement);

            altIdentifierElement = new Element("altIdentifier", this.xmlns);
            altIdentifierElement.setAttribute("type", "SC");
            msIdentifierElement.addContent(altIdentifierElement);

            idnoAltElement = new Element("idno", this.xmlns);
            idnoAltElement.addContent(source.getName());
            altIdentifierElement.addContent(idnoAltElement);

            // physDesc // TODO: strings
            objectDescElement = new Element("objectDesc", this.xmlns);
            objectDescElement.setAttribute("form", manuscript.getForm().toString().toLowerCase());
            physDescElement.addContent(objectDescElement);

            supportDescElement = new Element("supportDesc", this.xmlns);
            supportDescElement.setAttribute("material", manuscript.getMaterial().name().toLowerCase());
            objectDescElement.addContent(supportDescElement);

            layoutDescElement = new Element("layoutDesc", this.xmlns);
            objectDescElement.addContent(layoutDescElement);

            layoutElement = new Element("layout", this.xmlns);
            layoutElement.setAttribute("columns", Integer.toString(manuscript.getColumns()));
            layoutDescElement.addContent(layoutElement);

            handDescElement = new Element("handDesc", this.xmlns);
            physDescElement.addContent(handDescElement);

            pElement = new Element("p", this.xmlns);
            pElement.addContent(manuscript.getNotes());
            handDescElement.addContent(pElement);

            additionsElement = new Element("additions", this.xmlns);

            if (manuscript.getHasLdoDLabel()) {
                additionsElement.addContent("VirtualModule");
            }
            physDescElement.addContent(additionsElement);

            bindingDescElement = new Element("bindingDesc", this.xmlns);
            physDescElement.addContent(bindingDescElement);

            bindingElement = new Element("binding", this.xmlns);
            bindingDescElement.addContent(bindingElement);

            // TODO: to update
            pElement = new Element("p", this.xmlns);
            // pElement.addContent(manuscript.getNotes());
            bindingElement.addContent(pElement);

            if (manuscript.getLdoDDate() != null) {
                historyElement = new Element("history", this.xmlns);
                msDescElement.addContent(historyElement);

                originElement = new Element("origin", this.xmlns);
                historyElement.addContent(originElement);

                String date = manuscript.getLdoDDate().print();

                origdateElement = new Element("origDate", this.xmlns);
                origdateElement.setAttribute("when", date);

                if (manuscript.getLdoDDate() != null) {
                    origdateElement.setAttribute("precision", manuscript.getLdoDDate().getPrecision().getDesc());
                }

                origdateElement.addContent(date);
                originElement.addContent(origdateElement);
            }

            listBibl2.addContent(msDescElement);

        }

    }

    private void generateWitnesses(Fragment fragment, Element rootElement) {

        // generate Sources Interp
        Element listWitElement = null;
        Element listWitAuthElement = null;
        Element listWitEdElement = null;
        Element witnessElement = null;
        Element refElement = null;

        // listWitEd
        Element headListWitElement = null;
        Element listWitEdCritElement = null;

        Element biblElement = null;
        Element respStmtElement = null;
        Element respElement = null;
        Element persNameElement = null;

        Element titleElement = null;
        Element biblScopeElement = null;

        Element noteElement = null;
        Element dateElement = null;

        listWitElement = new Element("listWit", this.xmlns);

        Attribute idlw = new Attribute("id", fragment.getXmlId() + ".WIT", Namespace.XML_NAMESPACE);
        listWitElement.setAttribute(idlw);

        // listWitElement.addNamespaceDeclaration(Namespace.getNamespace("id",
        // fragment.getXmlId() + ".WIT"));

        listWitAuthElement = new Element("listWit", this.xmlns);

        // listWitAuthElement.addNamespaceDeclaration(Namespace.getNamespace("id",
        // fragment.getXmlId() + ".WIT.MS"));

        Attribute idlwa = new Attribute("id", fragment.getXmlId() + ".WIT.MS", Namespace.XML_NAMESPACE);
        listWitAuthElement.setAttribute(idlwa);

        // manuscripts
        boolean selected = false;
        for (SourceInter sourceInter : fragment.getSortedSourceInter()) {

            // TODO selecionar as edicoes autorais ?
            if (this.scholarInterSelectedSet.contains(sourceInter)) {

                witnessElement = new Element("witness", this.xmlns);
                // witnessElement.addNamespaceDeclaration(Namespace.getNamespace(
                // "id", sourceInter.getXmlId()));

                Attribute idw = new Attribute("id", sourceInter.getXmlId(), Namespace.XML_NAMESPACE);
                witnessElement.setAttribute(idw);

                refElement = new Element("ref", this.xmlns);
                refElement.setAttribute("target", "#" + sourceInter.getSource().getXmlId());
                witnessElement.addContent(refElement);

                listWitAuthElement.addContent(witnessElement);
                selected = true;
            }
        }

        if (selected) {
            listWitElement.addContent(listWitAuthElement);
        }

        // editorial witness

        listWitEdElement = new Element("listWit", this.xmlns);
        // listWitEdElement.addNamespaceDeclaration(Namespace.getNamespace("id",
        // fragment.getXmlId() + ".WIT.ED"));

        Attribute idlwe = new Attribute("id", fragment.getXmlId() + ".WIT.ED", Namespace.XML_NAMESPACE);
        listWitEdElement.setAttribute(idlwe);

        headListWitElement = new Element("head", this.xmlns);
        listWitEdElement.addContent(headListWitElement);

        listWitEdCritElement = new Element("listWit", this.xmlns);

        // listWitEdCritElement.addNamespaceDeclaration(Namespace.getNamespace(
        // "id", fragment.getXmlId() + ".WIT.ED.CRIT"));

        Attribute idlwec = new Attribute("id", fragment.getXmlId() + ".WIT.ED.CRIT", Namespace.XML_NAMESPACE);
        listWitEdCritElement.setAttribute(idlwec);

        listWitEdElement.addContent(listWitEdCritElement);

        ExpertEditionInter expertEditionInter = null;

        for (ScholarInter scholarInter : fragment.getScholarInterSet()) {

            // TODO: confirm: type EDITORIAL && selected
            if (scholarInter.isExpertInter()
                    && this.scholarInterSelectedSet.contains(scholarInter)) {

                expertEditionInter = (ExpertEditionInter) scholarInter;

                witnessElement = new Element("witness", this.xmlns);

                Attribute idwe = new Attribute("id", expertEditionInter.getXmlId(), Namespace.XML_NAMESPACE);
                witnessElement.setAttribute(idwe);

                refElement = new Element("ref", this.xmlns);
                refElement.setAttribute("target", "#" + expertEditionInter.getEdition().getXmlId());

                witnessElement.addContent(refElement);

                biblElement = new Element("bibl", this.xmlns);
                witnessElement.addContent(biblElement);

                // heteronimo nao declarado (!=null)
                if (scholarInter.getHeteronym().getName().compareTo(NullHeteronym.getNullHeteronym().getName()) != 0) {

                    respStmtElement = new Element("respStmt", this.xmlns);
                    biblElement.addContent(respStmtElement);

                    respElement = new Element("resp", this.xmlns);
                    respElement.addContent("heterónimo");
                    respStmtElement.addContent(respElement);

                    String name = scholarInter.getHeteronym().getName();
                    String corresp = "";

                    persNameElement = new Element("persName", this.xmlns);
                    persNameElement.addContent(name);

                    if (name.compareTo("Bernardo Soares") == 0) {
                        corresp = "BS";
                    } else {
                        corresp = "VG";
                    }

                    persNameElement.setAttribute("corresp", "#HT." + corresp);
                    respStmtElement.addContent(persNameElement);
                }

                titleElement = new Element("title", this.xmlns);
                titleElement.setAttribute("level", "a");
                titleElement.addContent(scholarInter.getTitle());
                biblElement.addContent(titleElement);

                biblScopeElement = new Element("biblScope", this.xmlns);
                biblScopeElement.setAttribute("unit", "number");
                biblScopeElement.addContent(expertEditionInter.getNumber() + "");
                biblElement.addContent(biblScopeElement);

                if (expertEditionInter.getVolume() != null) {
                    biblScopeElement = new Element("biblScope", this.xmlns);
                    biblScopeElement.setAttribute("unit", "vol");
                    biblScopeElement.addContent(expertEditionInter.getVolume() + "");
                    biblElement.addContent(biblScopeElement);
                }

                biblScopeElement = new Element("biblScope", this.xmlns);
                biblScopeElement.setAttribute("from", expertEditionInter.getStartPage() + "");
                biblScopeElement.setAttribute("to", expertEditionInter.getEndPage() + "");

                // TODO confirm
                biblScopeElement.setAttribute("unit", "pp");
                biblElement.addContent(biblScopeElement);

                if (expertEditionInter.getNotes().compareTo("") != 0) {
                    noteElement = new Element("note", this.xmlns);
                    noteElement.setAttribute("type", "physDesc");
                    noteElement.addContent(expertEditionInter.getNotes());
                    biblElement.addContent(noteElement);
                }

                if (expertEditionInter.getLdoDDate() != null) {

                    dateElement = new Element("date", this.xmlns);
                    dateElement.addContent(expertEditionInter.getLdoDDate().print());

                    dateElement.setAttribute("when", expertEditionInter.getLdoDDate().print());

                    biblElement.addContent(dateElement);
                }

                listWitEdCritElement.addContent(witnessElement);

            }

        }

        listWitElement.addContent(listWitEdElement);
        rootElement.addContent(listWitElement);
    }

    private void generateFacsimiles(Fragment fragment, Element fragElement) {
        // TODO Auto-generated method stub
        for (Source source : fragment.getSourcesSet()) {
            generateFacsimile(source.getFacsimile(), fragElement);
        }

    }

    private void generateFacsimile(Facsimile facsimile, Element fragElement) {

        Element facElement = new Element("facsimile", this.xmlns);

        Attribute idf = new Attribute("id", facsimile.getXmlId(), Namespace.XML_NAMESPACE);
        facElement.setAttribute(idf);

        Attribute corresp = new Attribute("corresp", "#" + facsimile.getSource().getXmlId());
        facElement.setAttribute(corresp);

        int i = 0;

        for (Surface surface : facsimile.getSurfaces()) {
            i++;
            Element surfaceElement = new Element("surface", this.xmlns);
            Element graphElement = new Element("graphic", this.xmlns);

            Attribute graphAtt = new Attribute("url", surface.getGraphic());
            graphElement.setAttribute(graphAtt);

            Attribute idg = new Attribute("id", facsimile.getXmlId() + "-" + i, Namespace.XML_NAMESPACE);
            graphElement.setAttribute(idg);

            surfaceElement.addContent(graphElement);
            facElement.addContent(surfaceElement);
        }

        fragElement.addContent(facElement);
    }

    private void generateTranscription(Element parentElement, Fragment fragment, Set<ScholarInter> scholarInterSelectedSet) {

        Element textElement = new Element("text", this.xmlns);
        parentElement.addContent(textElement);

        Element bodyElement = new Element("body", this.xmlns);
        textElement.addContent(bodyElement);

        Element divElement = new Element("div", this.xmlns);
        bodyElement.addContent(divElement);

        Attribute iddiv = new Attribute("id", fragment.getXmlId() + ".TEXT", Namespace.XML_NAMESPACE);
        divElement.setAttribute(iddiv);

        this.writer = new JDomTEITextPortionWriter(divElement, this.scholarInterSelectedSet);
        // writer.visit((AppText) fragment.getTextPortion());
        AppText app = (AppText) fragment.getTextPortion();
        RdgText rdg = (RdgText) app.getFirstChildText();

        if (rdg.getFirstChildText() instanceof ParagraphText) {
            this.writer.visit((ParagraphText) rdg.getFirstChildText());
        } else if (rdg.getFirstChildText() instanceof PbText) {
            this.writer.visit((PbText) rdg.getFirstChildText());
        }

    }

    // TODO: to remove
    public JDomTEITextPortionWriter getWriter() {
        return this.writer;
    }

    public String updateTeiHeader(String xml) {
        String header = "";
        String result = "";
        Resource resource = new ClassPathResource("teiCorpusHeader.xml");

        try {
            InputStream resourceInputStream = resource.getInputStream();
            header = IOUtils.toString(resourceInputStream, "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result = xml.subSequence(0, xml.indexOf("<teiHeader")) + header + "\n" + xml.substring(xml.indexOf("<TEI"));

        return result;
    }

    public String getXMLResult() {
        XMLOutputter xml = new XMLOutputter();
        // we want to format the xml. This is used only for demonstration.
        // pretty formatting adds extra spaces and is generally not required.
        xml.setFormat(Format.getPrettyFormat());
        return updateTeiHeader(xml.outputString(this.jdomDoc));
    }

}
