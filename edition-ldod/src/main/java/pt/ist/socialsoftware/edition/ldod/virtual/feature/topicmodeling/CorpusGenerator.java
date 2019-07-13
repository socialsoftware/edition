package pt.ist.socialsoftware.edition.ldod.virtual.feature.topicmodeling;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainTextFragmentWriter;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

public class CorpusGenerator {

    public void generate(Fragment fragment) throws FileNotFoundException, IOException {
        generateCorpus(fragment);
        generateInters(fragment);
    }

    public void generateCorpus(Fragment fragment) throws FileNotFoundException, IOException {
        String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
        File corpusDirectory = new File(corpusFilesPath);

        // use the representative interpretation for corpus
        SourceInter sourceInter = fragment.getRepresentativeSourceInter();

        File file = new File(corpusDirectory, sourceInter.getExternalId() + ".txt");

        // delete file if it already exists, for a clean generation
        if (file.exists()) {
            file.delete();
        }

        PlainTextFragmentWriter writer = new PlainTextFragmentWriter(sourceInter);
        writer.write();

        file = new File(corpusDirectory, sourceInter.getExternalId() + ".txt");
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(writer.getTranscription().getBytes());
        }
    }

    public void generateInters(Fragment fragment) throws FileNotFoundException, IOException {
        String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
        File intersDirectory = new File(intersFilesPath);

        for (ScholarInter inter : fragment.getScholarInterSet().stream().map(ScholarInter.class::cast).collect(Collectors.toSet())) {

            File file = new File(intersDirectory, inter.getExternalId() + ".txt");

            // delete file if it already exists, for a clean generation
            if (file.exists()) {
                file.delete();
            }

            PlainTextFragmentWriter writer = new PlainTextFragmentWriter(inter);
            writer.write();

            file = new File(intersDirectory, inter.getExternalId() + ".txt");
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(writer.getTranscription().getBytes());
            }
        }

    }
}
