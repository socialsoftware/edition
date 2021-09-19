package pt.ist.socialsoftware.edition.virtual.feature.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.domain.*;
import pt.ist.socialsoftware.edition.virtual.utils.RangeJson;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class VirtualEditionFragmentsTEIImport {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEditionFragmentsTEIImport.class);

    private static Map<String, ScholarInterDto> scholarInterMap = new HashMap<>();

//    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public static final String SOURCE = "source";

    private VirtualModule virtualModule = null;
    private Namespace namespace = null;

    public String importFragmentFromTEI(InputStream inputStream) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        Document doc;
        try {
            doc = builder.build(inputStream);
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

        return processImport(doc);
    }

    public void importFragmentFromTEI(String fragmentTEI) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        InputStream stream = new ByteArrayInputStream(fragmentTEI.getBytes());

        importFragmentFromTEI(stream);
    }

    @Atomic(mode = TxMode.WRITE)
    private String processImport(Document doc) {

        this.virtualModule = VirtualModule.getInstance();
        this.namespace = doc.getRootElement().getNamespace();


        FragmentDto fragment = getFragment(doc);

       logger.debug("IMPORT FRAGMENT {}", fragment.getXmlId());

        // IMPORT THEM FROM THE FILES IN DISK
        //importFragmentCitations(doc, fragment);

        Map<String, VirtualEditionInter> virtualEditionInterMap = importWitnesses(doc, fragment);

        importTextClasses(doc, virtualEditionInterMap);

        return fragment.getXmlId();
    }

    private Map<String, VirtualEditionInter> importWitnesses(Document doc, FragmentDto fragment) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:witness", Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));
        List<Element> wits = new ArrayList<>(xp.evaluate(doc));

        Map<String, VirtualEditionInter> createdInters = new HashMap<>();

        while (!wits.isEmpty()) {
            Element wit = wits.remove(0);

            String sourceXmlId = wit.getAttributeValue(SOURCE).substring(1);
            System.out.println(sourceXmlId);

            String interXmlId = wit.getAttributeValue("id", Namespace.XML_NAMESPACE);
            String editionAcronym = interXmlId.substring(interXmlId.lastIndexOf("VIRT.") + "VIRT.".length(),
                    interXmlId.lastIndexOf('.'));
            VirtualEdition virtualEdition = this.virtualModule.getVirtualEdition(editionAcronym);

//            ScholarInterDto scholarInter = fragment.getScholarInterByXmlId(sourceXmlId);
            ScholarInterDto scholarInter = VirtualRequiresInterface.getInstance().getScholarInterByXmlId(sourceXmlId);

            VirtualEditionInter virtualEditionInter = createdInters.get(sourceXmlId);

            if (scholarInter == null && virtualEditionInter == null) {
                logger.debug("source xmlId was not created yet: {}", sourceXmlId);
                wits.add(wit);
            }

            if (scholarInter != null) {
//                VirtualEditionInter created = virtualEdition.createVirtualEditionInter(new ScholarInterDto(sourceXmlId),
//                        Integer.parseInt(wit.getChild("num", this.namespace).getAttributeValue("value")));
                VirtualEditionInter created = virtualEdition.createVirtualEditionInter(scholarInter,
                        Integer.parseInt(wit.getChild("num", this.namespace).getAttributeValue("value")));
                if (created == null) {
                    created = virtualEdition.getAllDepthVirtualEditionInters().stream()
                            .filter(virtualEditionInter1 -> virtualEditionInter1.getFragmentXmlId().equals(fragment.getXmlId()))
                            .findAny()
                            .orElse(null);
                }
                createdInters.put(interXmlId, created);
                logger.debug("scholarInter != null id: {}, source: {}, created: {}", interXmlId, sourceXmlId, created);
                logger.debug("virtual edition inter source : {}", created.getXmlId());
            }

            if (virtualEditionInter != null) {
                VirtualEditionInter created = virtualEdition.createVirtualEditionInter(virtualEditionInter,
                        Integer.parseInt(wit.getChild("num", this.namespace).getAttributeValue("value")));
                createdInters.put(interXmlId, created);
                logger.debug("scholarInter == null id: {}, source: {}, created: {}", interXmlId, sourceXmlId, created);

                if (created != null) logger.debug("virtual edition inter source : {}", created.getXmlId());
            }

        }

        return createdInters;
    }

    private void importTextClasses(Document doc, Map<String, VirtualEditionInter> virtualEditionInterMap) {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:textClass", Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));

        for (Element textClass : xp.evaluate(doc)) {
            VirtualEditionInter inter = virtualEditionInterMap.get(textClass.getAttributeValue(SOURCE).substring(1));

            for (Element catRef : textClass.getChildren("catRef", this.namespace)) {
                    importTag(catRef, inter);
            }

            for (Element note : textClass.getChildren("note", this.namespace)) {
                    importAnnotation(note, inter);
            }

            //TODO Address game import from texts
            //importClassificationGames(textClass, inter);
        }
    }


