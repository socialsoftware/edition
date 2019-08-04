package pt.ist.socialsoftware.edition.ldod.virtual.feature.inout;

import org.apache.commons.io.IOUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.user.feature.inout.UsersXMLExport;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
        for (FragmentDto fragmentDto : textProvidesInterface.getFragmentDtoSet()) {
            zos.putNextEntry(new ZipEntry(fragmentDto.getXmlId() + ".xml"));
            in = generateFragmentInputStream(fragmentDto.getXmlId());
            copyToZipStream(zos, in);
            zos.closeEntry();

        }

        zos.close();

        Event event = new Event(Event.EventType.VIRTUAL_EXPORT, filename);
        EventInterface eventInterface = new EventInterface();
        eventInterface.publish(event);

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

    private InputStream generateFragmentInputStream(String fragmentXmlId) throws IOException {
        VirtualEditionFragmentsTEIExport generator = new VirtualEditionFragmentsTEIExport();
        InputStream in = IOUtils.toInputStream(generator.exportFragment(fragmentXmlId), "UTF-8");
        return in;
    }

}
