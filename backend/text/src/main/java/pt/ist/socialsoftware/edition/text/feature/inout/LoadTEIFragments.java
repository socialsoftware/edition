package pt.ist.socialsoftware.edition.text.feature.inout;

import org.jdom2.*;
import org.jdom2.Content.CType;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.notification.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.text.domain.*;
import pt.ist.socialsoftware.edition.text.domain.AddText.Place;
import pt.ist.socialsoftware.edition.text.domain.AltText.AltMode;
import pt.ist.socialsoftware.edition.text.domain.Fragment.PrecisionType;
import pt.ist.socialsoftware.edition.text.domain.GapText.GapReason;
import pt.ist.socialsoftware.edition.text.domain.GapText.GapUnit;
import pt.ist.socialsoftware.edition.text.domain.ManuscriptSource.Medium;
import pt.ist.socialsoftware.edition.text.domain.RefText.RefType;
import pt.ist.socialsoftware.edition.text.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.text.domain.SpaceText.SpaceUnit;




import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class LoadTEIFragments {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoadTEIFragments.class);

    private Element ldoDTEI = null;
    private Namespace namespace = null;
    private TextModule text = null;

    private Document doc = null;

    // to define an order among page breaks
    private int pbOrder = 0;

    private final Map<String, List<Object>> directIdMap = new HashMap<>();

    private void putObjectDirectIdMap(String xmlID, Object object) {

        List<Object> list = this.directIdMap.get(xmlID);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(object);

        this.directIdMap.put(xmlID, list);
    }

    private List<Object> getObjectDirectIdMap(String xmlID) {
        List<Object> objects = this.directIdMap.get(xmlID);
        return objects;
    }

    private List<Object> getObjectDirectIdsMap(String[] listXmlId) {
        List<Object> objects = new ArrayList<>();
        for (String xmlId : listXmlId) {
            List<Object> objects2 = getObjectDirectIdMap(xmlId.substring(1));
            if (objects2 == null) {
                throw new LdoDLoadException("identificador não declarado " + xmlId);
            }
            objects.addAll(objects2);
        }
        return objects;
    }

    private Set<ScholarInter> getFragItersByListXmlID(String[] listInterXmlId) {
        List<Object> objects = getObjectDirectIdsMap(listInterXmlId);
        Set<ScholarInter> fragIters = new HashSet<>();
        for (Object object : objects) {
            try {
                fragIters.add((ScholarInter) object);
            } catch (ClassCastException ex) {
                throw new LdoDLoadException(
                        "um dos identificadores desta lista não é um identificador válido de witness: "
                                + Arrays.toString(listInterXmlId));
            }
        }
        return fragIters;
    }

    private final Map<String, Set<Object>> inverseIdMap = new HashMap<>();

    private Set<Object> getObjectInverseIdMap(String xmlID) {
        if (this.inverseIdMap.get(xmlID) == null) {
            return new HashSet<>();
        } else {
            Set<Object> objects = this.inverseIdMap.get(xmlID);
            return objects;
        }
    }

    private void putObjectInverseIdMap(String xmlID, Object object) {

        Set<Object> list = this.inverseIdMap.get(xmlID);
        if (list == null) {
            list = new HashSet<>();
        }
        list.add(object);

        this.inverseIdMap.put(xmlID, list);
    }

    private void getCorpusXmlIds() {
        for (ExpertEdition edition : this.text.getExpertEditionsSet()) {
            putObjectDirectIdMap(edition.getXmlId(), edition);
        }

        for (Heteronym heteronym : this.text.getHeteronymsSet()) {
            putObjectDirectIdMap(heteronym.getXmlId(), heteronym);
        }
    }

    XPathFactory xpfac = XPathFactory.instance();

    private void parseTEIFile(InputStream file) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);
        try {
            // TODO: create a config variable for the xml file
            this.doc = builder.build(file);
        } catch (FileNotFoundException e) {
            throw new LdoDLoadException("Ficheiro não encontrado");
        } catch (JDOMException e) {
            throw new LdoDLoadException("Ficheiro com problemas de codificação TEI");
        } catch (IOException e) {
            throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato: " + e.getMessage());
        }

        if (this.doc == null) {
            LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
            throw ex;
        }

        this.ldoDTEI = this.doc.getRootElement();
        this.namespace = this.ldoDTEI.getNamespace();
    }

    @Atomic(mode = TxMode.WRITE)
    public String loadFragmentsAtOnce(InputStream file) throws LdoDLoadException {
        String message = null;

        parseTEIFile(file);

//        this.virtualModule = VirtualModule.getInstance();
        this.text = TextModule.getInstance();
        getCorpusXmlIds();
        XPathExpression<Element> xp = this.xpfac.compile("//def:TEI/def:teiHeader", Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element element : xp.evaluate(this.doc)) {
            String xmlId = getFragmentXmlId(element);
            String title = getFragmentTitle(element);

            Fragment oldFragment = null;
            for (Fragment frag : this.text.getFragmentsSet()) {
                if (frag.getXmlId().equals(xmlId)) {
                    oldFragment = frag;
                    break;
                }
            }

            try {
                loadFragment(title, xmlId);
            } catch (LdoDLoadException e) {
                throw new LdoDLoadException("[" + title + "(" + xmlId + ")]: " + e.getMessage());
                // Warning: using the code below inconsistent data can be
                // committed
                // message = e.getMessage();
                // if (oldFragment != null) {
                // oldFragment.remove();
                // }
                // break;
            }

            if (oldFragment != null) {
                oldFragment.remove();
            }
        }
        return message;
    }

    public String loadFragmentsStepByStep(InputStream file) throws LdoDLoadException {
        String result = null;

        parseTEIFile(file);

        XPathExpression<Element> xp = this.xpfac.compile("//def:TEI/def:teiHeader", Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element element : xp.evaluate(this.doc)) {
  //          this.virtualModule = VirtualModule.getInstance();
            this.text = TextModule.getInstance();

            String xmlId = getFragmentXmlId(element);
            String title = getFragmentTitle(element);

            result = "CARREGAR: [" + xmlId + "(" + title + ")] <br>";

            Boolean exists = false;
            for (Fragment frag : this.text.getFragmentsSet()) {
                if (frag.getXmlId().equals(xmlId)) {
                    result = result + "------------> FRAG-ID JÁ EXISTE LOGO NÃO FOI CARREGADO <br>";
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                try {
                    atomicLoadFragment(title, xmlId);
                } catch (LdoDLoadException e) {
                    throw new LdoDLoadException("[" + title + "(" + xmlId + ")]: " + e.getMessage());
                }
            }
        }
        return result;
    }

    @Atomic(mode = TxMode.WRITE)
    private void atomicLoadFragment(String title, String xmlId) {
        this.directIdMap.clear();
        this.inverseIdMap.clear();
        getCorpusXmlIds();
        loadFragment(title, xmlId);
    }

    private void loadFragment(String title, String xmlId) {
        Fragment fragment = new Fragment(this.text, title, xmlId);

        putObjectDirectIdMap(xmlId, fragment);

        loadSourceManuscripts(fragment, xmlId);
        loadPrintedSources(fragment, xmlId);
        loadWitnesses(fragment, xmlId);
        loadFacsimile(xmlId);

        loadFragmentText(fragment, xmlId);

    }

    private void loadFragmentText(Fragment fragment, String fragmentXmlID) {
        String selectThisFragment = "[@xml:id='" + fragmentXmlID + "']";
        String queryExpression = "//def:TEI" + selectThisFragment + "/def:text/def:body";

        XPathExpression<Element> xp = this.xpfac.compile(queryExpression, Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        Set<ScholarInter> fragInters = fragment.getScholarInterSet().stream().map(ScholarInter.class::cast).collect(Collectors.toSet());

        AppText app = new AppText(TextPortion.VariationType.UNSPECIFIED);
        app.setFragment(fragment);
        RdgText rdg = new RdgText(app, TextPortion.VariationType.UNSPECIFIED, fragInters);

        Element element = xp.evaluate(this.doc).get(0);

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
                } else if (element2.getName().equals("gap")) {
                    loadGap(element2, parent);
                } else if (element2.getName().equals("unclear")) {
                    loadUnclear(element2, parent);
                } else if (element2.getName().equals("alt")) {
                    loadAlt(element2, parent);
                } else if (element2.getName().equals("note")) {
                    loadNote(element2, parent);
                } else if (element2.getName().equals("ref")) {
                    loadRef(element2, parent);
                } else {
                    throw new LdoDLoadException("DOES NOT HANDLE LOAD OF:" + element2 + " OF TYPE:"
                            + element2.getCType().toString() + " VALOR=" + element.getText());
                }
            }
        }
    }

    private void loadRef(Element element, TextPortion parent) {
        RefType type = getRefType(element);
        String target = getRefTarget(element);

        RefText refText = new RefText(parent, type, target);

        switch (type) {
            case GRAPHIC:
                // considering that it only refers to surfaces in the same fragment
                Surface surface = parent.getTopParent().getFragment().getSurface(target);
                if (surface != null) {
                    refText.setSurface(surface);
                } else {
                    throw new LdoDLoadException("o valor " + target
                            + " do atributo xml:id do elemento ref não corresponde ao id the nenhum elemento graphic");
                }
                break;
            case WITNESS:
                // considers that it only refers to interpretations in the same
                // fragment
                ScholarInter inter = parent.getFragment().getScholarInterByXmlId(target);
                if (inter != null) {
                    refText.setScholarInter(inter);
                } else {
                    throw new LdoDLoadException("o valor " + target
                            + " do atributo xml:id do elemento ref não corresponde ao id the nenhum elemento witness");
                }
                break;
            case FRAGMENT:
                Fragment fragment = TextModule.getInstance().getFragmentByXmlId(target);
                // if (fragment != null) {
                // if fragment == null is deal in class RefText
                refText.setRefFrag(fragment);
                // } else {
                // throw new LdoDLoadException(
                // "o valor "
                // + target
                // +
                // " do atributo xml:id do elemento ref não corresponde ao id the
                // nenhum elemento fragment."
                // + " NOTA: PODERÁ TER QUE CARREGAR ESSE FRAGMENTO PRIMEIRO");
                // }
                break;
        }

        List<Content> contentList = element.getContent();

        loadSimpleText((Text) contentList.get(0), refText);
    }

    private void loadNote(Element element, TextPortion parent) {
        NoteText.NoteType type = getNoteType(element);

        NoteText noteText = new NoteText(parent, type);

        for (Content content : element.getContent()) {
            if (content.getCType() == CType.Text) {
                if (content.getValue().trim() != "") {
                    loadSimpleText((Text) content, noteText);
                } else {
                    // empty text
                }
            } else if (content.getCType() == CType.Comment) {
                // ignore comments
            } else if (content.getCType() == CType.Element) {
                Element element2 = (Element) content;
                if (element2.getName().equals("ref")) {
                    loadRef(element2, noteText);
                } else {
                    throw new LdoDLoadException("não carrega elementos: " + element2 + " do tipo:" + element2.getName()
                            + " dentro de note " + " _VALOR=" + element.getText());
                }
            }
        }
    }

    private void loadAlt(Element element, TextPortion parent) {
        // get targets
        String[] targetList = getTarget(element);

        List<SegText> segTextList = new ArrayList<>();
        for (String xmlId : targetList) {
            List<Object> listSegTextList = getObjectDirectIdMap(xmlId.substring(1));
            if (listSegTextList == null) {
                throw new LdoDLoadException(
                        "Não está declarado xml:id associado a um identicador target do elemento alt. Valor="
                                + xmlId.substring(1));
            }
            SegText segText = (SegText) listSegTextList.get(0);
            if (segText == null) {
                throw new LdoDLoadException(
                        "Não há elemento seg associado a um identicador target do elemento alt. Valor="
                                + xmlId.substring(1));
            }
            segTextList.add(segText);
        }

        // get mode
        AltMode altMode = getAltMode(element);

        // get weights
        String[] weightList = getAltWeights(element);

        new AltText(parent, segTextList, altMode, weightList);
    }

    private void loadUnclear(Element element, TextPortion parent) {
        List<Content> contentList = element.getContent();

        if (contentList.size() != 1) {
            throw new LdoDLoadException("unclear não contém apenas texto" + element.getText());
        }

        if (contentList.get(0).getCType() != CType.Text) {
            throw new LdoDLoadException("unclear não contém apenas texto" + element.getText());
        }

        UnclearText.UnclearReason reason = getUnclearReasonAttribute(element);
        if (reason == UnclearText.UnclearReason.NONSPECIFIED) {
            throw new LdoDLoadException("O elemento unclear no contexto " + element.getParent().getContent()
                    + " deve ter valor para o atributo reason");
        }

        UnclearText unclearText = new UnclearText(parent, reason);

        loadSimpleText((Text) contentList.get(0), unclearText);
    }

    private void loadGap(Element element, TextPortion parent) {
        GapReason reason = getGapReasonAttribute(element);
        if (reason == GapReason.NONSPECIFIED) {
            throw new LdoDLoadException("O elemento gap no contexto " + element.getParent().getContent()
                    + " deve ter valor para o atributo reason");
        }

        int extent = getExtentAttribute(element);
        if (extent == 0) {
            throw new LdoDLoadException("O elemento gap no contexto " + element.getParent().getContent()
                    + " deve ter valor para o atributo extent");
        }

        GapUnit unit = getGapUnitAttribute(element);
        if (unit == GapUnit.NONSPECIFIED) {
            throw new LdoDLoadException("O elemento gap no contexto " + element.getParent().getContent()
                    + " deve ter valor para o atributo unit");
        }

        new GapText(parent, reason, extent, unit);
    }

    private void loadSubst(Element element, TextPortion parent) {
        SubstText substText = new SubstText(parent);

        for (Content content : element.getContent()) {
            if (content.getCType() == CType.Text) {
                // ignore text
            } else if (content.getCType() == CType.Comment) {
                // ignore comments
            } else if (content.getCType() == CType.Element) {
                Element element2 = (Element) content;
                if (element2.getName().equals("add")) {
                    loadAdd(element2, substText);
                } else if (element2.getName().equals("del")) {
                    loadDel(element2, substText);
                } else {
                    throw new LdoDLoadException("não carrega elementos: " + element2 + " do tipo:" + element2.getName()
                            + " dentro de subst" + " _VALOR=" + element.getText());
                }
            }
        }
    }

    private void loadDel(Element element, TextPortion parent) {
        Attribute howDelAttribute = element.getAttribute("rend");
        DelText.HowDel how = getHowDelAttribute(howDelAttribute);

        DelText delText = new DelText(parent, how);

        for (Content content : element.getContent()) {
            if (content.getCType() == CType.Text) {
                if (content.getValue().trim() != "") {
                    loadSimpleText((Text) content, delText);
                } else {
                    // empty text
                }
            } else if (content.getCType() == CType.Comment) {
                // ignore comments
            } else if (content.getCType() == CType.Element) {
                Element element2 = (Element) content;
                if (element2.getName().equals("add")) {
                    loadAdd(element2, delText);
                } else if (element2.getName().equals("lb")) {
                    loadLb(element2, delText);
                } else if (element2.getName().equals("gap")) {
                    loadGap(element2, delText);
                } else if (element2.getName().equals("unclear")) {
                    loadUnclear(element2, delText);
                } else if (element2.getName().equals("note")) {
                    loadNote(element2, parent);
                } else if (element2.getName().equals("space")) {
                    loadSpace(element2, delText);
                } else {
                    throw new LdoDLoadException("não carrega elementos: " + element2 + " do tipo:" + element2.getName()
                            + " dentro de del " + " _VALOR=" + element.getText());
                }
            }
        }
    }

    private void loadAdd(Element element, TextPortion parent) {
        Attribute placeAttribute = element.getAttribute("place");
        Place place = getPlaceAttribute(placeAttribute);

        AddText addText = new AddText(parent, place);

        setRenditions(element, addText);

        for (Content content : element.getContent()) {
            if (content.getCType() == CType.Text) {
                if (content.getValue().trim() != "") {
                    loadSimpleText((Text) content, addText);
                } else {
                    // empty text
                }
            } else if (content.getCType() == CType.Comment) {
                // ignore comments
            } else if (content.getCType() == CType.Element) {
                Element element2 = (Element) content;
                if (element2.getName().equals("add")) {
                    loadAdd(element2, addText);
                } else if (element2.getName().equals("del")) {
                    loadDel(element2, addText);
                } else if (element2.getName().equals("subst")) {
                    loadSubst(element2, addText);
                } else if (element2.getName().equals("gap")) {
                    loadGap(element2, addText);
                } else if (element2.getName().equals("unclear")) {
                    loadUnclear(element2, addText);
                } else if (element2.getName().equals("lb")) {
                    loadLb(element2, addText);
                } else if (element2.getName().equals("seg")) {
                    loadSeg(element2, addText);
                } else if (element2.getName().equals("note")) {
                    loadNote(element2, parent);
                } else {
                    throw new LdoDLoadException("não carrega elementos: " + element2 + " do tipo:" + element2.getName()
                            + " dentro de add" + "_VALOR=" + element.getText());

                }
            }
        }
    }

    /**
     * In this project a <seg> element can only contain simple text, it is used for
     * formating and cross referencing
     */
    private void loadSeg(Element element, TextPortion parent) {
        List<Content> contentList = element.getContent();

        if (contentList.size() != 1) {
            throw new LdoDLoadException("seg não contém apenas texto" + element.getText());
        }

        if (contentList.get(0).getCType() != CType.Text) {
            throw new LdoDLoadException("seg não contém apenas texto" + element.getText());
        }

        SegText segText = new SegText(parent);

        Attribute xmlIdAttribute = element.getAttribute("id", element.getNamespace("xml"));
        if (xmlIdAttribute != null) {
            segText.setXmlId(xmlIdAttribute.getValue());
            putObjectDirectIdMap(xmlIdAttribute.getValue(), segText);
        }

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
        TextPortion.VariationType type = getVariationType(rdgGrpElement);

        RdgGrpText rdgGrpText = new RdgGrpText(parent, type);
        for (Element rdgElement : rdgGrpElement.getChildren()) {
            if (rdgElement.getName().equals("rdg")) {
                loadRdg(rdgElement, rdgGrpText);
            } else if (rdgElement.getName().equals("rdgGrp")) {
                loadRdgGrp(rdgElement, rdgGrpText);
            }
        }
    }

    private void loadRdg(Element rdgElement, TextPortion parent) {
        TextPortion.VariationType type = getVariationType(rdgElement);

        Attribute witAttribute = rdgElement.getAttribute("wit");

        if (witAttribute == null) {
            throw new LdoDLoadException("elemento rdg necessita de atributo wit " + rdgElement.getText());
        }

        String witValue = rdgElement.getAttribute("wit").getValue().trim();

        String[] listInterXmlId = witValue.split("\\s+");
        Set<ScholarInter> fragInters = getFragItersByListXmlID(listInterXmlId);

        RdgText rdgText = new RdgText(parent, type, fragInters);

        if (!rdgElement.getContent().isEmpty()) {
            loadElement(rdgElement, rdgText);
        }
    }

    private void loadApp(Element appElement, TextPortion parent) {
        TextPortion.VariationType type = getVariationType(appElement);

        AppText app = new AppText(parent, type);

        for (Element rdgElement : appElement.getChildren()) {
            if (rdgElement.getName().equals("rdg")) {
                loadRdg(rdgElement, app);
            } else if (rdgElement.getName().equals("rdgGrp")) {
                loadRdgGrp(rdgElement, app);
            } else {
                throw new LdoDLoadException("elemento inesperado dentro de app" + " VALOR=" + rdgElement.getName());
                // assert false : "UNEXPECTED ELEMENT NESTED WITHIN APP" +
                // rdgElement.getName();
            }
        }
    }

    private void loadPb(Element element, TextPortion parent) {
        Set<ScholarInter> toFragInters = null;

        Attribute edAttribute = element.getAttribute("ed");
        if (edAttribute == null) {
            toFragInters = parent.getInterps();
        } else {
            String[] listInterXmlId = element.getAttribute("ed").getValue().split("\\s+");
            toFragInters = getFragItersByListXmlID(listInterXmlId);

            for (ScholarInter inter : toFragInters) {
                if (!parent.getInterps().contains(inter)) {
                    throw new LdoDLoadException("testemunho com identificador:" + inter.getXmlId()
                            + " é associado a pb mas não está declarado no contexto do seu rdg");
                }
            }
        }

        PbText pbText = new PbText(parent, toFragInters, this.pbOrder++);

        Attribute facsAttribute = element.getAttribute("facs");
        if (facsAttribute != null) {
            String facsValue = facsAttribute.getValue();

            Surface surface = null;
            for (Source source : parent.getTopParent().getFragment().getSourcesSet()) {
                for (Surface surf : source.getFacsimile().getSurfaces()) {
                    if (surf.getGraphic().equals(facsValue)) {
                        surface = surf;
                        break;
                    }
                }
            }

            if (surface == null) {
                throw new LdoDLoadException("o attributo facs do elemento pb que tem o valor=" + facsValue
                        + " não está associado a nenhum elemento graphic de surface");
            } else {
                pbText.setSurface(surface);
            }
        }

    }

    private void loadLb(Element element, TextPortion parent) {
        Set<ScholarInter> toFragInters = null;

        Attribute edAttribute = element.getAttribute("ed");
        if (edAttribute == null) {
            toFragInters = parent.getInterps();
        } else {
            String xmlsString = element.getAttribute("ed").getValue().trim();
            String[] listInterXmlId = xmlsString.split("\\s+");

            toFragInters = getFragItersByListXmlID(listInterXmlId);

            for (ScholarInter inter : toFragInters) {
                if (!parent.getInterps().contains(inter)) {
                    throw new LdoDLoadException("testemunho com identificador:" + inter.getXmlId()
                            + " é associado a lb mas não está declarado no contexto do seu rdg");
                }
            }
        }

        new LbText(parent, isBreak(element), isHiphenated(element), toFragInters);
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

        String xmlID = paragraph.getAttributeValue("id", paragraph.getNamespace("xml"));

        for (Object obj : getObjectInverseIdMap(xmlID)) {
            PhysNote physNote = (PhysNote) obj;
            physNote.addTextPortion(paragraphText);
        }

        loadElement(paragraph, paragraphText);
    }

    private void loadDiv(Element div, TextPortion parent) {
        loadElement(div, parent);
    }

    private void loadFacsimile(String fragmentTEIID) {
        String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
        String queryExpression = "//def:TEI" + selectThisFragment + "/def:facsimile";

        XPathExpression<Element> xp = this.xpfac.compile(queryExpression, Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element facsElement : xp.evaluate(this.doc)) {
            String xmlID = facsElement.getAttributeValue("id", facsElement.getNamespace("xml"));

            if (xmlID == null) {
                throw new LdoDLoadException("elemento facsimile sem atributo xml:id");
            }

            if (getObjectDirectIdMap(xmlID) != null) {
                throw new LdoDLoadException("o atributo xml:id=" + xmlID + " de facsimile já foi declarado");
            }

            Attribute correspAtt = facsElement.getAttribute("corresp");
            String sourceID = null;
            if (correspAtt != null) {
                sourceID = correspAtt.getValue().substring(1);
            } else {
                throw new LdoDLoadException("elemento facsimile xml:id=" + xmlID + " necessita de atributo corresp");
            }

            List<Object> list = getObjectDirectIdMap(sourceID);

            if (list == null) {
                throw new LdoDLoadException("Referência=" + sourceID + " para testemunho fonte em facsimilexml:id="
                        + xmlID + " não existe");
            }

            Source source = (Source) list.get(0);

            if (source.getFacsimile() != null) {
                throw new LdoDLoadException("Existe mais do que um fac-símile para a fonte com referência=" + sourceID);
            }

            Facsimile facsimile = new Facsimile(source, xmlID);

            putObjectDirectIdMap(sourceID, facsimile);

            loadSurface(facsElement, facsimile);
        }

    }

    private void loadSurface(Element facsElement, Facsimile facsimile) {
        for (Element surfElement : facsElement.getChildren("surface", this.namespace)) {
            Element graphElement = surfElement.getChild("graphic", this.namespace);

            if (graphElement == null) {
                throw new LdoDLoadException("elemento surface não possui elemento graphic");
            }

            String graphic = graphElement.getAttributeValue("url");
            Attribute xmlIdAttribute = graphElement.getAttribute("id", graphElement.getNamespace("xml"));
            if (xmlIdAttribute != null) {
                String xmlId = xmlIdAttribute.getValue();
                Surface surface = new Surface(facsimile, graphic, xmlId);

                // when loading ref in annex notes in witnesses the surface was
                // not created yet
                for (Object refText : getObjectInverseIdMap(xmlId)) {
                    ((RefText) refText).setSurface(surface);
                }
            } else {
                new Surface(facsimile, graphic);
            }
        }

    }

    // TODO: a cleaner way to read parent's xmlID
    // TODO: its is necessary to refactor this part of the domain, define what
    // is common in FragInter
    private void loadWitnesses(Fragment fragment, String fragmentTEIID) {
        String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
        String queryExpression = "//def:TEI" + selectThisFragment
                + "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listWit/.//def:witness";

        XPathExpression<Element> xp = this.xpfac.compile(queryExpression, Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element witness : xp.evaluate(this.doc)) {
            String sourceOrEditionXmlID = witness.getChild("ref", this.namespace).getAttributeValue("target")
                    .substring(1);

            List<Object> objects = getObjectDirectIdMap(sourceOrEditionXmlID);

            if (objects == null || objects.isEmpty()) {
                throw new LdoDLoadException(
                        "não existe uma fonte declarada para o atributo xml:id=" + sourceOrEditionXmlID);
            }

            assert objects != null && !objects.isEmpty() : "MISSING SOURCE OBJECT FOR xml:id:" + sourceOrEditionXmlID;

            String witnessXmlID = witness.getAttributeValue("id", witness.getNamespace("xml"));
            String witnessListXmlID = witness.getParentElement().getAttributeValue("id", witness.getNamespace("xml"));
            String witnessListXmlID2 = witness.getParentElement().getParentElement().getAttributeValue("id",
                    witness.getNamespace("xml"));

            if (witnessXmlID == null) {
                throw new LdoDLoadException("elemento wit sem atributo xml:id=" + witnessXmlID);
            }
            assert witnessXmlID != null : "MISSING xml:id FOR WITNESS";

            if (getObjectDirectIdMap(witnessXmlID) != null) {
                throw new LdoDLoadException("já está declarado o atributo xml:id=" + witnessXmlID);
            }
            assert getObjectDirectIdMap(witnessXmlID) == null : "xml:id:" + witnessXmlID + " IS ALREADY DECLARED";

            if (witnessListXmlID == null) {
                throw new LdoDLoadException("falta atributo xml:id para listWit");
            }
            assert witnessListXmlID != null : "MISSING xml:id FOR WITNESS LIST";

            if (witnessListXmlID2 == null) {
                throw new LdoDLoadException("falta atributo xml:id para listWit");
            }
            assert witnessListXmlID2 != null : "MISSING xml:id FOR WITNESS LIST";

            Object object = objects.get(0);

            ScholarInter fragInter = null;

            if (object instanceof ManuscriptSource) {
                fragInter = new SourceInter();
                fragInter.setFragment(fragment);
                ((SourceInter) fragInter).setSource((ManuscriptSource) object);
            } else if (object instanceof PrintedSource) {
                fragInter = new SourceInter();
                fragInter.setFragment(fragment);
                ((SourceInter) fragInter).setSource((PrintedSource) object);
            } else {
                // (object instanceof ExpertEdition)

                fragInter = new ExpertEditionInter();
                fragInter.setFragment(fragment);
                ((ExpertEditionInter) fragInter).setExpertEdition((ExpertEdition) object);
            }

            // when loading ref in annex notes in witnesses the witness may
            // not be created yet
            for (Object refText : getObjectInverseIdMap(witnessXmlID)) {
                ((RefText) refText).setScholarInter(fragInter);
            }

            Element bibl = witness.getChild("bibl", this.namespace);
            if (bibl != null) {
                Heteronym heteronym = getHeteronym(bibl);
                if (heteronym != null) {
                    fragInter.setHeteronym(heteronym);
                } else {
                    fragInter.setHeteronym(NullHeteronym.getNullHeteronym());
                }

                Element titleElement = bibl.getChild("title", this.namespace);
                if (titleElement != null && fragInter instanceof ExpertEditionInter) {
                    ((ExpertEditionInter) fragInter).setTitle(titleElement.getTextTrim());
                }

                Element dateElement = bibl.getChild("date", this.namespace);
                if (dateElement != null) {
                    Attribute whenAttribute = dateElement.getAttribute("when");
                    if (whenAttribute == null) {
                        fragInter.setLdoDDate(null);
                    } else {
                        PrecisionType precision = getPrecisionAttribute(dateElement);

                        fragInter.setLdoDDate(new LdoDDate(whenAttribute.getValue(), precision));
                    }
                }

                setBiblScopes(fragInter, bibl);

                setNotes(fragInter, bibl);
            } else {
                fragInter.setHeteronym(NullHeteronym.getNullHeteronym());
            }

            fragInter.setXmlId(witnessXmlID);

            putObjectDirectIdMap(witnessXmlID, fragInter);
            putObjectDirectIdMap(witnessListXmlID, fragInter);
            putObjectDirectIdMap(witnessListXmlID2, fragInter);
        }

    }

    private void setNotes(ScholarInter fragInter, Element bibl) {
        String notes = "";
        List<Element> notesList = bibl.getChildren("note", this.namespace);
        for (Element noteElement : notesList) {
            String typeValue = noteElement.getAttributeValue("type");
            if (typeValue == null) {
                throw new LdoDLoadException("elemento note sem atributo type");
            }
            if (typeValue.equals("physDesc")) {
                notes = notes + noteElement.getTextTrim() + ";";
            } else if (typeValue.equals("annex")) {
                AnnexNote annexNote = new AnnexNote(fragInter);

                NoteText.NoteType type = NoteText.NoteType.ANNEX;
                NoteText noteText = new NoteText(annexNote, type);

                annexNote.setNoteText(noteText);

                for (Content content : noteElement.getContent()) {
                    if (content.getCType() == CType.Text) {
                        if (!content.getValue().trim().equals("")) {
                            loadSimpleText((Text) content, noteText);
                        } else {
                            // empty text
                        }
                    } else if (content.getCType() == CType.Comment) {
                        // ignore comments
                    } else if (content.getCType() == CType.Element) {
                        Element element2 = (Element) content;
                        if (element2.getName().equals("ref")) {
                            RefType refType = getRefType(element2);
                            String target = getRefTarget(element2);

                            RefText refText = new RefText(noteText, refType, target);

                            if (refType == RefType.GRAPHIC) {
                                Surface surface = fragInter.getFragment().getSurface(target);
                                if (surface != null) {
                                    refText.setSurface(surface);
                                } else {
                                    putObjectInverseIdMap(target, refText);
                                }
                            } else if (refType == RefType.WITNESS) {
                                ScholarInter inter = (ScholarInter) fragInter.getFragment().getScholarInterByXmlId(target);
                                if (inter != null) {
                                    refText.setScholarInter(inter);
                                } else {
                                    putObjectInverseIdMap(target, refText);
                                }
                            } else if (refType == RefType.FRAGMENT) {
                                Fragment frag = TextModule.getInstance().getFragmentByXmlId(target);
                                // if (frag != null) {
                                // it is not verified if frag == null but an
                                // exception will be raised when accessing the
                                // fragment
                                refText.setRefFrag(frag);
                                // } else {
                                // throw new LdoDLoadException(
                                // "o valor "
                                // + target
                                // +
                                // " do atributo xml:id do elemento ref não
                                // corresponde ao id the nenhum elemento
                                // fragment."
                                // +
                                // " NOTA: PODERÁ TER QUE CARREGAR ESSE
                                // FRAGMENTO PRIMEIRO");
                                //
                                // }
                            }

                            fragInter.getFragment().getScholarInterByXmlId(target);

                            List<Content> contentList = element2.getContent();

                            loadSimpleText((Text) contentList.get(0), refText);
                        } else {
                            throw new LdoDLoadException("não carrega elementos: " + element2 + " do tipo:"
                                    + element2.getName() + " dentro de note " + " _VALOR=" + noteElement.getText());
                        }
                    }
                }

            } else {
                throw new LdoDLoadException("atributo type de elemento note com valor=" + typeValue);
            }

            if (fragInter instanceof ExpertEditionInter) {
                ((ExpertEditionInter) fragInter).setNotes(notes);
            }
        }
    }

    private void setBiblScopes(ScholarInter fragInter, Element bibl) {
        for (Element biblScope : bibl.getChildren("biblScope", this.namespace)) {
            Attribute unitAtt = biblScope.getAttribute("unit");
            if (unitAtt == null) {
                throw new LdoDLoadException("elemento biblScope sem atributo unit" + " título="
                        + bibl.getChildren("title", this.namespace));
            }
            String value = biblScope.getTextTrim();
            switch (unitAtt.getValue()) {
                case "pp":
                    if (fragInter instanceof ExpertEditionInter) {
                        if (value != null && !value.equals("")) {
                            ((ExpertEditionInter) fragInter).setStartPage(Integer.parseInt(value));
                            ((ExpertEditionInter) fragInter).setEndPage(Integer.parseInt(value));
                        } else {
                            String from = biblScope.getAttributeValue("from");
                            String to = biblScope.getAttributeValue("to");

                            ((ExpertEditionInter) fragInter).setStartPage(Integer.parseInt(from));
                            ((ExpertEditionInter) fragInter).setEndPage(Integer.parseInt(to));
                        }
                    }
                    break;
                case "vol":
                    if (value == null) {
                        value = "";
                    }
                    ((ExpertEditionInter) fragInter).setVolume(value);
                    break;
                case "number":
                    if (value != null && !value.equals("")) {
                        ((ExpertEditionInter) fragInter).setNumber(Integer.parseInt(value.trim()));
                    } else {
                        ((ExpertEditionInter) fragInter).setNumber(0);
                    }
                    break;
                case "subnumber":
                    if (value != null && !value.trim().equals("")) {
                        ((ExpertEditionInter) fragInter).setSubNumber(value.trim());
                    } else {
                        ((ExpertEditionInter) fragInter).setSubNumber("");
                    }
                    break;
            }
        }
    }

    private Heteronym getHeteronym(Element bibl) {
        Heteronym heteronym;
        String hetXmlId = null;
        Element respStmt = bibl.getChild("respStmt", this.namespace);
        if (respStmt != null) {
            Element persName = respStmt.getChild("persName", this.namespace);
            if (persName != null) {
                Attribute correspAtt = persName.getAttribute("corresp");
                if (correspAtt != null) {
                    hetXmlId = persName.getAttributeValue("corresp").substring(1);

                    List<Object> heteronymList = getObjectDirectIdMap(hetXmlId);
                    if (heteronymList != null) {
                        heteronym = (Heteronym) heteronymList.get(0);
                    } else {
                        throw new LdoDLoadException("atributo corresp=" + hetXmlId + " para persName em witness "
                                + bibl.getValue() + " não está declarado");
                    }
                } else {
                    throw new LdoDLoadException("falta atributo corresp para persName em witness" + bibl.getValue());
                }
            } else {
                throw new LdoDLoadException("falta atributo element persName em witness respStmt" + bibl.getValue());
            }
        } else {
            heteronym = NullHeteronym.getNullHeteronym();
        }
        return heteronym;
    }

    private void loadPrintedSources(Fragment fragment, String fragmentTEIID) {
        String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
        String queryExpression = "//def:TEI" + selectThisFragment
                + "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:bibl";

        XPathExpression<Element> xp = this.xpfac.compile(queryExpression, Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element bibl : xp.evaluate(this.doc)) {
            PrintedSource printedSource = new PrintedSource();
            printedSource.setFragment(fragment);

            Element msId = bibl.getChild("msIdentifier", this.namespace);
            loadMsId(msId, printedSource);

            String biblID = bibl.getAttributeValue("id", bibl.getNamespace("xml"));

            if (getObjectDirectIdMap(biblID) != null) {
                throw new LdoDLoadException("já está declarado o atributo xml:id=" + biblID);
            }
            assert getObjectDirectIdMap(biblID) == null : "xml:id:" + biblID + " IS ALREADY DECLARED";

            putObjectDirectIdMap(biblID, printedSource);

            printedSource.setXmlId(biblID);

            Heteronym heteronym = getHeteronym(bibl);
            printedSource.setHeteronym(heteronym);

            for (Element title : bibl.getChildren("title", this.namespace)) {
                Attribute levelAtt = title.getAttribute("level");
                if (levelAtt == null) {
                    throw new LdoDLoadException("elemento title sem level " + " biblID=" + printedSource.getXmlId());
                }
                String value = title.getTextTrim();
                if (value == null || value.equals("")) {
                    throw new LdoDLoadException("elemento title sem valor " + " biblID=" + printedSource.getXmlId());
                }

                switch (levelAtt.getValue()) {
                    case "a":
                        printedSource.setTitle(value);
                        break;
                    case "j":
                        printedSource.setJournal(value);
                        break;
                    default:
                        throw new LdoDLoadException(
                                "elemento biblScope com attributo com valor não esperado " + levelAtt.getValue());
                }
            }

            printedSource.setPubPlace(bibl.getChildText("pubPlace", this.namespace));

            Element dateElement = bibl.getChild("date", this.namespace);
            if (dateElement != null) {
                Attribute whenAttribute = dateElement.getAttribute("when");

                if (whenAttribute == null) {
                    printedSource.setLdoDDate(null);
                } else {
                    PrecisionType precision = getPrecisionAttribute(dateElement);
                    printedSource.setLdoDDate(new LdoDDate(whenAttribute.getValue(), precision));
                }
            } else {
                throw new LdoDException(
                        "A fonte autoral impressa não possui data attibuída " + printedSource.getTitle());
            }

            for (Element biblScope : bibl.getChildren("biblScope", this.namespace)) {
                Attribute unitAtt = biblScope.getAttribute("unit");
                if (unitAtt == null) {
                    throw new LdoDLoadException(
                            "elemento biblScope sem atributo unit" + " título=" + printedSource.getTitle());
                }
                String value = biblScope.getTextTrim();
                switch (unitAtt.getValue()) {
                    case "pp":
                        if (value != null && !value.equals("")) {
                            printedSource.setStartPage(Integer.parseInt(value));
                            printedSource.setEndPage(Integer.parseInt(value));
                        } else {
                            String from = biblScope.getAttributeValue("from");
                            String to = biblScope.getAttributeValue("to");

                            printedSource.setStartPage(Integer.parseInt(from));
                            printedSource.setEndPage(Integer.parseInt(to));
                        }
                        break;
                    case "issue":
                        if (value == null) {
                            throw new LdoDLoadException(
                                    "elemento biblScope issues sem número " + " título=" + printedSource.getTitle());
                        }
                        printedSource.setIssue(value);
                        break;
                    default:
                        throw new LdoDLoadException("elemento biblScope com attributo com valor não esperado "
                                + unitAtt.getValue() + " título=" + printedSource.getTitle());
                }
            }

        }

    }

    private void loadSourceManuscripts(Fragment fragment, String fragmentTEIID) {

        String selectThisFragment = "[@xml:id='" + fragmentTEIID + "']";
        String queryExpression = "//def:TEI" + selectThisFragment
                + "/def:teiHeader/def:fileDesc/def:sourceDesc/def:listBibl/.//def:msDesc";

        XPathExpression<Element> xp = this.xpfac.compile(queryExpression, Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element msDesc : xp.evaluate(this.doc)) {
            ManuscriptSource manuscript = new ManuscriptSource();
            manuscript.setFragment(fragment);

            String manuscriptID = msDesc.getAttributeValue("id", msDesc.getNamespace("xml"));

            if (getObjectDirectIdMap(manuscriptID) != null) {
                throw new LdoDLoadException("já está declarado o atributo xml:id=" + manuscriptID);
            }

            putObjectDirectIdMap(manuscriptID, manuscript);

            manuscript.setXmlId(manuscriptID);

            Element msId = msDesc.getChild("msIdentifier", this.namespace);
            loadMsId(msId, manuscript);

            loadPhysDesc(msDesc, manuscript);

            loadMsHistory(msDesc, manuscript);
        }
    }

    private void loadDimensions(Element supportDesc, ManuscriptSource manuscript) {
        List<Element> dimensionsList = supportDesc.getChild("extent", this.namespace).getChildren("dimensions",
                this.namespace);

        int position = 1;
        for (Element dimensions : dimensionsList) {
            String unit = dimensions.getAttributeValue("unit");
            if (!unit.equals("cm")) {
                throw new LdoDException("As unidades (unit) do elemento dimensions deve ser em cm");
            }

            Float height;
            try {
                height = Float.parseFloat(dimensions.getChildTextTrim("height", this.namespace));
            } catch (NumberFormatException e) {
                throw new LdoDException("O valor de height do elemento dimensions não pode ser "
                        + dimensions.getChildTextTrim("height", this.namespace));
            }

            Float width;
            try {
                width = Float.parseFloat(dimensions.getChildTextTrim("width", this.namespace));
            } catch (NumberFormatException e) {
                throw new LdoDException("O valor de width do elemento dimensions não pode ser "
                        + dimensions.getChildTextTrim("width", this.namespace));
            }

            manuscript.addDimensions(new Dimensions(height, width, ++position));
        }
    }

    private void loadMsHistory(Element msDesc, ManuscriptSource manuscript) {
        manuscript.setLdoDDate(null);

        Element history = msDesc.getChild("history", this.namespace);
        if (history != null) {
            Element origin = history.getChild("origin", this.namespace);

            if (origin != null) {
                Element origDate = origin.getChild("origDate", this.namespace);

                if (origDate != null) {
                    Attribute when = origDate.getAttribute("when");

                    if (when == null) {
                        // TODO: some fragments have the hierarchy but not the
                        // last element
                        // throw new LdoDLoadException(
                        // "Não existe attributo when associado a elemento
                        // msDesc.history.origin.origDate da fonte "
                        // + manuscript.getXmlId());
                    } else {
                        PrecisionType precision = getPrecisionAttribute(origDate);

                        manuscript.setLdoDDate(new LdoDDate(when.getValue(), precision));
                    }
                }
            }
        }
    }

    private void loadMsId(Element msIdentifier, Source source) {
        source.setSettlement(msIdentifier.getChildText("settlement", this.namespace));
        source.setRepository(msIdentifier.getChildText("repository", this.namespace));
        source.setIdno(msIdentifier.getChildText("idno", this.namespace));

        Element altElement = msIdentifier.getChild("altIdentifier", this.namespace);

        if (altElement == null) {
            throw new LdoDLoadException(
                    "falta declarar altIdentifier de um msIdentifier" + " _VALOR_ " + msIdentifier.getContent());
        }

        source.setAltIdentifier(altElement.getChildText("idno", this.namespace));
    }

    private void loadPhysDesc(Element msDesc, ManuscriptSource manuscript) {
        Element physDesc = msDesc.getChild("physDesc", this.namespace);

        Element objectDesc = physDesc.getChild("objectDesc", this.namespace);
        if (objectDesc.getAttributeValue("form").equals("leaf")) {
            manuscript.setForm(ManuscriptSource.Form.LEAF);
        } else {
            throw new LdoDLoadException("não está definido o valor do atributo form="
                    + objectDesc.getAttributeValue("form") + " _VALOR_ " + objectDesc.getContent());
        }

        Element supportDesc = objectDesc.getChild("supportDesc", this.namespace);
        if (supportDesc.getAttributeValue("material").equals("paper")) {
            manuscript.setMaterial(ManuscriptSource.Material.PAPER);
        } else {
            throw new LdoDLoadException("não está definido o valor do atributo material="
                    + supportDesc.getAttributeValue("material") + " _VALOR_ " + supportDesc.getContent());
        }

        if (supportDesc.getChild("extent", this.namespace) != null) {
            loadDimensions(supportDesc, manuscript);
        }

        Element layoutElement = objectDesc.getChild("layoutDesc", this.namespace).getChild("layout", this.namespace);
        manuscript.setColumns(Integer.parseInt(layoutElement.getAttributeValue("columns")));

        if (physDesc.getChild("handDesc", this.namespace) != null) {
            loadHandDesc(manuscript, physDesc);
        }
        if (physDesc.getChild("typeDesc", this.namespace) != null) {
            loadTypeDesc(manuscript, physDesc);
        }

        Element additions = physDesc.getChild("additions", this.namespace);
        if (additions.getTextTrim().equals("VirtualModule")) {
            manuscript.setHasLdoDLabel(true);
        }

        Element binding = physDesc.getChild("bindingDesc", this.namespace).getChild("binding", this.namespace)
                .getChild("p", this.namespace);

        manuscript.setNotes(additions.getTextTrim() + ", " + binding.getTextTrim());
    }

    private void loadTypeDesc(ManuscriptSource manuscript, Element physDesc) {
        Element typeDesc = physDesc.getChild("typeDesc", this.namespace);
        Element typeDescParagraph = typeDesc.getChild("p", this.namespace);
        String stringTypeNote = null;

        if (typeDescParagraph != null) {
            stringTypeNote = typeDescParagraph.getTextTrim();
            TypeNote typeNote = new TypeNote(null, stringTypeNote);
            typeNote.setManuscript(manuscript);
        } else {
            for (Element typeNoteElement : typeDesc.getChildren("typeNote", this.namespace)) {
                String mediumValue = typeNoteElement.getAttributeValue("medium");
                Medium medium = getMedium(mediumValue);

                if (typeNoteElement.getChild("locus", this.namespace) != null) {
                    stringTypeNote = typeNoteElement.getChild("locus", this.namespace).getTextTrim();
                } else if (typeNoteElement.getChild("p", this.namespace) != null) {
                    stringTypeNote = typeNoteElement.getChild("p", this.namespace).getTextTrim();
                } else {
                    throw new LdoDLoadException(
                            "Elemento typeNote sem locus ou p " + "_VALOR_ " + typeNoteElement.getContent());
                }

                TypeNote typeNote = new TypeNote(medium, stringTypeNote);
                typeNote.setManuscript(manuscript);

                if (typeNoteElement.getChild("locus", this.namespace) != null) {
                    String[] targets = typeNoteElement.getChild("locus", this.namespace).getAttributeValue("target")
                            .trim().split("\\s+");
                    for (String target : targets) {
                        putObjectInverseIdMap(target.substring(1), typeNote);
                    }
                }
            }
        }
    }

    private void loadHandDesc(ManuscriptSource manuscript, Element physDesc) {
        Element handDesc = physDesc.getChild("handDesc", this.namespace);
        Element handDescParagraph = handDesc.getChild("p", this.namespace);
        String stringHandNote = null;

        if (handDescParagraph != null) {
            stringHandNote = handDescParagraph.getTextTrim();

            HandNote handNote = new HandNote(null, stringHandNote);
            handNote.setManuscript(manuscript);
        } else {
            for (Element handNoteElement : handDesc.getChildren("handNote", this.namespace)) {
                String mediumValue = handNoteElement.getAttributeValue("medium");
                Medium medium = getMedium(mediumValue);

                if (handNoteElement.getChild("locus", this.namespace) != null) {
                    stringHandNote = handNoteElement.getChild("locus", this.namespace).getTextTrim();
                } else if (handNoteElement.getChild("p", this.namespace) != null) {
                    stringHandNote = handNoteElement.getChild("p", this.namespace).getTextTrim();
                } else {
                    throw new LdoDLoadException(
                            "Elemento handNote sem locus ou p " + "_VALOR_ " + handNoteElement.getContent());
                }

                HandNote handNote = new HandNote(medium, stringHandNote);
                handNote.setManuscript(manuscript);

                if (handNoteElement.getChild("locus", this.namespace) != null) {
                    String[] targets = handNoteElement.getChild("locus", this.namespace).getAttributeValue("target")
                            .trim().split("\\s+");
                    for (String target : targets) {
                        putObjectInverseIdMap(target.substring(1), handNote);
                    }
                }
            }
        }
    }

    private String getRefTarget(Element element) {
        String target = null;
        Attribute targetAttribute = element.getAttribute("target");
        if (targetAttribute != null) {
            target = targetAttribute.getValue();
        } else {
            throw new LdoDLoadException("É necessário o atributo target para o elemento ref");
        }

        if (target != null && !target.equals("")) {
            return target.substring(1);
        } else {
            throw new LdoDLoadException("Falta o valor do atributo target para o elemento ref");
        }

    }

    private PrecisionType getPrecisionAttribute(Element date) {
        Attribute precisionAttribute = date.getAttribute("precision");
        if (precisionAttribute != null) {
            switch (precisionAttribute.getValue()) {
                case "high":
                    return PrecisionType.HIGH;
                case "medium":
                    return PrecisionType.MEDIUM;
                case "low":
                    return PrecisionType.LOW;
                case "unknown":
                    return PrecisionType.UNKNOWN;
                default:
                    throw new LdoDLoadException("valor inesperado para atribute precison=" + precisionAttribute.getValue());
            }
        } else {
            return null;
        }
    }

    private RefType getRefType(Element element) {
        RefType type = null;

        String typeValue = null;
        Attribute typeAttribute = element.getAttribute("type");
        if (typeAttribute != null) {
            typeValue = typeAttribute.getValue();
        } else {
            throw new LdoDLoadException("o elemento ref deve ter valor para o atributo type");
        }

        switch (typeValue) {
            case "graphic":
                type = RefType.GRAPHIC;
                break;
            case "witness":
                type = RefType.WITNESS;
                break;
            case "fragment":
                type = RefType.FRAGMENT;
                break;
            default:
                throw new LdoDLoadException("valor inesperado para atribute type do elemento ref=" + typeValue);
        }

        return type;
    }

    private NoteText.NoteType getNoteType(Element element) {
        NoteText.NoteType type = null;

        String typeValue = null;
        Attribute typeAttribute = element.getAttribute("type");
        if (typeAttribute != null) {
            typeValue = typeAttribute.getValue();
        }

        if (typeValue == null) {
            throw new LdoDLoadException("elemento note não possui atributo type");
        }

        switch (typeValue) {
            case "annex":
                type = NoteText.NoteType.ANNEX;
                break;
            default:
                throw new LdoDLoadException("valor inesperado para atribute type do elemento note=" + typeValue);
        }

        return type;
    }

    private Medium getMedium(String mediumValue) {
        Medium medium = null;
        switch (mediumValue) {
            case "pen":
                medium = Medium.PEN;
                break;
            case "pencil":
                medium = Medium.PENCIL;
                break;
            case "blue-ink":
                medium = Medium.BLUE_INK;
                break;
            case "black-ink":
                medium = Medium.BLACK_INK;
                break;
            case "violet-ink":
                medium = Medium.VIOLET_INK;
                break;
            case "red-ink":
                medium = Medium.RED_INK;
                break;
            case "green-ink":
                medium = Medium.GREEN_INK;
                break;
            default:
                throw new LdoDLoadException("valor inesperado para atribute medium=" + mediumValue);
        }
        return medium;
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
            throw new LdoDLoadException("valor inesperado para atribute hyphenated=" + hyphenated);

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
            throw new LdoDLoadException("valor inesperado para atribute break=" + breakWord);
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
                throw new LdoDLoadException("valor para atributo quantity não é um número="
                        + quantityAttribute.getValue() + " _APLICADO AO VALOR_ " + element.getText());
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
            throw new LdoDLoadException("valor desconhecido para atributo unit=" + unitAttribute.getValue()
                    + " _APLICADO AO VALOR_ " + element.getText());
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
            throw new LdoDLoadException("valor desconhecido para atributo dim=" + dimAttribute.getValue()
                    + " _APLICADO AO VALOR_ " + element.getText());

            // assert false : "UNKNOWN VALUE FOR ATTRIBUTE dim=\""
            // + dimAttribute.getValue() + "\"";
        }
        return dim;
    }

    private void setRenditions(Element element, TextPortion text) {
        String[] listRendXmlId = null;

        Attribute rendAttribute = element.getAttribute("rendition");

        if (rendAttribute != null) {
            listRendXmlId = element.getAttribute("rendition").getValue().split("\\s+");

            for (int i = listRendXmlId.length - 1; i >= 0; i--) {
                String rendXmlId = listRendXmlId[i].substring(1);

                if (rendXmlId.equals("right")) {
                    text.addRend(new Rend(Rend.Rendition.RIGHT));
                } else if (rendXmlId.equals("left")) {
                    text.addRend(new Rend(Rend.Rendition.LEFT));
                } else if (rendXmlId.equals("center")) {
                    text.addRend(new Rend(Rend.Rendition.CENTER));
                } else if (rendXmlId.equals("bold")) {
                    text.addRend(new Rend(Rend.Rendition.BOLD));
                } else if (rendXmlId.equals("i")) {
                    text.addRend(new Rend(Rend.Rendition.ITALIC));
                } else if (rendXmlId.equals("red")) {
                    text.addRend(new Rend(Rend.Rendition.RED));
                } else if (rendXmlId.equals("green")) {
                    text.addRend(new Rend(Rend.Rendition.GREEN));
                } else if (rendXmlId.equals("u")) {
                    text.addRend(new Rend(Rend.Rendition.UNDERLINED));
                } else if (rendXmlId.equals("super")) {
                    text.addRend(new Rend(Rend.Rendition.SUPERSCRIPT));
                } else if (rendXmlId.equals("sub")) {
                    text.addRend(new Rend(Rend.Rendition.SUBSCRIPT));
                } else {
                    throw new LdoDLoadException("valor desconhecido para rend=" + listRendXmlId[i]
                            + " _APLICADO AO VALOR_ " + element.getText());
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
            } else if (placeValue.equals("inspace")) {
                place = Place.INSPACE;
            } else if (placeValue.equals("overleaf")) {
                place = Place.OVERLEAF;
            } else if (placeValue.equals("opposite")) {
                place = Place.OPPOSITE;
            } else if (placeValue.equals("end")) {
                place = Place.END;
            } else {
                throw new LdoDLoadException("valor desconhecido para atributo place=" + placeValue
                        + " _APLICADO AO VALOR_ " + placeAttribute.getParent().getText());
            }
        }
        return place;
    }

    private DelText.HowDel getHowDelAttribute(Attribute howDelAttribute) {
        DelText.HowDel howDel = DelText.HowDel.UNSPECIFIED;
        if (howDelAttribute != null) {
            String howDelValue = howDelAttribute.getValue();

            if (howDelValue.equals("overtyped")) {
                howDel = DelText.HowDel.OVERTYPED;
            } else if (howDelValue.equals("overstrike")) {
                howDel = DelText.HowDel.OVERSTRIKE;
            } else if (howDelValue.equals("overwritten")) {
                howDel = DelText.HowDel.OVERWRITTEN;
            } else {
                throw new LdoDLoadException("valor desconhecido para atributo rend=" + howDelValue + " dentro de del"
                        + " _APLICADO AO VALOR_ " + howDelAttribute.getParent().getText());
            }
        }
        return howDel;
    }

    private TextPortion.VariationType getVariationType(Element appElement) {
        TextPortion.VariationType type = TextPortion.VariationType.UNSPECIFIED;

        Attribute typeAttribute = appElement.getAttribute("type");

        if (typeAttribute != null) {
            String typeValue = typeAttribute.getValue();

            switch (typeValue) {
                case "ortographic":
                    // due to a error in the encoding both forms are allowed
                case "orthographic":
                    type = TextPortion.VariationType.ORTHOGRAPHIC;
                    break;
                case "substantive":
                    type = TextPortion.VariationType.SUBSTANTIVE;
                    break;
                case "style":
                    type = TextPortion.VariationType.STYLE;
                    break;
                case "paragraph":
                    type = TextPortion.VariationType.PARAGRAPH;
                    break;
                case "punctuation":
                    type = TextPortion.VariationType.PUNCTUATION;
                    break;
                default:
                    throw new LdoDLoadException("valor desconhecido para atributo type=" + typeValue + " dentro de app");
            }
        }

        return type;
    }

    private GapReason getGapReasonAttribute(Element element) {
        GapReason reason = GapReason.NONSPECIFIED;

        Attribute reasonAttribute = element.getAttribute("reason");

        if (reasonAttribute != null) {
            String reasonValue = reasonAttribute.getValue();

            switch (reasonValue) {
                case "irrelevant":
                    reason = GapReason.IRRELEVANT;
                    break;
                case "illegible":
                    reason = GapReason.ILLEGIABLE;
                    break;
                default:
                    throw new LdoDLoadException(
                            "valor desconhecido para atributo reason=" + reasonValue + " dentro de gap");
            }
        }
        return reason;
    }

    private int getExtentAttribute(Element element) {
        int extent = 0;

        Attribute extentAttribute = element.getAttribute("extent");

        if (extentAttribute != null) {
            try {
                extent = Integer.parseInt(extentAttribute.getValue());
            } catch (NumberFormatException e) {
                throw new LdoDLoadException("valor para atributo extent de um element gap não é um número="
                        + extentAttribute.getValue() + " _APLICADO AO VALOR_ " + element.getParent().getContent());
            }
        }
        return extent;
    }

    private GapUnit getGapUnitAttribute(Element element) {
        GapUnit unit = GapUnit.NONSPECIFIED;

        Attribute unitAttribute = element.getAttribute("unit");

        if (unitAttribute != null) {
            String unitValue = unitAttribute.getValue();

            switch (unitValue) {
                case "word":
                    unit = GapUnit.WORD;
                    break;
                case "char":
                    unit = GapUnit.CHAR;
                    break;
                default:
                    throw new LdoDLoadException("valor desconhecido para atributo unit=" + unitValue + " dentro de gap");
            }
        }
        return unit;
    }

    private UnclearText.UnclearReason getUnclearReasonAttribute(Element element) {
        UnclearText.UnclearReason reason = UnclearText.UnclearReason.NONSPECIFIED;

        Attribute reasonAttribute = element.getAttribute("reason");

        if (reasonAttribute != null) {
            String reasonValue = reasonAttribute.getValue();

            switch (reasonValue) {
                case "irrelevant":
                    reason = UnclearText.UnclearReason.IRRELEVANT;
                    break;
                case "illegible":
                    reason = UnclearText.UnclearReason.ILLEGIABLE;
                    break;
                case "background noise":
                    reason = UnclearText.UnclearReason.BACKGROUND_NOISE;
                    break;
                case "eccentric ductus":
                    reason = UnclearText.UnclearReason.ECCENTRIC_DUCTUS;
                    break;
                case "ink blot":
                    reason = UnclearText.UnclearReason.INK_BLOT;
                    break;
                case "faded":
                    reason = UnclearText.UnclearReason.FADED;
                    break;
                default:
                    throw new LdoDLoadException(
                            "valor desconhecido para atributo reason=" + reasonValue + " dentro de unclear");
            }
        }
        return reason;
    }

    private AltMode getAltMode(Element element) {
        AltMode altMode = AltMode.NONSPECIFIED;

        Attribute modeAttribute = element.getAttribute("mode");
        if (modeAttribute == null) {
            throw new LdoDLoadException("O elemento alt deve ter attributo mode");
        }

        String modeValue = modeAttribute.getValue();
        switch (modeValue) {
            case "excl":
                altMode = AltMode.EXCL;
                break;
            case "incl":
                altMode = AltMode.INCL;
                break;
            default:
                throw new LdoDLoadException("O atributo mode do elemento alt tem valor=" + modeValue);
        }
        return altMode;
    }

    private String[] getTarget(Element element) {
        Attribute targetAttribute = element.getAttribute("target");
        if (targetAttribute == null) {
            throw new LdoDLoadException("O elemento alt deve ter attributo target");
        }
        String targetValue = targetAttribute.getValue().trim();
        String[] targetList = targetValue.split("\\s+");

        if (targetList.length < 2) {
            throw new LdoDLoadException(
                    "O atributo target do elemento alt deve ter pelo menos dois valores. Valor=" + targetValue);
        }
        return targetList;
    }

    private String[] getAltWeights(Element element) {
        Attribute weightsAttribute = element.getAttribute("weights");
        if (weightsAttribute == null) {
            throw new LdoDLoadException("O elemento alt deve ter attributo weights");
        }
        String weightsValue = weightsAttribute.getValue();
        String[] weightsList = weightsValue.split("\\s+");
        if (weightsList.length < 2) {
            throw new LdoDLoadException(
                    "O atributo weights do elemento alt deve ter pelo menos dois valores. Valor=" + weightsValue);
        }

        double total = 0;
        for (String weight : weightsList) {
            total = total + Double.parseDouble(weight);
        }

        if (total != 1) {
            throw new LdoDLoadException(
                    "No atributo weights do elemento alt a soma dos valores deve ser igual a 1. Valor=" + weightsValue);
        }

        return weightsList;
    }

    private String getFragmentTitle(Element element) {
        String title = element.getChild("fileDesc", this.namespace).getChild("titleStmt", this.namespace)
                .getChildText("title", this.namespace);
        return title;
    }

    private String getFragmentXmlId(Element element) {
        String xmlId = element.getParentElement().getAttributeValue("id", element.getNamespace("xml"));

        if (xmlId == null) {
            throw new LdoDLoadException(
                    "falta xml:id de um fragmento" + " VALOR=" + element.getChild("fileDesc", this.namespace)
                            .getChild("titleStmt", this.namespace).getChildText("title", this.namespace));
        }

        assert xmlId != null : "MISSING xml:id ATTRIBUTE IN FRAGMENT <TEI > ELEMENT";

        if (getObjectDirectIdMap(xmlId) != null) {
            throw new LdoDLoadException("xml:id:" + xmlId + " já está declarado");
        }
        return xmlId;
    }
}