//    private void importFragmentCitations(Document doc, Fragment fragment) {
//        XPathFactory xpfac = XPathFactory.instance();
//        XPathExpression<Element> xp = xpfac.compile("//def:citation", Filters.element(), null,
//                Namespace.getNamespace("def", this.namespace.getURI()));
//        for (Element citation : xp.evaluate(doc)) {
//            if (citation.getAttributeValue("type").equals("twitter")) {
//                String sourceLink = citation.getAttributeValue("sourceLink");
//                String date = citation.getAttributeValue("date");
//
//                Element fragTextElement = citation.getChild("fragText", this.namespace);
//                String fragText = fragTextElement.getTextModule(); // trim() ?
//
//                Element tweetTextElement = citation.getChild("tweetText", this.namespace);
//                String tweetText = tweetTextElement.getTextModule(); // trim() ?
//
//                long tweetID = Long.parseLong(citation.getAttributeValue("tweetId"));
//                String location = citation.getAttributeValue("location");
//                String country = citation.getAttributeValue("country");
//                String username = citation.getAttributeValue("username");
//                String userProfileURL = citation.getAttributeValue("userProfileURL");
//                String userImageURL = citation.getAttributeValue("userImageURL");
//
//                TwitterCitation twitterCitation = new TwitterCitation(fragment, sourceLink, date, fragText, tweetText,
//                        tweetID, location, country, username, userProfileURL, userImageURL);
//
//                // TODO: ciclo for que percorre todos os tweets da nova tag criada na citation e
//                // faz tweet.setTwitterCitation()
//                setTwitterCitation(citation, twitterCitation);
//
//                // TODO suggestion: fazer aqui o import dos info ranges visto que são elementos
//                // criados dentro de cada citation element
//                importInfoRanges(fragment, citation, twitterCitation);
//            }
//
//        }
//
//    }

//    private void importInfoRanges(Fragment fragment, Element citation, TwitterCitation twitterCitation) {
//        for (Element infoRangeElement : citation.getChild("infoRangesList", this.namespace).getChildren()) {
//            String start = infoRangeElement.getAttributeValue("start");
//            int startOffset = Integer.parseInt(infoRangeElement.getAttributeValue("startOffset"));
//            String end = infoRangeElement.getAttributeValue("end");
//            int endOffset = Integer.parseInt(infoRangeElement.getAttributeValue("endOffset"));
//
//            Element quoteElement = infoRangeElement.getChild("quote", this.namespace);
//            String quote = quoteElement.getTextModule(); // trim() ?
//
//            Element textElement = infoRangeElement.getChild("text", this.namespace);
//            String text = textElement.getTextModule(); // trim() ?
//
//            FragInter fragInter = fragment.getFragInterByXmlId(infoRangeElement.getAttributeValue("fragInterXmlId"));
//
//            new InfoRange(twitterCitation, fragInter, start, startOffset, end, endOffset, quote, text);
//        }
//    }

