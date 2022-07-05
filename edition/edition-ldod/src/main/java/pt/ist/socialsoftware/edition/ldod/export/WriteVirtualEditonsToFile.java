package pt.ist.socialsoftware.edition.ldod.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class WriteVirtualEditonsToFile {
	@Atomic
	public String export() throws IOException, FileNotFoundException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
		File directory = new File(exportDir);
		String filename = "virtualeditions-" + timeStamp + ".zip";
		FileOutputStream fos = new FileOutputStream(directory.getPath() + "/" + filename);
		ZipOutputStream zos = new ZipOutputStream(fos);

		zos.putNextEntry(new ZipEntry("users.xml"));
		InputStream in = generateUsersInputStream();
		copyToZipStream(zos, in);
		zos.closeEntry();

		zos.putNextEntry(new ZipEntry("corpus.xml"));
		in = generateCorpusInputStream();
		copyToZipStream(zos, in);
		zos.closeEntry();

		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			zos.putNextEntry(new ZipEntry(fragment.getXmlId() + ".xml"));
			in = generateFragmentInputStream(fragment);
			copyToZipStream(zos, in);
			zos.closeEntry();

		}

		zos.close();

		return filename;
	}

	private void copyToZipStream(ZipOutputStream zos, InputStream in) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}
		in.close();
	}

	private InputStream generateUsersInputStream() throws IOException {
		UsersXMLExport usersExporter = new UsersXMLExport();
		InputStream in = IOUtils.toInputStream(usersExporter.export(), "UTF-8");
		return in;
	}

	private InputStream generateCorpusInputStream() throws IOException {
		VirtualEditionsTEICorpusExport generator = new VirtualEditionsTEICorpusExport();
		InputStream in = IOUtils.toInputStream(generator.export(), "UTF-8");
		return in;
	}

	private InputStream generateFragmentInputStream(Fragment fragment) throws IOException {
		VirtualEditionFragmentsTEIExport generator = new VirtualEditionFragmentsTEIExport();
		InputStream in = IOUtils.toInputStream(generator.exportFragment(fragment), "UTF-8");
		return in;
	}

}
