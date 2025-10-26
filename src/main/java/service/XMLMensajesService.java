package service;

import jakarta.xml.bind.*;
import model.Mensaje;
import model.Conversacion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XMLMensajesService {
    private final File archivoMensajes = new File("data/mensajes.xml");

    /**
     * Carga la conversación completa desde el archivo mensajes.xml.
     * Si el archivo no existe, devuelve una conversación vacía.
     * @return un objeto Conversacion con todos los mensajes cargados del XML.
     */
    public Conversacion cargarConversacion() {
        try {
            if (!archivoMensajes.exists()) {
                return new Conversacion();
            }

            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Conversacion) unmarshaller.unmarshal(archivoMensajes);
        } catch (Exception e) {
            return new Conversacion();
        }
    }

    /**
     * Guarda todos los mensajes en el archivo mensajes.xml.
     * Si no existe la carpeta "data", la crea automáticamente.
     * @param conversacion La conversación completa que se quiere guardar.
     */
    public void guardarConversacion(Conversacion conversacion) {
        try {
            File dir = archivoMensajes.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(conversacion, archivoMensajes);
        } catch (Exception e) {
            System.out.println("Error al guardar mensajes: " + e.getMessage());
        }
    }

    /**
     * Envía (guarda) un nuevo mensaje en el XML.
     * Primero se cargan los mensajes que ya existen, se añade el nuevo,
     * y después se vuelve a guardar todo el archivo.
     * @param mensaje El mensaje que se va a guardar.
     */
    public void enviarMensaje(Mensaje mensaje) {
        Conversacion conversacion = cargarConversacion();
        conversacion.getMensajes().add(mensaje);
        guardarConversacion(conversacion);
    }

    /**
     * Devuelve todos los mensajes que hay entre dos usuarios.
     * Filtra los mensajes del XML para mostrar solo los de esa conversación.
     * @param usuario1 Primer usuario.
     * @param usuario2 Segundo usuario.
     * @return Lista de mensajes entre ambos usuarios.
     */
    public List<Mensaje> obtenerConversacion(String usuario1, String usuario2) {
        return cargarConversacion().getMensajes().stream()
                .filter(m -> (m.getRemitente().equalsIgnoreCase(usuario1) && m.getDestinatario().equalsIgnoreCase(usuario2)) ||
                             (m.getRemitente().equalsIgnoreCase(usuario2) && m.getDestinatario().equalsIgnoreCase(usuario1)))
                .collect(Collectors.toList());
    }
}