//    private void setTwitterCitation(Element citation, TwitterCitation twitterCitation) {
//        for (Element tweetElement : citation.getChild("tweets", this.namespace).getChildren()) {
//            Tweet tweet = VirtualModule.getInstance()
//                    .getTweetByTweetID(Long.parseLong(tweetElement.getAttributeValue("tweetId")));
//            tweet.setCitation(twitterCitation);
//        }
//    }

    private void importTag(Element catRef, VirtualEditionInter inter) {
        String username = catRef.getAttributeValue("resp").substring(1);
        String tag = catRef.getAttributeValue("target").substring(1);

        inter.getVirtualEdition().getTaxonomy().createTag(inter, tag, null, username);
    }

    // TODO: else if aware - done
    // novo import annotation
    private void importAnnotation(Element note, VirtualEditionInter virtualEditionInter) {
        String text = note.getText().trim();
        Element quoteElement = note.getChild("quote", this.namespace);
        String from = quoteElement.getAttributeValue("from");
        String to = quoteElement.getAttributeValue("to");
        String fromOffset = quoteElement.getAttributeValue("fromOffset");
        String toOffset = quoteElement.getAttributeValue("toOffset");
        String quote = quoteElement.getText().trim();

        RangeJson range = new RangeJson();
        range.setStart(from);
        range.setStartOffset(Integer.parseInt(fromOffset));
        range.setEnd(to);
        range.setEndOffset(Integer.parseInt(toOffset));
        List<RangeJson> rangeList = new ArrayList<>();
        rangeList.add(range);

        if (note.getAttribute("type") != null && note.getAttributeValue("type").equals("human")) {
            logger.debug("resp {}", note.getAttributeValue("resp"));
            String username = note.getAttributeValue("resp").substring(1);
            List<String> tagList = new ArrayList<>();
            for (Element catRef : note.getChildren("catRef", this.namespace)) {
                String tag = catRef.getAttributeValue("target").substring(1);
                tagList.add(tag);
            }
            virtualEditionInter.createHumanAnnotation(quote, text, username, rangeList, tagList);
        }
        //else if (note.getAttributeValue("type").equals("aware")) {
        // do nothing
//            long tweetID = Long.parseLong(note.getAttributeValue("citationId"));
//            Citation citation = inter.getFragment().getCitationById(tweetID);
//            inter.createAwareAnnotation(quote, text, citation, rangeList);
//        }
    }

    // original code
    // private void importAnnotation(Element note, VirtualEditionInter inter) {
    // String username = note.getAttributeValue("resp").substring(1);
    // String text = StringEscapeUtils.escapeHtml(note.getTextModule().trim());
    // Element quoteElement = note.getChild("quote", this.namespace);
    // String from = quoteElement.getAttributeValue("from");
    // String to = quoteElement.getAttributeValue("to");
    // String fromOffset = quoteElement.getAttributeValue("fromOffset");
    // String toOffset = quoteElement.getAttributeValue("toOffset");
    // String quote = quoteElement.getTextModule().trim();
    //
    // RangeJson range = new RangeJson();
    // range.setStart(from);
    // range.setStartOffset(Integer.parseInt(fromOffset));
    // range.setEnd(to);
    // range.setEndOffset(Integer.parseInt(toOffset));
    //
    // List<String> tagList = new ArrayList<>();
    // for (Element catRef : note.getChildren("catRef", this.namespace)) {
    // String tag = catRef.getAttributeValue("target").substring(1);
    // tagList.add(tag);
    // }
    // List<RangeJson> rangeList = new ArrayList<>();
    // rangeList.add(range);
    // inter.createAnnotation(quote, text, this.virtualModule.getUser(username), rangeList,
    // tagList);
    // }

    private FragmentDto getFragment(Document doc) {
        Namespace namespace = doc.getRootElement().getNamespace();
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:TEI", Filters.element(), null,
                Namespace.getNamespace("def", namespace.getURI()));
        String xmlId = xp.evaluate(doc).get(0).getAttributeValue("id", Namespace.XML_NAMESPACE);

        return VirtualRequiresInterface.getInstance().getFragmentByXmlId(xmlId);

        //return TextModule.getInstance().getFragmentByXmlId(xmlId);
    }




