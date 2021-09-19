package pt.ist.socialsoftware.edition.text.feature.topicmodelling;

import cc.mallet.pipe.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TopicModeler {

    private static final Logger logger = LoggerFactory.getLogger(TopicModeler.class);

    private Pipe pipe;
    private final String corpusPath = PropertiesManager.getProperties().getProperty("corpus.dir");
    private final String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
    private final String stopListPath = PropertiesManager.getProperties().getProperty("corpus.stoplist");

    public TopicModeler() {}

    public void deleteFile(String externalId) {
        if (Files.exists(Paths.get(this.corpusFilesPath + externalId + ".txt"))) {
            try {
                Files.delete(Paths.get(this.corpusFilesPath + externalId + ".txt"));
            } catch (IOException e) {
                throw new LdoDException(
                        "TopicModeler.deleteFile cannot delete file " + this.corpusFilesPath + externalId + ".txt");
            }
        }
    }

}
