package service;

import model.Adjunto;

import java.io.*;

public class AdjuntoService {
    public Adjunto guardarAdjunto(String rutaOriginal) {
        File archivo = new File(rutaOriginal);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaOriginal);
            return null;
        }

        File carpetaMedia = new File("media");
        if (!carpetaMedia.exists()) {
            carpetaMedia.mkdir();
        }
        File destino = new File(carpetaMedia, archivo.getName());
        try(FileInputStream fis = new FileInputStream(archivo);
            FileOutputStream fos = new FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int bytesLeidos;
            while  ((bytesLeidos = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesLeidos);
            }
            System.out.println("Archivo guardado en: " + destino.getAbsolutePath());


            String tipo = obtenerTipoArchivo(archivo.getName());
            return new Adjunto(archivo.getName(), destino.getAbsolutePath(), tipo);
            
        } catch (IOException e) {
            System.out.println("Error al guardar adjunto: " + e.getMessage());
            return null;
        }


        }

    private String obtenerTipoArchivo(String name) {
        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")) {
            return "imagen";
        } else if (name.endsWith(".pdf")) {
            return "pdf";
        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
            return "documento";
        } else if (name.endsWith(".mp4") || name.endsWith(".avi")) {
            return "video";
        } else {
            return "otro";
        }
    }
}