////    //TODO : these class game related functions should be moved to their own importer in the game module
//    private void importClassificationGames(Element textClass, VirtualEditionInter inter) {
//        if (textClass.getChild("classificationGameList", this.namespace) == null) {
//            return;
//        }
//
//        for (Element gameElement : textClass.getChild("classificationGameList", this.namespace).getChildren()) {
//            ClassificationGame.ClassificationGameState state = ClassificationGame.ClassificationGameState
//                    .valueOf(gameElement.getAttributeValue("state"));
//            String description = gameElement.getAttributeValue("description");
//            DateTime dateTime = new DateTime(DateTime.parse(gameElement.getAttributeValue("dateTime")));
//            boolean sync = Boolean.parseBoolean(gameElement.getAttributeValue("sync"));
//            String responsible = gameElement.getAttributeValue("responsible");
//            String winner = gameElement.getAttributeValue("winningUser");
//
//            ClassificationGame game = new ClassificationGame(new VirtualEditionDto(inter.getVirtualEdition()), description, dateTime,
//                    new VirtualEditionInterDto(inter), responsible);
//
//            game.setState(state);
//            game.setSync(sync);
//
//            if (winner != null && winner.trim().length() != 0) {
//                logger.debug("winner: {}, tag: {}", winner, gameElement.getAttributeValue("tag"));
//                Tag tag = inter.getTagSet().stream()
//                        .filter(t -> t.getCategory().getName().equals(gameElement.getAttributeValue("tag"))
//                                && t.getContributor().equals(winner))
//                        .findFirst().orElse(null);
//                game.setTagId(tag != null ? tag.getExternalId() : null);
//            }
//            importClassificationGameParticipants(gameElement, game);
//            importClassificationGameRounds(gameElement, game);
//        }
//    }
//
//    private void importClassificationGameRounds(Element gameElement, ClassificationGame game) {
//        for (Element roundElement : gameElement.getChild("classificationGameRoundList", this.namespace).getChildren()) {
//            String username = roundElement.getAttributeValue("username");
//
//            int paragraphNumber = Integer.parseInt(roundElement.getAttributeValue("paragraphNumber"));
//            int roundNumber = Integer.parseInt(roundElement.getAttributeValue("roundNumber"));
//            String tag = roundElement.getAttributeValue("tag");
//            double vote = Double.parseDouble(roundElement.getAttributeValue("vote"));
//            DateTime dateTime = new DateTime(DateTime.parse(roundElement.getAttributeValue("dateTime")));
//
//            ClassificationGameRound gameRound = new ClassificationGameRound();
//            gameRound.setNumber(paragraphNumber);
//            gameRound.setRound(roundNumber);
//            gameRound.setTag(tag);
//            gameRound.setVote(vote);
//            gameRound.setTime(dateTime);
//
//            ClassificationGameParticipant participant = game.getClassificationGameParticipantSet().stream()
//                    .filter(p -> p.getPlayer().getUser().equals(username)).findFirst().get();
//            gameRound.setClassificationGameParticipant(participant);
//        }
//
//    }

//    private void importClassificationGameParticipants(Element element, ClassificationGame game) {
//        for (Element participantElement : element.getChild("classificationGameParticipantList", this.namespace)
//                .getChildren()) {
//            String username = participantElement.getAttributeValue("username");
//            boolean winner = Boolean.parseBoolean(participantElement.getAttributeValue("winner"));
//            double score = Double.parseDouble(participantElement.getAttributeValue("score"));
//
//            ClassificationGameParticipant participant = new ClassificationGameParticipant(game, username);
//            participant.setWinner(winner);
//            participant.setScore(score);
//        }
//    }


//    public void LoadFragmentsCorpus() {
//        this.virtualModule = VirtualModule.getInstance();
//        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
//
//        for (FragmentDto fragmentDto: textProvidesInterface.getFragmentDtoSet()) {
//            Fragment fragment = TextModule.getInstance().getFragmentByXmlId(fragmentDto.getXmlId());
//            // generate corpus in corpus.dir
//            CorpusGenerator generator = new CorpusGenerator();
//            try {
//                generator.generate(fragment);
//            } catch (FileNotFoundException e1) {
//                throw new LdoDLoadException(
//                        "LoadTEIFragments.loadFragment erro FileNotFoundException a gerar corpus do fragmento "
//                                + fragment.getXmlId() + e1.getMessage());
//            } catch (IOException e1) {
//                throw new LdoDLoadException("LoadTEIFragments.loadFragment erro IOException a gerar corpus do fragmento "
//                        + fragment.getXmlId());
//            }
//
//            // generate index in indexer.dir
//            for (ScholarInter inter : fragment.getScholarInterSet().stream().map(ScholarInter.class::cast).collect(Collectors.toSet())) {
//                try {
//                    Indexer indexer = Indexer.getIndexer();
//                    indexer.addDocument(inter);
//
//                } catch (FileNotFoundException e) {
//                    throw new LdoDLoadException(
//                            "LoadTEIFragments.loadFragment erro FileNotFoundException a gerar index da interpretação "
//                                    + inter.getXmlId());
//                } catch (IOException e) {
//                    throw new LdoDLoadException(
//                            "LoadTEIFragments.loadFragment erro IOException a gerar index da interpretação "
//                                    + inter.getXmlId());
//                }
//            }
//
//            VirtualEdition archiveEdition = this.virtualModule.getArchiveEdition();
//            // if the representative fragment interpretation is not in
//            // the archive edition we have to add it
//            if (archiveEdition != null
//                    && !archiveEdition.getIntersSet().contains(fragment.getRepresentativeSourceInter())) {
//                logger.debug("loadFragment ldod-edition-size:{}", archiveEdition.getAllDepthVirtualEditionInters().size());
//                archiveEdition.createVirtualEditionInter(new ScholarInterDto(fragment.getRepresentativeSourceInter().getXmlId()),
//                        archiveEdition.getMaxFragNumber() + 1);
//            }
//        }
//    }

}
