package pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout;

import org.jdom2.Document;
import org.jdom2.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;

public class GameXMLExport {
    private static final Logger logger = LoggerFactory.getLogger(GameXMLExport.class);

    Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

    Document jdomDoc = null;

    @Atomic
    public String export() {

        //TODO: Actually export the games from the database

        logger.debug("CLASSIFICATION GAMES ARE NOT BEING EXPORTED.");

        return null;
    }
}
