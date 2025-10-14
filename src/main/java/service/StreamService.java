package service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import model.Conversacion;
import model.Mensaje;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class StreamService {
    private final File archivoMensajes = new File("data/mensajes.xml");

    // Cargar conversación desde el XML (solo lectura)
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
//    resumen de la conversacion
    public void mostrarConversacion(String usuario1, String usuario2){
        Conversacion conversacion = cargarConversacion();

        List<Mensaje> mensajes = conversacion.getMensajes().stream()
                .filter(m ->
                        (m.getRemitente().equalsIgnoreCase(usuario1) && m.getDestinatario().equalsIgnoreCase(usuario2)) ||
                                (m.getRemitente().equalsIgnoreCase(usuario2) && m.getDestinatario().equalsIgnoreCase(usuario1))
                )
                .collect(Collectors.toList());

                if(mensajes.isEmpty()){
                    System.out.println("No hay mensajes entre " + usuario1 + " y " + usuario2);
                    return;
                }
        System.out.println("Resumen de conversacion entre " + usuario1 + " y " + usuario2);

        System.out.println("Total de mensajes: "+ mensajes.size());

        long recibidosPorUsuario1 = mensajes.stream()
                .filter(m -> m.getDestinatario().equalsIgnoreCase(usuario1))
                .count();
        long recibidosPorUsuario2 = mensajes.stream()
                .filter(m -> m.getDestinatario().equalsIgnoreCase(usuario2))
                .count();

        System.out.println("Mensajes recibidos para " + usuario1 + ": " + recibidosPorUsuario1);
        System.out.println("Mensajes recibidos para " + usuario2 + ": " + recibidosPorUsuario2);

    }
}
