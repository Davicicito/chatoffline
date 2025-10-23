package service;

import model.Conversacion;
import model.Mensaje;
import model.Usuario;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ExportService {

    private final File archivoMensajes = new File("data/mensajes.xml");

    private Conversacion cargarConversacion() {
        try {
            if (!archivoMensajes.exists()) return new Conversacion();

            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Conversacion) unmarshaller.unmarshal(archivoMensajes);
        } catch (Exception e) {
            System.out.println("Error al cargar mensajes desde XML: " + e.getMessage());
            return new Conversacion();
        }
    }

    /**
     * Exporta todos los mensajes enviados y recibidos por un usuario a un archivo CSV.
     * @param usuario El usuario cuyos mensajes se exportarán.
     * @param archivoDestino El archivo donde se guardará el CSV.
     * @return true si la exportación fue exitosa, false en caso contrario.
     */
    public boolean exportarDatosUsuario(Usuario usuario, File archivoDestino) {
        List<Mensaje> todosLosMensajes = cargarConversacion().getMensajes();
        String nombreUsuario = usuario.getNombre();

        // Filtrar todos los mensajes donde el usuario es remitente o destinatario
        List<Mensaje> mensajesDelUsuario = todosLosMensajes.stream()
                .filter(m -> m.getRemitente().equalsIgnoreCase(nombreUsuario) || m.getDestinatario().equalsIgnoreCase(nombreUsuario))
                .collect(Collectors.toList());

        if (mensajesDelUsuario.isEmpty()) {
            System.out.println("El usuario no tiene mensajes para exportar.");
            return false;
        }

        // Escribir los mensajes en formato CSV
        try (FileWriter writer = new FileWriter(archivoDestino)) {

            // Escribir la cabecera del CSV
            writer.write("Fecha,Remitente,Destinatario,Contenido,Adjunto\n");

            // Recorrer todos los mensajes del usuario
            for (Mensaje mensaje : mensajesDelUsuario) {
                String adjunto = ""; // Valor por defecto (sin adjunto)

                if (mensaje.getAdjunto() != null) {
                    adjunto = mensaje.getAdjunto().getNombre();
                }

                // Escribir una línea del CSV con los datos del mensaje
                writer.write(
                        mensaje.getFecha() + "," +
                                mensaje.getRemitente() + "," +
                                mensaje.getDestinatario() + "," +
                                "\"" + mensaje.getContenido().replace("\"", "\"\"") + "\"," +
                                adjunto + "\n"
                );
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al exportar los datos del usuario: " + e.getMessage());
            return false;
        }
    }
}
