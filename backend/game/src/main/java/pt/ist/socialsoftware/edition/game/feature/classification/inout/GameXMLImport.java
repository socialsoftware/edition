package pt.ist.socialsoftware.edition.game.feature.classification.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;

import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.game.domain.ClassificationGameRound;
import pt.ist.socialsoftware.edition.game.domain.Player;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.utils.LdoDLoadException;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GameXMLImport {
    private static final Logger logger = LoggerFactory.getLogger(GameXMLImport.class);

    private Namespace namespace = null;

    public String importGamesFromTEI(InputStream inputStream) {
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

    public void importGamesFromTEI(String fragmentTEI) {
        SAXBuilder builder = new SAXBuilder();
        builder.setIgnoringElementContentWhitespace(true);

        InputStream stream = new ByteArrayInputStream(fragmentTEI.getBytes());

        importGamesFromTEI(stream);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private String processImport(Document doc) {
        this.namespace = doc.getRootElement().getNamespace();
        importPlayers(doc);
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:textClass", Filters.element(), null,
                Namespace.getNamespace("def", this.namespace.getURI()));


        for (Element textClass : xp.evaluate(doc)) {

//            try {
               VirtualEditionInterDto virtualEditionInter = GameRequiresInterface.getInstance().getVirtualEditionInterFromModule(textClass.getAttributeValue("source").substring(1));
               importClassificationGames(textClass, virtualEditionInter);
//            }catch (LdoDException e) {
//
//            }
        }

        return null;
    }

    private void importPlayers(Document doc) {
        Namespace namespace = doc.getRootElement().getNamespace();
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Element> xp = xpfac.compile("//def:player", Filters.element(), null,
                Namespace.getNamespace("def", namespace.getURI()));
        for (Element playerElement : xp.evaluate(doc)) {
            String user = playerElement.getAttributeValue("user");
            double score = Double.parseDouble(playerElement.getAttributeValue("score"));
            logger.debug("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB" + user);
            Player player = new Player(user);
            player.setScore(score);
        }
    }

    private void importClassificationGames(Element textClass, VirtualEditionInterDto inter) {
        if (textClass.getChild("classificationGameList", this.namespace) == null) {
            return;
        }

        for (Element gameElement : textClass.getChild("classificationGameList", this.namespace).getChildren()) {
            ClassificationGame.ClassificationGameState state = ClassificationGame.ClassificationGameState
                    .valueOf(gameElement.getAttributeValue("state"));
            String description = gameElement.getAttributeValue("description");
            DateTime dateTime = new DateTime(DateTime.parse(gameElement.getAttributeValue("dateTime")));
            boolean sync = Boolean.parseBoolean(gameElement.getAttributeValue("sync"));
            String responsible = gameElement.getAttributeValue("responsible");
            String winner = gameElement.getAttributeValue("winningUser");

            ClassificationGame game = new ClassificationGame(inter.getVirtualEditionDto(), description, dateTime,
                    inter, responsible);

            game.setState(state);
            game.setSync(sync);
            game.setTagId(gameElement.getAttributeValue("tag"));

            importClassificationGameParticipants(gameElement, game);
            importClassificationGameRounds(gameElement, game);
        }

    }

    private void importClassificationGameRounds(Element gameElement, ClassificationGame game) {
        for (Element roundElement : gameElement.getChild("classificationGameRoundList", this.namespace).getChildren()) {
            String username = roundElement.getAttributeValue("username");

            int paragraphNumber = Integer.parseInt(roundElement.getAttributeValue("paragraphNumber"));
            int roundNumber = Integer.parseInt(roundElement.getAttributeValue("roundNumber"));
            String tag = roundElement.getAttributeValue("tag");
            double vote = Double.parseDouble(roundElement.getAttributeValue("vote"));
            DateTime dateTime = new DateTime(DateTime.parse(roundElement.getAttributeValue("dateTime")));

            ClassificationGameRound gameRound = new ClassificationGameRound();
            gameRound.setNumber(paragraphNumber);
            gameRound.setRound(roundNumber);
            gameRound.setTag(tag);
            gameRound.setVote(vote);
            gameRound.setTime(dateTime);

            ClassificationGameParticipant participant = game.getClassificationGameParticipantSet().stream()
                    .filter(p -> p.getPlayer().getUser().equals(username)).findFirst().get();
            gameRound.setClassificationGameParticipant(participant);
        }

    }

    private void importClassificationGameParticipants(Element element, ClassificationGame game) {
        for (Element participantElement : element.getChild("classificationGameParticipantList", this.namespace)
                .getChildren()) {
            String username = participantElement.getAttributeValue("username");
            boolean winner = Boolean.parseBoolean(participantElement.getAttributeValue("winner"));
            double score = Double.parseDouble(participantElement.getAttributeValue("score"));

            ClassificationGameParticipant participant = new ClassificationGameParticipant(game, username);
            participant.setWinner(winner);
            participant.setScore(score);
        }
    }
}
