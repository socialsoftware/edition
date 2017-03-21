package pt.ist.socialsoftware.edition.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ist.socialsoftware.edition.export.UsersXMLExport;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

@Component
public class ScheduledTasks {
	private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Scheduled(cron = "0 0 10,18 * * *")
	public void reportCurrentTime() throws IOException {
		String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		File directory = new File(exportDir);
		File file = new File(directory, "users-" + timeStamp + ".xml");

		UsersXMLExport generator = new UsersXMLExport();
		try (FileOutputStream out = new FileOutputStream(file)) {
			out.write(generator.export().getBytes());
		}
	}

}