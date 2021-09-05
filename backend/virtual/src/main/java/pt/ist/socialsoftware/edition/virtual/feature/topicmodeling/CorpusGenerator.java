package pt.ist.socialsoftware.edition.virtual.feature.topicmodeling;


import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CorpusGenerator {



    public void generate(FragmentDto fragment) throws FileNotFoundException, IOException {
        generateCorpus(fragment);
        generateInters(fragment);
    }


    public void generateCorpus(FragmentDto fragment) throws FileNotFoundException, IOException {
        String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
        File corpusDirectory = new File(corpusFilesPath);

        // use the representative interpretation for corpus
        ScholarInterDto sourceInter = fragment.getSortedSourceInter().get(fragment.getSortedSourceInter().size() - 1);

        File file = new File(corpusDirectory, sourceInter.getExternalId() + ".txt");

        // delete file if it already exists, for a clean generation
        if (file.exists()) {
            file.delete();
        }

        String transcription = VirtualRequiresInterface.getInstance().getWriteFromPlainTextFragmentWriter(sourceInter.getXmlId());

        file = new File(corpusDirectory, sourceInter.getExternalId() + ".txt");
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(transcription.getBytes());
        }
    }


    public void generateInters(FragmentDto fragment) throws FileNotFoundException, IOException {
        String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
        File intersDirectory = new File(intersFilesPath);

        for (ScholarInterDto interDto : fragment.getEmbeddedScholarInterDtos()) {
            File file = new File(intersDirectory, interDto.getExternalId() + ".txt");

            // delete file if it already exists, for a clean generation
            if (file.exists()) {
                file.delete();
            }

            String transcription = VirtualRequiresInterface.getInstance().getWriteFromPlainTextFragmentWriter(interDto.getXmlId());

            file = new File(intersDirectory, interDto.getExternalId() + ".txt");
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(transcription.getBytes());
            }
        }

    }
}
