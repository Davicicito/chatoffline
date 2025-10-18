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

    // 🔹 Cargar todos los mensajes desde XML (hecho público)
    public Conversacion cargarConversacion() {
        try {
            if (!archivoMensajes.exists()) return new Conversacion();

            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Conversacion) unmarshaller.unmarshal(archivoMensajes);
        } catch (Exception e) {
            System.out.println("Error al cargar mensajes: " + e.getMessage());
            return new Conversacion();
        }
    }

    // 🔹 Guardar todos los mensajes en XML (hecho público)
    public void guardarConversacion(Conversacion conversacion) {
        try {
            archivoMensajes.getParentFile().mkdirs(); // crea "data/" si no existe
            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(conversacion, archivoMensajes);
        } catch (Exception e) {
            System.out.println("Error al guardar mensajes: " + e.getMessage());
        }
    }

    // 🔹 Enviar (guardar) un nuevo mensaje
    public void enviarMensaje(Mensaje mensaje) {
        Conversacion conversacion = cargarConversacion();
        conversacion.getMensajes().add(mensaje);
        guardarConversacion(conversacion);
    }

    // 🔹 Obtener los mensajes entre dos usuarios
    public List<Mensaje> obtenerConversacion(String usuario1, String usuario2) {
        return cargarConversacion().getMensajes().stream()
                .filter(m -> (m.getRemitente().equalsIgnoreCase(usuario1) && m.getDestinatario().equalsIgnoreCase(usuario2)) ||
                             (m.getRemitente().equalsIgnoreCase(usuario2) && m.getDestinatario().equalsIgnoreCase(usuario1)))
                .collect(Collectors.toList());
    }
}
