package model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "conversacion")
public class Conversacion {
    private List<Mensaje> mensajes = new ArrayList<>();

    @XmlElement(name = "mensaje")
    public List<Mensaje> getMensajes() {
        return mensajes;
    }
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
    public void addMensaje(Mensaje m) {
        mensajes.add(m);
    }
}
