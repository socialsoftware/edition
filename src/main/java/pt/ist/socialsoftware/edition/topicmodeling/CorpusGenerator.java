package pt.ist.socialsoftware.edition.topicmodeling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.generators.PlainTextFragmentWriter;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

public class CorpusGenerator {

	public void generate(Fragment fragment) throws FileNotFoundException, IOException {
		String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
		File directory = new File(corpusFilesPath);

		// use the representative interpretation
		SourceInter sourceInter = fragment.getRepresentativeSourceInter();

		File file = new File(directory, sourceInter.getExternalId() + ".txt");

		// delete file if it already exists, for a clean generation
		if (file.exists())
			file.delete();

		PlainTextFragmentWriter writer = new PlainTextFragmentWriter(sourceInter);
		writer.write();

		file = new File(directory, sourceInter.getExternalId() + ".txt");
		try (FileOutputStream out = new FileOutputStream(file)) {
			out.write(writer.getTranscription().getBytes());
		}

	}

}
