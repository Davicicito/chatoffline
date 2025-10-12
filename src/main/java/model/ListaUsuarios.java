package model;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "usuarios")
public class ListaUsuarios {
    private List<Usuario> usuarios = new ArrayList<>();

    @XmlElement(name = "usuario")
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    public void setUsuario(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    public void addUsuario(Usuario u) {
        usuarios.add(u);
    }
    public boolean existeUsurio(String email){
        return usuarios.stream().anyMatch(usuario -> usuario.getEmail().equalsIgnoreCase(email));
    }
}
