package pt.ist.socialsoftware.edition.game.feature.classification.inout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;


import java.io.*;
import java.nio.file.Files;
import java.util.zip.*;

public class WriteGamestoFile {
    private static final Logger logger = LoggerFactory.getLogger(WriteGamestoFile.class);

    @Atomic
    public void exportToVirtualZip(String filename) {
        String exportDir = PropertiesManager.getProperties().getProperty("export.dir");
        File directory = new File(exportDir);
        File virtualZip = new File(directory, filename);

        GameXMLExport gameXMLExport = new GameXMLExport();

        String export = gameXMLExport.export();

        // "inject" game info file into virtual zip export so it becomes invisible to virtual module
        ZipOutputStream zos;
        ZipInputStream zis;
        try {
            // copy contents of export zip file to temp file to facilitate manipulation
            File tempFile = File.createTempFile("temp-zip", ".zip");
            zos = new ZipOutputStream(new FileOutputStream(tempFile));
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

            // delete original zip and create new file with the same name
            Files.deleteIfExists(virtualZip.toPath());
            File newZip = new File(directory, filename);

            // copy all files from the temp zip to the new file and add the new game xml export
            zos = new ZipOutputStream(new FileOutputStream(newZip));
            zis = new ZipInputStream(new FileInputStream(tempFile));
            for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                zos.putNextEntry(ze);
                byte[] buffer = new byte[1024];
                for (int read = zis.read(buffer); read != -1; read = zis.read(buffer)) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            }
            byte[] exportBytes = export.getBytes();
            ZipEntry ze = new ZipEntry("games.xml");
            zos.putNextEntry(ze);
            zos.write(exportBytes, 0, exportBytes.length);
            zos.closeEntry();

            zos.close();
            zis.close();

        } catch (IOException e) {
            logger.debug(e.getMessage());
            throw new LdoDException("Failed to locate virtual zip export file");
        }
    }
}
