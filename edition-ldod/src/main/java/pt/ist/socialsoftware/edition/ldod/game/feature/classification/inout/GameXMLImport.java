package pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDLoadException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GameXMLImport {
    private static final Logger logger = LoggerFactory.getLogger(GameXMLImport.class);

    private ClassificationModule classificationModule = null;
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
        this.classificationModule = ClassificationModule.getInstance();
        this.namespace = doc.getRootElement().getNamespace();

        //TODO: Actually import the games from the source

        logger.debug("CLASSIFICATION GAMES ARE NOT BEING IMPORTED.");

        return null;
    }
}
