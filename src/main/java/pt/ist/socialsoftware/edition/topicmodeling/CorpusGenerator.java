package pt.ist.socialsoftware.edition.topicmodeling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.visitors.PlainTextFragmentWriter;

public class CorpusGenerator {

	public void generate(FragInter inter) throws FileNotFoundException,
			IOException {
		String corpusFilesPath = PropertiesManager.getProperties().getProperty(
				"corpus.files.dir");
		File directory = new File(corpusFilesPath);

		File file = new File(directory, inter.getLastUsed().getExternalId()
				+ ".txt");

		// delete file if it already exists, for a clean generation
		if (file.exists())
			file.delete();

		// use original transcription
		// may be changed when the annotations will be included
		PlainTextFragmentWriter writer = new PlainTextFragmentWriter(inter.getLastUsed());
		writer.write();
		// use original transcription identification
		// may be change when the annotations will be included
		file = new File(directory, inter.getLastUsed().getExternalId() + ".txt");
		try (FileOutputStream out = new FileOutputStream(file)) {
			out.write(writer.getTranscription().getBytes());
		}

	}

}
