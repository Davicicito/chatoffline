package model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(name = "mensaje")
public class Mensaje {
    private String remitente;
    private String destinatario;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private Adjunto adjunto;


    public Mensaje() { // Constructor vacío para JAXB
    }

    public Mensaje(String remitente, String destinatario, String contenido, LocalDateTime fechaEnvio, Adjunto adjunto) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
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
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
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
        return "Mensaje [remitente=" + remitente + ", destinatario=" + destinatario + ", contenido=" + contenido
                + ", fechaEnvio=" + fechaEnvio + ", adjunto=" + adjunto.getNombreArchivo() + "]";
    }
}
