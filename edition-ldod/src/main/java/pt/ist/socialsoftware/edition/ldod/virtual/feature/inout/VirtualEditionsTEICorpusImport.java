package pt.ist.socialsoftware.edition.ldod.virtual.feature.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDLoadException;

import java.io.*;
import java.nio.charset.Charset;

public class VirtualEditionsTEICorpusImport {

    public void importVirtualEditionsCorpus(InputStream inputStream) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        Document doc;
        try {
            Reader reader = new InputStreamReader(inputStream, Charset.defaultCharset());
            doc = builder.build(reader);
            //doc = builder.build(inputStream);
        } catch (FileNotFoundException e) {
            throw new LdoDLoadException("Ficheiro não encontrado");
        } catch (JDOMException e) {
            throw new LdoDLoadException("Ficheiro com problemas de codificação TEI");
        } catch (IOException e) {
            throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato");
        }

        if (doc == null) {
            LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
            throw ex;
        }

        processImport(doc);
    }

    public void importVirtualEditionsCorpus(String corpusTEI) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        InputStream stream = new ByteArrayInputStream(corpusTEI.getBytes());

        importVirtualEditionsCorpus(stream);
    }

    @Atomic(mode = TxMode.WRITE)
    private void processImport(Document doc) {
        VirtualModule virtualModule = VirtualModule.getInstance();


        //importPlayers(doc, virtualModule);

        importVirtualEditions(doc, virtualModule);

        importTaxonomies(doc, virtualModule);

        importSocialMediaCriteria(doc, virtualModule);

        // IMPORT THEM FROM THE FILES IN DISK
        //	importTweets(doc, virtualModule);
    }

    // TODO: o construtor recebe a TwitterCitation a null pq ainda não sabe qual é
    // só no import Twitter Citation da outra classe é q é feita esta ligação
