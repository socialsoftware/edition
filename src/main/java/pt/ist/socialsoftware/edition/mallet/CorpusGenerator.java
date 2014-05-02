package pt.ist.socialsoftware.edition.mallet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.visitors.TextFragmentWriter;

public class CorpusGenerator {

	public void generate(Edition edition) throws FileNotFoundException,
			IOException {
		String path = PropertiesManager.getProperties().getProperty(
				"corpus.editions.dir");
		File directory = new File(path + edition.getExternalId());

		// delete directory and all its files is it already exists, for a clean
		// generation
		if (directory.exists())
			FileUtils.deleteDirectory(directory);
		directory.mkdirs();

		for (FragInter inter : edition.getIntersSet()) {
			// use original transcription
			// may change when the annotations will be included
			TextFragmentWriter writer = new TextFragmentWriter(
					inter.getLastUsed());
			writer.write();
			// use original transcription identification
			// may change when the annotations will be included
			File file = new File(directory, inter.getLastUsed().getExternalId()
					+ ".txt");
			try (FileOutputStream out = new FileOutputStream(file)) {
				out.write(writer.getTranscription().getBytes());
			}
		}
	}

}
