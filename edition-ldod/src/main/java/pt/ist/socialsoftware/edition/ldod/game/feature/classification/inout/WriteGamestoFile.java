package pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.io.*;
import java.util.zip.*;

public class WriteGamestoFile {
    private static final Logger logger = LoggerFactory.getLogger(WriteGamestoFile.class);

    @Atomic
    public String exportToVirtualZip(String filename) {
        String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
        File directory = new File(exportDir);
        File virtualZip = new File(directory, filename);

        logger.debug(virtualZip.getAbsolutePath());

        //TODO: uncomment when the new game file is added or a way to append the xml string to an
        // existing file is found
        /*ZipFile zipFile;
        ZipOutputStream zos;
        ZipInputStream zis;
        File newZip = new File(directory, "newZip.zip");
        try {
            zos = new ZipOutputStream(new FileOutputStream(newZip));
            zis = new ZipInputStream(new FileInputStream(virtualZip));
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                    zos.putNextEntry(ze);
                    byte[] buffer = new byte[1024];
                    for (int read = zis.read(buffer); read != -1; read = zis.read(buffer)) {
                        zos.write(buffer, 0, read);
                    }
                    zos.closeEntry();
            }
            zos.close();
            zis.close();

        } catch (IOException e) {
            throw new LdoDException("Failed to locate virtual zip export file");
        }*/

        GameXMLExport gameXMLExport = new GameXMLExport();

        gameXMLExport.export();

        return null;
    }
}
