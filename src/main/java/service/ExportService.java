package service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import model.Conversacion;
import model.Mensaje;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ExportService {
    private final File archivoMensajes = new File("data/mensajes.xml");

    private Conversacion cargarConversacion() {
        try {
            if (!archivoMensajes.exists()) {
                return new Conversacion();
            }
            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Conversacion) unmarshaller.unmarshal(archivoMensajes);

        } catch (Exception e) {
            System.out.println("Error al cargar mensajes desde XML: "+ e.getMessage());
            return new Conversacion();
        }
    }
    public void exportarConversacion(String usuario1, String usuario2) {
        Conversacion conversacion = cargarConversacion();

        // Filtrar los mensajes entre los dos usuarios
        List<Mensaje> mensajes = conversacion.getMensajes().stream()
                .filter(m ->
                        (m.getRemitente().equalsIgnoreCase(usuario1) && m.getDestinatario().equalsIgnoreCase(usuario2)) ||
                                (m.getRemitente().equalsIgnoreCase(usuario2) && m.getDestinatario().equalsIgnoreCase(usuario1))
                )
                .collect(Collectors.toList());

        if (mensajes.isEmpty()) {
            System.out.println("No hay mensajes entre " + usuario1 + " y " + usuario2);
            return;
        }

        File carpeta = new File("exported");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        // Pedir al usuario un nombre de archivo
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce un nombre para el archivo (sin extensión): ");
        String nombreUsuario = sc.nextLine().trim();

        String nombreArchivo;
        if (nombreUsuario.isEmpty()) {
            nombreArchivo = "exported/conversacion_" + usuario1 + "_" + usuario2 + ".txt";
        } else {
            nombreArchivo = "exported/" + nombreUsuario + ".txt";
        }

        // Escribir los mensajes en el archivo de texto
        try(FileWriter writer = new FileWriter(nombreArchivo)) {
            for (Mensaje mensaje : mensajes) {
                writer.write(mensaje.getRemitente() + " a " + mensaje.getDestinatario() + ": " + mensaje.getContenido() + "\n");
            }
            System.out.println("Conversacion exportada a " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al exportar la conversacion: " + e.getMessage());

        }
    }

}
