package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipService {

    /**
     * Crea un archivo ZIP en la ruta de destino especificada.
     * @param archivoZipDestino El archivo ZIP que se va a crear (ruta completa).
     * @param archivoCsvExportado El archivo CSV con los mensajes para incluir.
     * @param carpetaMedia La carpeta con los archivos adjuntos para incluir.
     * @return true si el ZIP se creó correctamente, false en caso contrario.
     */
    public boolean crearBackup(File archivoZipDestino, File archivoCsvExportado, String carpetaMedia) {
        try (FileOutputStream fos = new FileOutputStream(archivoZipDestino);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            // 1. Añadir el archivo CSV de la conversación
            if (archivoCsvExportado.exists()) {
                agregarArchivoAlZip(archivoCsvExportado, archivoCsvExportado.getName(), zipOut);
            }

            // 2. Añadir todos los adjuntos de la carpeta de medios
            File dirMedia = new File(carpetaMedia);
            if (dirMedia.exists() && dirMedia.isDirectory()) {
                File[] adjuntos = dirMedia.listFiles();
                if (adjuntos != null) {
                    for (File adjunto : adjuntos) {
                        // Añadir los adjuntos dentro de una carpeta 'media' en el ZIP
                        agregarArchivoAlZip(adjunto, "media/" + adjunto.getName(), zipOut);
                    }
                }
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error al crear el archivo ZIP: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para añadir un archivo al stream del ZIP
    private void agregarArchivoAlZip(File archivo, String nombreEnZip, ZipOutputStream zipOut) throws IOException {
        try (FileInputStream fis = new FileInputStream(archivo)) {
            ZipEntry zipEntry = new ZipEntry(nombreEnZip);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }
}
