package pt.ist.socialsoftware.edition.text.feature.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.notification.utils.DateUtils;
import pt.ist.socialsoftware.edition.notification.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.text.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.text.domain.Heteronym;
import pt.ist.socialsoftware.edition.text.domain.TextModule;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadTEICorpus {

    private Element ldoDTEI = null;
    private Namespace namespace = null;
   // private VirtualModule virtualModule = null;
    private TextModule text = null;

    private Document doc = null;

    private final Map<String, List<Object>> xmlIDMap = new HashMap<>();

    private void putObjectByXmlID(String xmlID, Object object) {

        List<Object> list = this.xmlIDMap.get(xmlID);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(object);

        this.xmlIDMap.put(xmlID, list);
    }

    private List<Object> getObjectsByXmlID(String xmlID) {
        List<Object> objects = this.xmlIDMap.get(xmlID);
        return objects;
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
            throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato");
        }

        if (this.doc == null) {
            LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
            throw ex;
        }

        this.ldoDTEI = this.doc.getRootElement();
        this.namespace = this.ldoDTEI.getNamespace();
    }

    @Atomic(mode = TxMode.WRITE)
    public void loadTEICorpus(InputStream file) {
        parseTEIFile(file);

       // this.virtualModule = VirtualModule.getInstance();
        this.text = TextModule.getInstance();

       // loadTitleStmt();

        loadListBibl();

        loadHeteronyms();

    }

    private void loadHeteronyms() {
        Element corpusHeteronyms = this.ldoDTEI.getChild("teiHeader", this.namespace)
                .getChild("profileDesc", this.namespace).getChild("particDesc", this.namespace)
                .getChild("listPerson", this.namespace);

        for (Element heteronymTEI : corpusHeteronyms.getChildren("person", this.namespace)) {
            String heteronymXmlID = heteronymTEI.getAttributeValue("id", heteronymTEI.getNamespace("xml"));

            if (getObjectsByXmlID(heteronymXmlID) != null) {
                throw new LdoDLoadException("xml:id:" + heteronymXmlID + " já foi declarado");
            }

            assert getObjectsByXmlID(heteronymXmlID) == null : "xml:id:" + heteronymXmlID + " IS ALREADY DECLARED";

            String name = heteronymTEI.getChildText("persName", this.namespace);

            List<Heteronym> heteronymList = this.text.getHeteronymsSet().stream().filter(heteronym -> heteronym.getName().equals(name))
                    .collect(Collectors.toList());

            if (heteronymList.isEmpty()) {
                Heteronym heteronym = new Heteronym(this.text, name);

                putObjectByXmlID(heteronymXmlID, heteronym);

                heteronym.setXmlId(heteronymXmlID);
            }

        }
    }

    private void loadListBibl() {
        Element corpusHeaderListBibl = this.ldoDTEI.getChild("teiHeader", this.namespace)
                .getChild("fileDesc", this.namespace).getChild("sourceDesc", this.namespace)
                .getChild("listBibl", this.namespace);

        String listEditionsXmlID = corpusHeaderListBibl.getAttributeValue("id",
                corpusHeaderListBibl.getNamespace("xml"));

        if (getObjectsByXmlID(listEditionsXmlID) != null) {
            throw new LdoDLoadException("xml:id:" + listEditionsXmlID + " já foi declarado");
        }

        assert getObjectsByXmlID(listEditionsXmlID) == null : "xml:id:" + listEditionsXmlID + " IS ALREADY DECLARED";

        for (Element bibl : corpusHeaderListBibl.getChildren("bibl", this.namespace)) {
            String editionXmlID = bibl.getAttributeValue("id", bibl.getNamespace("xml"));

            if (getObjectsByXmlID(editionXmlID) != null) {
                throw new LdoDLoadException("xml:id:" + editionXmlID + " já foi declarado");
            }

            assert getObjectsByXmlID(editionXmlID) == null : "xml:id:" + editionXmlID + " IS ALREADY DECLARED";

            String author = bibl.getChild("author", this.namespace).getChild("persName", this.namespace).getText();
            String title = bibl.getChild("title", this.namespace).getText();
            String editor = bibl.getChild("editor", this.namespace).getChild("persName", this.namespace).getText();
            LocalDate date = DateUtils.convertDate(bibl.getChild("date", this.namespace).getAttributeValue("when"));

            ExpertEdition edition = new ExpertEdition(this.text, title, author, editor, date);

            edition.setXmlId(editionXmlID);

            putObjectByXmlID(editionXmlID, edition);
            putObjectByXmlID(listEditionsXmlID, edition);

        }
    }

//    private void loadTitleStmt() {
//        Element corpusHeaderTitleStmt = this.ldoDTEI.getChild("teiHeader", this.namespace)
//                .getChild("fileDesc", this.namespace).getChild("titleStmt", this.namespace);
//
////        this.virtualModule.setTitle(corpusHeaderTitleStmt.getChild("title", this.namespace).getText());
////        this.virtualModule.setAuthor(corpusHeaderTitleStmt.getChild("author", this.namespace).getText());
////        this.virtualModule.setEditor(corpusHeaderTitleStmt.getChild("editor", this.namespace).getText());
////        this.virtualModule.setSponsor(corpusHeaderTitleStmt.getChild("sponsor", this.namespace).getText());
////        this.virtualModule.setFunder(corpusHeaderTitleStmt.getChild("funder", this.namespace).getText());
////        this.virtualModule.setPrincipal(corpusHeaderTitleStmt.getChild("principal", this.namespace).getText());
//    }

}