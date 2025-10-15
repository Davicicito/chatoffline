package model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adjunto")
public class Adjunto {

    private String nombre; // nombre del archivo
    private String ruta;   // ubicación dentro de /media/
    private String tipo;   // tipo de archivo (imagen, pdf, etc.)

    public Adjunto() {}

    public Adjunto(String nombre, String ruta, String tipo) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.tipo = tipo;
    }

    @XmlElement
    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    @XmlElement
    public String getRuta() { return ruta; }

    public void setRuta(String ruta) { this.ruta = ruta; }

    @XmlElement
    public String getTipo() { return tipo; }

    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
