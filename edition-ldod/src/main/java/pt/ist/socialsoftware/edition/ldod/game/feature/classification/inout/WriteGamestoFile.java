package pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

public class WriteGamestoFile {
    private static final Logger logger = LoggerFactory.getLogger(WriteGamestoFile.class);

    @Atomic
    public String exportToVirtualZip(String filename) {
        String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
        File directory = new File(exportDir);
        File virtualZip = new File(directory, filename);

        logger.debug(virtualZip.getAbsolutePath());

        //TODO: fix bug with copying of virtual zip file contents
        /*ZipFile zipFile;
        ZipOutputStream zipOutputStream;
        try {
            zipFile = new ZipFile(virtualZip);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(virtualZip));

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while(entries.hasMoreElements()){
                zipOutputStream.putNextEntry(entries.nextElement());
                zipOutputStream.closeEntry();
            }

            zipFile.close();

            virtualZip.delete();
            zipOutputStream.close();

        } catch (IOException e) {
            throw new LdoDException("Failed to locate virtual zip export file");
        }*/

        GameXMLExport gameXMLExport = new GameXMLExport();

        gameXMLExport.export();

        return null;
    }
}
