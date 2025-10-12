package Service;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import model.Conversacion;
import model.Mensaje;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
public class XMLMensajesService {
    private final File archivoMensajes = new File("data/mensajes.xml");

    public Conversacion cargarConversacion(){
        try {
            if (!archivoMensajes.exists()) {
                return new Conversacion();
            }
            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Conversacion) unmarshaller.unmarshal(archivoMensajes);
        } catch (Exception e) {
            System.out.println("Error al cargar mensajes desde XML: " + e.getMessage());
            return new Conversacion();
        }
    }
    public void guardarConversacion(Conversacion conversacion){
        try {
            JAXBContext context = JAXBContext.newInstance(Conversacion.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(conversacion, archivoMensajes);
        } catch (Exception e) {
            System.out.println("Error al guardar mensajes en XML: " + e.getMessage());
        }
    }

    public void enviarMensaje(Mensaje nuevo){
        Conversacion conversacion = cargarConversacion();
        conversacion.addMensaje(nuevo);
        guardarConversacion(conversacion);
        System.out.println("Mensaje enviado: " + nuevo);
    }
    public void mostrarConversacion(String usuario1, String usuario2){
        Conversacion conversacion = cargarConversacion();
        List<Mensaje> mensajesFiltrados = conversacion.getMensajes().stream()
                .filter(m ->
                        (m.getRemitente().equalsIgnoreCase(usuario1) && m.getDestinatario().equalsIgnoreCase(usuario2)) ||
                                (m.getRemitente().equalsIgnoreCase(usuario2) && m.getDestinatario().equalsIgnoreCase(usuario1))
                )
                .collect(Collectors.toList());
        if(mensajesFiltrados.isEmpty()){
            System.out.println("No hay mensajes entre " + usuario1 + " y " + usuario2);
        }


        System.out.println("Conversación entre " + usuario1 + " y " + usuario2 + ":");
        for(Mensaje m : mensajesFiltrados){
            System.out.println(m);
        }
    }
}



