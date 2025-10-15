package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipService {

    public void crearZip(String NombreConversacion, String rutaExport, String rutaMedia) {

        String nombreZip = "exported/" + NombreConversacion + ".zip";

        try (FileOutputStream fos = new FileOutputStream(nombreZip);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            // Añadir la conversación exportada (.txt)
            File archivoTxt = new File(rutaExport);
            if (archivoTxt.exists()) {
                agregarArchivoAlZip(archivoTxt, archivoTxt.getName(), zipOut);
            }

            // Añadir todos los adjuntos (carpeta /media/)
            File carpetaMedia = new File(rutaMedia);
            if (carpetaMedia.exists() && carpetaMedia.isDirectory()) {
                for (File adjunto : carpetaMedia.listFiles()) {
                    agregarArchivoAlZip(adjunto, "media/" + adjunto.getName(), zipOut);
                }
            }

            System.out.println("Conversación empaquetada en: " + nombreZip);

        } catch (IOException e) {
            System.out.println("Error al crear el ZIP: " + e.getMessage());
        }
    }

    // Método auxiliar para añadir archivos al ZIP
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

