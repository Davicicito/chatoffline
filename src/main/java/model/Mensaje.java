package model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(name = "mensaje")
public class Mensaje {

    private String remitente;
    private String destinatario;
    private String contenido;
    private String fecha;     // fecha guardada como texto (simple y efectivo)
    private Adjunto adjunto;  // puede ser null si no hay archivo

    public Mensaje() {
        // Constructor vacío requerido por JAXB
    }

    public Mensaje(String remitente, String destinatario, String contenido, Adjunto adjunto) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        java.time.format.DateTimeFormatter formato =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = java.time.LocalDateTime.now().format(formato); // guarda la fecha actual como texto
        this.adjunto = adjunto;
    }

    @XmlElement
    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    @XmlElement
    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    @XmlElement
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @XmlElement
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @XmlElement
    public Adjunto getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(Adjunto adjunto) {
        this.adjunto = adjunto;
    }

    @Override
    public String toString() {
        String texto = "[" + fecha + "] " + remitente + ": " + contenido;
        if (adjunto != null) {
            texto += " [Adjunto: " + adjunto.getNombre() + "]";
        }
        return texto;
    }
}
