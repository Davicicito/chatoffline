package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipService {

    /**
     * Crea un archivo ZIP con la copia de seguridad del usuario.
     * Dentro del ZIP se guarda el archivo CSV con los mensajes
     * y también todos los archivos de la carpeta "media" (los adjuntos).
     *
     * @param archivoZipDestino Archivo ZIP que se va a crear.
     * @param archivoCsvExportado Archivo CSV que contiene los mensajes.
     * @param carpetaMedia Carpeta con los adjuntos que se incluirán en el ZIP.
     * @return true si todo salió bien, false si hubo algún error.
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

    /**
     * Método auxiliar que añade un archivo individual al ZIP.
     * Se encarga de leer el archivo original y escribirlo dentro del ZIP.
     *
     * @param archivo Archivo que se quiere añadir.
     * @param nombreEnZip Nombre que tendrá dentro del ZIP.
     * @param zipOut Flujo de salida del ZIP (donde se está escribiendo).
     * @throws IOException Si ocurre un error al leer o escribir los datos.
     */
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
