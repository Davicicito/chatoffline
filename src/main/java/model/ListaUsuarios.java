package model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "usuarios")
public class ListaUsuarios {

    private List<Usuario> usuarios = new ArrayList<>();

    public ListaUsuarios(List<Usuario> usuarios) {} // necesario para JAXB

    @XmlElement(name = "usuario")
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void addUsuario(Usuario u) {
        usuarios.add(u);
    }

    public boolean existeUsuario(String email) {
        return usuarios.stream().anyMatch(usuario -> usuario.getEmail().equalsIgnoreCase(email));
    }
}