//    private void importTweets(Document doc, VirtualModule ldoD) {
//        Namespace namespace = doc.getRootElement().getNamespace();
//        XPathFactory xpfac = XPathFactory.instance();
//        XPathExpression<Element> xp = xpfac.compile("//def:tweet", Filters.element(), null,
//                Namespace.getNamespace("def", namespace.getURI()));
//        for (Element tweet : xp.evaluate(doc)) {
//            String sourceLink = tweet.getAttributeValue("sourceLink");
//            String date = tweet.getAttributeValue("date");
//
//            Element tweetTextElement = tweet.getChild("tweetText", namespace);
//            String tweetText = tweetTextElement.getTextModule(); // trim() ?
//
//            long tweetID = Long.parseLong(tweet.getAttributeValue("tweetId"));
//            String location = tweet.getAttributeValue("location");
//            String country = tweet.getAttributeValue("country");
//            String username = tweet.getAttributeValue("username");
//            String userProfileURL = tweet.getAttributeValue("userProfileURL");
//            String userImageURL = tweet.getAttributeValue("userImageURL");
//
//            long originalTweetID = Long.parseLong(tweet.getAttributeValue("originalTweetId"));
//            boolean isRetweet = Boolean.valueOf(tweet.getAttributeValue("isRetweet"));
//
//            new Tweet(ldoD, sourceLink, date, tweetText, tweetID, location, country, username, userProfileURL,
//                    userImageURL, originalTweetID, isRetweet, null);
//        }
//    }


    /*private void importPlayers(Document doc, VirtualModule virtualModule) {
        Namespace namespace = doc.getRootElement().getNamespace();
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:player", Filters.element(), null,
                Namespace.getNamespace("def", namespace.getURI()));
        for (Element playerElement : xp.evaluate(doc)) {
            String user = playerElement.getAttributeValue("user");
            double score = Double.parseDouble(playerElement.getAttributeValue("score"));
            Player player = new Player(user);
            player.setScore(score);
        }
    }*/

    private void importVirtualEditions(Document doc, VirtualModule virtualModule) {
        Namespace namespace = doc.getRootElement().getNamespace();
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:bibl", Filters.element(), null,
                Namespace.getNamespace("def", namespace.getURI()));
        for (Element bibl : xp.evaluate(doc)) {
            VirtualEdition virtualEdition = null;

            boolean pub = bibl.getAttributeValue("status").equals("PUBLIC") ? true : false;
            String acronym = bibl.getAttributeValue("id", Namespace.XML_NAMESPACE);
            String title = bibl.getChildText("title", namespace);
            String synopsis = bibl.getChildText("synopsis", namespace);
            LocalDate date = LocalDate.parse(bibl.getChild("date", namespace).getAttributeValue("when"));

            String owner = null;
            for (Element editor : bibl.getChildren("editor", namespace)) {
                if (editor.getAttributeValue("role").equals("ADMIN")) {
                    owner = editor.getAttributeValue("nymRef");
                    // if a virtual edition exists with the same name, it is
                    // deleted
                    virtualEdition = virtualModule.getVirtualEdition(acronym);
                    if (virtualEdition != null) {
                        virtualEdition.remove();
                        virtualEdition = null;
                    }
                    virtualEdition = virtualModule.createVirtualEdition(owner, acronym, title, date, pub, null);
                    virtualEdition.setSynopsis(synopsis);
                    break;
                }
            }

            for (Element editor : bibl.getChildren("editor", namespace)) {
                String user = editor.getAttributeValue("nymRef");
                Member.MemberRole role = Member.MemberRole.valueOf(editor.getAttributeValue("role"));
                boolean active = editor.getAttributeValue("active").equals("true") ? true : false;

                Member member = null;
                if (user.equals(owner)) {
                    member = virtualEdition.getMember(user);
                } else {
                    member = new Member(virtualEdition, user, role, active);
                }

                date = LocalDate.parse(editor.getChild("date", namespace).getAttributeValue("when"));
                member.setDate(date);
            }
        }
    }

    private void importTaxonomies(Document doc, VirtualModule virtualModule) {
        Namespace namespace = doc.getRootElement().getNamespace();
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:taxonomy", Filters.element(), null,
                Namespace.getNamespace("def", namespace.getURI()));
        for (Element tax : xp.evaluate(doc)) {
            String xmlId = tax.getAttributeValue("source").substring(1);
            System.out.println(xmlId);
            System.out.println(VirtualModule.getInstance().getVirtualEditionByXmlId(xmlId));
            Taxonomy taxonomy = VirtualModule.getInstance().getVirtualEditionByXmlId(xmlId).getTaxonomy();

            for (Element item : tax.getChild("desc", namespace).getChild("list", namespace).getChildren("item",
                    namespace)) {
                switch (item.getText()) {
                    case "OPEN_MANAGEMENT":
                        taxonomy.setOpenManagement(true);
                        break;
                    case "OPEN_ANNOTATION":
                        taxonomy.setOpenAnnotation(true);
                        break;
                    case "CLOSED_VOCABULARY":
                        taxonomy.setOpenVocabulary(false);
                        break;
                    default:
                        break;
                }
            }

            for (Element cat : tax.getChildren("category", namespace)) {
                new Category().init(taxonomy, cat.getChildText("catDesc", namespace));
            }
        }
    }

    private void importSocialMediaCriteria(Document doc, VirtualModule virtualModule) {
        Namespace namespace = doc.getRootElement().getNamespace();
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:editionCriteria", Filters.element(), null,
                Namespace.getNamespace("def", namespace.getURI()));
        for (Element editionCriteria : xp.evaluate(doc)) {
            String xmlId = editionCriteria.getAttributeValue("source").substring(1);
            System.out.println(xmlId);
            System.out.println(VirtualModule.getInstance().getVirtualEditionByXmlId(xmlId));

            VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEditionByXmlId(xmlId);

            Element mediaSource = editionCriteria.getChild("mediaSource", namespace);
            if (mediaSource != null) {
                new MediaSource(virtualEdition, mediaSource.getAttributeValue("name"));
            }

            Element timeWindow = editionCriteria.getChild("timeWindow", namespace);
            if (timeWindow != null) {
                // TODO: new code to support empty string exported dates
                LocalDate beginDate = null;
                LocalDate endDate = null;
                if (!timeWindow.getAttributeValue("beginDate").equals("")) {
                    beginDate = LocalDate.parse(timeWindow.getAttributeValue("beginDate"));
                }
                if (!timeWindow.getAttributeValue("endDate").equals("")) {
                    endDate = LocalDate.parse(timeWindow.getAttributeValue("endDate"));
                }

                new TimeWindow(virtualEdition, beginDate, endDate);

                // old code
                // new TimeWindow(virtualEdition,
                // LocalDate.parse(timeWindow.getAttributeValue("beginDate")),
                // LocalDate.parse(timeWindow.getAttributeValue("endDate")));
            }

            Element geographicLocation = editionCriteria.getChild("geographicLocation", namespace);
            if (geographicLocation != null) {
                new GeographicLocation(virtualEdition, geographicLocation.getAttributeValue("country"),
                        geographicLocation.getAttributeValue("location"));
            }

            Element frequency = editionCriteria.getChild("frequency", namespace);
            if (frequency != null) {
                new Frequency(virtualEdition, Integer.parseInt(frequency.getAttributeValue("frequency")));
            }

        }
    }

}
