package pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

public class GameXMLExport {
    private static final Logger logger = LoggerFactory.getLogger(GameXMLExport.class);

    Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

    Document jdomDoc = null;

    @Atomic
    public String export() {

        //TODO: Actually export the games from the database

        logger.debug("CLASSIFICATION GAMES ARE NOT BEING EXPORTED.");

        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

        this.jdomDoc = new Document();
        Element rootElement = new Element("teiCorpus");
        rootElement.setNamespace(this.xmlns);
        this.jdomDoc.setRootElement(rootElement);

        VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

        Element profileDesc = new Element("profileDesc", this.xmlns);
        rootElement.addContent(profileDesc);
        for (VirtualEditionInterDto virtualDto : virtualProvidesInterface.getVirtualEditionInterSet()) {
            if (!ClassificationModule.getInstance().getClassificationGamesForInter(virtualDto.getXmlId()).isEmpty()) {
                Element textClass = new Element("textClass", this.xmlns);
                textClass.setAttribute("source", "#" + virtualDto.getXmlId());
                profileDesc.addContent(textClass);

                Element gameList = new Element("classificationGameList", this.xmlns);
                textClass.addContent(gameList);
                for (ClassificationGame game : ClassificationModule.getInstance().getClassificationGamesForInter(virtualDto.getXmlId())) {
                    Element gameElement = new Element("classificationGame", this.xmlns);
                    gameElement.setAttribute("state", game.getState().toString());
                    gameElement.setAttribute("description", game.getDescription());
                    gameElement.setAttribute("dateTime", String.valueOf(game.getDateTime()));
                    gameElement.setAttribute("sync", Boolean.toString(game.getSync()));
                    gameElement.setAttribute("responsible", game.getResponsible());
                    if (game.getTagId() != null) {
                        gameElement.setAttribute("tag", game.getTagId());
                    }
                    ClassificationGameParticipant participant = game.getClassificationGameParticipantSet().stream()
                            .filter(ClassificationGameParticipant::getWinner).findFirst().orElse(null);
                    gameElement.setAttribute("winningUser",
                            participant != null ? participant.getPlayer().getUser() : " ");

                    exportClassificationGameRounds(gameElement, game);
                    exportClassificationGameParticipants(gameElement, game);
                    gameList.addContent(gameElement);
                }
            }
        }

        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());
        logger.debug(xml.outputString(rootElement));

        return null;
    }

    private void exportClassificationGameRounds(Element gameElement, ClassificationGame game) {
        Element classificationRoundList = new Element("classificationGameRoundList", this.xmlns);

        game.getAllRounds().forEach(round -> {
            Element roundElement = new Element("classificationGameRound", this.xmlns);
            roundElement.setAttribute("paragraphNumber", Integer.toString(round.getNumber()));
            roundElement.setAttribute("roundNumber", Integer.toString(round.getRound()));
            roundElement.setAttribute("tag", round.getTag());
            roundElement.setAttribute("vote", Double.toString(round.getVote()));
            roundElement.setAttribute("dateTime", String.valueOf(round.getTime()));
            roundElement.setAttribute("username",
                    round.getClassificationGameParticipant().getPlayer().getUser());
            classificationRoundList.addContent(roundElement);
        });
        gameElement.addContent(classificationRoundList);
    }

    private void exportClassificationGameParticipants(Element element, ClassificationGame game) {
        Element classificationParticipantList = new Element("classificationGameParticipantList", this.xmlns);

        for (ClassificationGameParticipant participant : game.getClassificationGameParticipantSet()) {
            Element participantElement = new Element("classificationGameParticipant", this.xmlns);
            participantElement.setAttribute("username", participant.getPlayer().getUser());
            participantElement.setAttribute("winner", Boolean.toString(participant.getWinner()));
            participantElement.setAttribute("score", Double.toString(participant.getScore()));
            classificationParticipantList.addContent(participantElement);
        }

        element.addContent(classificationParticipantList);
    }
}
