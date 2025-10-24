package service;

import model.Adjunto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AdjuntoService {

    private static final String MEDIA_DIR = "media";

    /**
     * Guarda una copia del archivo que el usuario selecciona (imagen, pdf, etc.)
     * dentro de la carpeta "media" del proyecto.
     * Devuelve un objeto Adjunto con la información del archivo.
     * @param rutaOriginal Ruta del archivo original en el equipo.
     * @return El objeto Adjunto con nombre, ruta y tipo del archivo.
     */
    public Adjunto guardarAdjunto(String rutaOriginal) {
        File archivoOriginal = new File(rutaOriginal);
        if (!archivoOriginal.exists()) {
            System.out.println("El archivo no existe: " + rutaOriginal);
            return null;
        }

        File carpetaMedia = new File(MEDIA_DIR);
        if (!carpetaMedia.exists()) {
            carpetaMedia.mkdirs();
        }

        File destino = new File(carpetaMedia, archivoOriginal.getName());

        try (FileInputStream fis = new FileInputStream(archivoOriginal);
             FileOutputStream fos = new FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int bytesLeidos;
            while ((bytesLeidos = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesLeidos);
            }

            System.out.println("Archivo guardado en: " + destino.getAbsolutePath());

            String tipo = obtenerTipoArchivo(destino.getName());
            // ¡CORRECCIÓN CLAVE! Guardar la ruta como una URI válida.
            String uri = destino.toURI().toString();
            return new Adjunto(destino.getName(), uri, tipo);

        } catch (IOException e) {
            System.out.println("Error al guardar adjunto: " + e.getMessage());
            return null;
        }
    }

    /**
     * Determina el tipo de archivo según su extensión.
     * Sirve para saber si es una imagen, documento, video, etc.
     * @param name Nombre del archivo (con extensión).
     * @return Tipo de archivo como texto (imagen, pdf, documento, video, otro).
     */
    private String obtenerTipoArchivo(String name) {
        String lowerCaseName = name.toLowerCase();
        if (lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".gif")) {
            return "imagen";
        } else if (lowerCaseName.endsWith(".pdf")) {
            return "pdf";
        } else if (lowerCaseName.endsWith(".doc") || lowerCaseName.endsWith(".docx")) {
            return "documento";
        } else if (lowerCaseName.endsWith(".mp4") || lowerCaseName.endsWith(".avi")) {
            return "video";
        } else {
            return "otro";
        }
    }
}
