package model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adjunto")
public class Adjunto {
    private String nombreArchivo;
    private String rutaArchivo;
    private long tamañoArchivo;
    private String tipoArchivo;

    public Adjunto() { // Constructor vacío para JAXB
    }

    public Adjunto(String nombreArchivo, String rutaArchivo, long tamañoArchivo, String tipoArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
        this.tamañoArchivo = tamañoArchivo;
        this.tipoArchivo = tipoArchivo;
    }

    @XmlElement
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    @XmlElement
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    @XmlElement
    public long getTamañoArchivo() {
        return tamañoArchivo;
    }
    public void setTamañoArchivo(long tamañoArchivo) {
        this.tamañoArchivo = tamañoArchivo;
    }
    @XmlElement
    public String getTipoArchivo() {
        return tipoArchivo;
    }
    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    @Override
    public String toString() {
        return nombreArchivo + " (" + tipoArchivo + ", " + tamañoArchivo + " bytes)";
    }

}
