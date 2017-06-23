package pt.ist.socialsoftware.edition.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.export.UsersXMLExport;
import pt.ist.socialsoftware.edition.export.VirtualEditionFragmentsTEIExport;
import pt.ist.socialsoftware.edition.export.VirtualEditionsTEICorpusExport;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 0 10,18 * * *")
	public void reportCurrentTime() throws IOException {
		String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String exportPath = exportDir + "/" + timeStamp;

		Files.createDirectories(Paths.get(exportPath));
		File directory = new File(exportPath);
		export(directory, exportPath);
	}

	@Atomic
	public void export(File directory, String exportPath) throws IOException, FileNotFoundException {
		File file = new File(directory, "users.xml");
		UsersXMLExport generator = new UsersXMLExport();
		try (FileOutputStream out = new FileOutputStream(file)) {
			out.write(generator.export().getBytes());
		}

		file = new File(directory, "corpus.xml");
		VirtualEditionsTEICorpusExport exportCorpus = new VirtualEditionsTEICorpusExport();
		try (FileOutputStream out = new FileOutputStream(file)) {
			out.write(exportCorpus.export().getBytes());
		}

		Files.createDirectories(Paths.get(exportPath + "/fragments"));
		directory = new File(exportPath + "/fragments");
		VirtualEditionFragmentsTEIExport exportFragment = new VirtualEditionFragmentsTEIExport();
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			file = new File(directory, fragment.getXmlId() + ".xml");
			try (FileOutputStream out = new FileOutputStream(file)) {
				out.write(exportFragment.exportFragment(fragment).getBytes());
			}
		}
	}

}